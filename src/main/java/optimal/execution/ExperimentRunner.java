package optimal.execution;

import optimal.BestMutationRateSearcher;
import optimal.configuration.OneExperimentConfiguration;
import optimal.configuration.UsualConfiguration;
import optimal.configuration.loaders.UsualConfigurationsLoader;
import optimal.execution.events.EventType;
import optimal.execution.events.EventsManager;
import optimal.execution.events.ResultEntityObtainedEvent;

import javax.naming.ConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.StandardOpenOption;
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
        final UsualConfigurationsLoader configurationsLoader = new UsualConfigurationsLoader();
        final UsualConfiguration configuration;
        try {
            configuration = configurationsLoader.getConfiguration();
        } catch (ConfigurationException | IOException e) {
            e.printStackTrace();
            return;
        }

        final ThreadPoolExecutor executor =
                (ThreadPoolExecutor) Executors.newFixedThreadPool(configuration.amountOfThreads);
        final ExecutorService loggingResultsService = Executors.newSingleThreadExecutor();

        final ResultWriter writer;
        try {
            writer = new ResultWriter(ResultsConsumer.ALL_MUTATION_RATES_RESULTS_FILE_NAME,
                    EventType.INTERMEDIATE_RESULT_READY, StandardOpenOption.APPEND);
            resultsConsumer.addWriter(writer);
        } catch (IOException e) {
            System.err.println("Failed to add writer for intermediate results");
            e.printStackTrace();
        }

        loggingResultsService.execute(resultsConsumer);

        final List<Future<?>> futures = new ArrayList<>();
        final Consumer<EventsManager.Event> resultsListener = event -> {
            if (event instanceof ResultEntityObtainedEvent) {
                resultsConsumer.consumeResult(((ResultEntityObtainedEvent) event).getResultEntity(), event.getEventType());
            }
        };
        final ProgressTracker progressTracker = new ProgressTracker();
        for (OneExperimentConfiguration oneExperimentConfiguration : configuration.experimentConfigurations) {
            futures.add(executor.submit(() -> {
                final BestMutationRateSearcher searcher = new BestMutationRateSearcher(oneExperimentConfiguration);
                searcher.addListener(resultsListener, EventType.INTERMEDIATE_RESULT_READY);
                searcher.addListener(resultsListener, EventType.OPTIMAL_RESULT_READY);
                searcher.addListener(event -> progressTracker.updateProgress(), EventType.PROGRESS_UPDATE);
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
