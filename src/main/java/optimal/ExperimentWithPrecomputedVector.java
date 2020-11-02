package optimal;

import optimal.configuration.OptimalMutationRateSearchingSingleExperimentConfiguration;
import optimal.configuration.loaders.OptimalMutationRateSearchingSingleExperimentConfigurationLoader;
import optimal.execution.ProgressTracker;
import optimal.execution.ResultWriter;
import optimal.execution.ResultsConsumer;
import optimal.execution.events.EventType;
import optimal.execution.events.ResultEntityObtainedEvent;

import javax.naming.ConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;

import static optimal.execution.ResultsConsumer.OPTIMAL_MUTATION_RATE_RESULTS_FILE_NAME;

public class ExperimentWithPrecomputedVector {
    public static void main(String[] args) throws IOException {
        final OptimalMutationRateSearchingSingleExperimentConfigurationLoader configurationLoader =
                new OptimalMutationRateSearchingSingleExperimentConfigurationLoader(
                        "precomputedVectorExperimentConfigs.json");
        final OptimalMutationRateSearchingSingleExperimentConfiguration configuration;
        try {
            configuration = configurationLoader.getConfiguration();
        } catch (FileNotFoundException | ConfigurationException e) {
            throw new IllegalStateException("Failed to load configurations", e);
        }
        final BestMutationRateSearcher searcher = new BestMutationRateSearcher(configuration);

        final ResultWriter optimalResultsWriter = new ResultWriter(OPTIMAL_MUTATION_RATE_RESULTS_FILE_NAME,
                EventType.OPTIMAL_RESULT_READY);
        final ResultWriter allResultsWriter = new ResultWriter(ResultsConsumer.ALL_MUTATION_RATES_RESULTS_FILE_NAME,
                EventType.INTERMEDIATE_RESULT_READY);
        searcher.addListener(event -> {
            if (event instanceof ResultEntityObtainedEvent) {
                allResultsWriter.writeResultsForMyType(((ResultEntityObtainedEvent) event).getResultEntity(),
                        event.getEventType());
            }
        }, EventType.INTERMEDIATE_RESULT_READY);
        searcher.addListener(event -> {
            if (event instanceof ResultEntityObtainedEvent) {
                optimalResultsWriter.writeResultsForMyType(((ResultEntityObtainedEvent) event).getResultEntity(),
                        event.getEventType());
            }
        }, EventType.OPTIMAL_RESULT_READY);
        final ProgressTracker progressTracker = new ProgressTracker();
        searcher.addListener(event -> progressTracker.updateProgress(), EventType.PROGRESS_UPDATE);
        searcher.getBestMutationProbabilities();
    }
}
