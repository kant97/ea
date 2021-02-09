package optimal;

import optimal.configuration.OptimalMutationRateSearchingSingleExperimentConfiguration;
import optimal.configuration.loaders.OptimalMutationRateSearchingSingleExperimentConfigurationLoader;
import optimal.execution.ProgressTracker;
import optimal.execution.ResultWriter;
import optimal.execution.ResultsConsumer;
import optimal.execution.cluster.Utils;
import optimal.execution.events.EventType;
import optimal.execution.events.ResultEntityObtainedEvent;
import org.jetbrains.annotations.NotNull;

import javax.naming.ConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.StandardOpenOption;

import static optimal.execution.ResultsConsumer.OPTIMAL_MUTATION_RATE_RESULTS_FILE_NAME;

public class ExperimentWithPrecomputedVector {
    private final String myResultsDirectoryName;
    private final OptimalMutationRateSearchingSingleExperimentConfiguration configuration;
    private final @NotNull StandardOpenOption[] myFileOpenMode;

    public ExperimentWithPrecomputedVector(String myResultsDirectoryName,
                                           OptimalMutationRateSearchingSingleExperimentConfiguration configuration,
                                           @NotNull StandardOpenOption... fileOpenMode) {
        this.myResultsDirectoryName = myResultsDirectoryName;
        this.configuration = configuration;
        myFileOpenMode = fileOpenMode;

        Utils.createDirectory(myResultsDirectoryName);
    }

    private String getResultsFileFqn(String fileName) {
        return myResultsDirectoryName + "/" + fileName;
    }

    private String getOptimalResultsFileFqn() {
        return getResultsFileFqn(OPTIMAL_MUTATION_RATE_RESULTS_FILE_NAME);
    }

    private String getAllResultsFileFqn() {
        return getResultsFileFqn(ResultsConsumer.ALL_MUTATION_RATES_RESULTS_FILE_NAME);
    }

    public void runExperiment() {
        final BestMutationRateSearcher searcher = new BestMutationRateSearcher(configuration);

        final ResultWriter optimalResultsWriter;
        try {
            optimalResultsWriter = new ResultWriter(getOptimalResultsFileFqn(), EventType.OPTIMAL_RESULT_READY,
                    myFileOpenMode);
            searcher.addListener(event -> {
                if (event instanceof ResultEntityObtainedEvent) {
                    optimalResultsWriter.writeResultsForMyType(((ResultEntityObtainedEvent) event).getResultEntity(),
                            event.getEventType());
                }
            }, EventType.OPTIMAL_RESULT_READY);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        final ResultWriter allResultsWriter;
        try {
            allResultsWriter = new ResultWriter(getAllResultsFileFqn(), EventType.INTERMEDIATE_RESULT_READY,
                    myFileOpenMode);
            searcher.addListener(event -> {
                if (event instanceof ResultEntityObtainedEvent) {
                    allResultsWriter.writeResultsForMyType(((ResultEntityObtainedEvent) event).getResultEntity(),
                            event.getEventType());
                }
            }, EventType.INTERMEDIATE_RESULT_READY);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        final ProgressTracker progressTracker = new ProgressTracker();
        searcher.addListener(event -> progressTracker.updateProgress(), EventType.PROGRESS_UPDATE);
        searcher.getBestMutationProbabilities();
    }

    public static void main(String[] args) {
        final OptimalMutationRateSearchingSingleExperimentConfigurationLoader configurationLoader =
                new OptimalMutationRateSearchingSingleExperimentConfigurationLoader(
                        "precomputedVectorExperimentConfigs.json");
        final OptimalMutationRateSearchingSingleExperimentConfiguration configuration;
        try {
            configuration = configurationLoader.getConfiguration();
        } catch (ConfigurationException | IOException e) {
            throw new IllegalStateException("Failed to load configurations", e);
        }
        new ExperimentWithPrecomputedVector("optimal_results", configuration,
                StandardOpenOption.APPEND).runExperiment();
    }
}
