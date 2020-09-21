package optimal.execution;

import optimal.BestMutationRateSearcher;
import optimal.configuration.Configuration;
import optimal.configuration.ConfigurationsLoader;
import optimal.configuration.OneExperimentConfiguration;
import optimal.execution.events.EventType;
import optimal.execution.events.EventsManager;
import optimal.execution.events.ResultEntityObtainedEvent;

import javax.naming.ConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class ExperimentRunner {

    private final ResultsConsumer resultsConsumer;

    public ExperimentRunner() throws IOException {
        resultsConsumer = ResultsConsumer.createResultsConsumer(true);
    }

    public void runExperiments() {
        ConfigurationsLoader configurationsLoader = new ConfigurationsLoader();
        Configuration configuration;
        try {
            configuration = configurationsLoader.getConfiguration();
        } catch (FileNotFoundException | URISyntaxException | ConfigurationException e) {
            e.printStackTrace();
            return;
        }

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(configuration.amountOfThreads);
        ExecutorService loggingResultsService = Executors.newSingleThreadExecutor();

        ResultWriter writer;
        try {
            writer = new ResultWriter(ResultsConsumer.ALL_MUTATION_RATES_RESULTS_FILE_NAME,
                    EventType.INTERMEDIATE_RESULT_READY);
            resultsConsumer.addWriter(writer);
        } catch (IOException e) {
            System.err.println("Failed to add writer for intermediate results");
            e.printStackTrace();
        }

        loggingResultsService.execute(resultsConsumer);

        List<Future<?>> futures = new ArrayList<>();
        final Consumer<EventsManager.Event> resultsListener = event -> {
            if (event instanceof ResultEntityObtainedEvent) {
                resultsConsumer.consumeResult(((ResultEntityObtainedEvent) event).getResultEntity(), event.getEventType());
            }
        };
        for (OneExperimentConfiguration oneExperimentConfiguration : configuration.experimentConfigurations) {
            futures.add(executor.submit(() -> {
                BestMutationRateSearcher searcher = new BestMutationRateSearcher(oneExperimentConfiguration);
                searcher.addListener(resultsListener, EventType.INTERMEDIATE_RESULT_READY);
                searcher.addListener(resultsListener, EventType.OPTIMAL_RESULT_READY);
                searcher.getBestMutationProbabilities();
            }));
        }

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
        try {
            if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
        loggingResultsService.shutdownNow();
    }

}
