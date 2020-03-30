package optimal.execution;

import optimal.BestMutationRateSearcher;
import optimal.configuration.Configuration;
import optimal.configuration.ConfigurationsLoader;
import optimal.configuration.OneExperimentConfiguration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ExperimentRunner {

    private final ConfigurationsLoader configurationsLoader = new ConfigurationsLoader();
    final ResultsWriter resultsWriter;

    public ExperimentRunner() throws IOException, URISyntaxException {
        resultsWriter = new ResultsWriter();
    }

    ExperimentRunner(String fileForResults) throws IOException {
        resultsWriter = new ResultsWriter(fileForResults);
    }

    public void runExperiments() {
        ConfigurationsLoader configurationsLoader = new ConfigurationsLoader();
        Configuration configuration;
        try {
            configuration = configurationsLoader.getConfiguration();
        } catch (FileNotFoundException | URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(configuration.amountOfThreads);
        ExecutorService loggingResultsService = Executors.newSingleThreadExecutor();

        loggingResultsService.execute(resultsWriter);

        List<Future<?>> futures = new ArrayList<>();
        for (OneExperimentConfiguration oneExperimentConfiguration : configuration.experimentConfigurations) {
            futures.add(executor.submit(() -> {
                BestMutationRateSearcher searcher = new BestMutationRateSearcher(oneExperimentConfiguration);
                searcher.addListener(resultsWriter::addToQueue);
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
