package optimal.execution;

import optimal.BestMutationRateSearcher;
import optimal.configuration.Configuration;
import optimal.configuration.ConfigurationsLoader;
import optimal.configuration.OneExperimentConfiguration;

import javax.naming.ConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

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
                    ResultsConsumer.ResultType.INTERMEDIATE);
            resultsConsumer.addWriter(writer);
        } catch (IOException e) {
            System.err.println("Failed to add writer for intermediate results");
            e.printStackTrace();
        }

        loggingResultsService.execute(resultsConsumer);

        List<Future<?>> futures = new ArrayList<>();
        for (OneExperimentConfiguration oneExperimentConfiguration : configuration.experimentConfigurations) {
            futures.add(executor.submit(() -> {
                BestMutationRateSearcher searcher = new BestMutationRateSearcher(oneExperimentConfiguration);
                searcher.addListener(resultsConsumer::consumeResult);
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
