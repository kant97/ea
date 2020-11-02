package optimal.probability;

import optimal.configuration.AbstractSingleExperimentConfiguration;
import optimal.configuration.OptimalMutationRateSearchingSingleExperimentConfiguration;
import optimal.configuration.algorithms.AlgorithmConfig;
import optimal.configuration.runs.StopConditionConfiguration;
import optimal.execution.cluster.ConfigurationToNumberTranslator;
import org.jetbrains.annotations.NotNull;
import problem.Problem;

public class ProbabilityVectorGeneratorManager {
    private static AbstractSingleExperimentConfiguration CURRENT_CONFIGURATION = null;
    private static ConfigurationToNumberTranslator CONFIGURATION_TO_NUMBER_TRANSLATOR = null;

    public static @NotNull ProbabilityVectorGeneratorInRuntime getProbabilityVectorGeneratorInRuntime(double probability,
                                                                                                      @NotNull Problem problem,
                                                                                                      @NotNull AlgorithmConfig algorithmConfig,
                                                                                                      @NotNull StopConditionConfiguration stopStrategy) {
        return ProbabilityVectorGeneratorInRuntime.createProbabilityVectorGeneratorByStrategy(probability, problem,
                algorithmConfig, stopStrategy);
    }

    private static void updateState(@NotNull AbstractSingleExperimentConfiguration configuration) {
        CURRENT_CONFIGURATION = configuration;
        CONFIGURATION_TO_NUMBER_TRANSLATOR = new ConfigurationToNumberTranslator(configuration);
    }

    public static @NotNull PrecomputedProbabilityVectorsReader getProbabilityVectorReaderFromPrecomputedFiles(
            @NotNull OptimalMutationRateSearchingSingleExperimentConfiguration configuration,
            double probability, int fitness) {
        if (CURRENT_CONFIGURATION == null || !CURRENT_CONFIGURATION.equals(configuration)) {
            updateState(configuration);
        }
        assert CONFIGURATION_TO_NUMBER_TRANSLATOR != null;
        return new PrecomputedProbabilityVectorsReader(configuration, CONFIGURATION_TO_NUMBER_TRANSLATOR, probability
                , fitness);
    }
}
