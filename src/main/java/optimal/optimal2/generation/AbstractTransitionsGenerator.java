package optimal.optimal2.generation;

import optimal.configuration.MainConfiguration;
import optimal.configuration.algorithms.AlgorithmConfig;
import optimal.configuration.problems.ProblemConfig;
import optimal.configuration.runs.StopConditionConfiguration;
import optimal.configuration.transitionsGeneration.TransitionsGenerationConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static optimal.configuration.runs.StopConditionConfiguration.Strategy.FIXED_RUNS;
import static optimal.configuration.runs.StopConditionConfiguration.Strategy.FIXED_SUCCESS;

public abstract class AbstractTransitionsGenerator {
    public static AbstractTransitionsGenerator create(@NotNull MainConfiguration experimentConfiguration) {
        final TransitionsGenerationConfiguration.TransitionsGenerationStrategy generationStrategy = experimentConfiguration.getTransitionsGeneration().getStrategy();
        if (generationStrategy == TransitionsGenerationConfiguration.TransitionsGenerationStrategy.RUN_TIME_TRANSITIONS_GENERATION) {
            return createRunTimeTransitionsGenerator(experimentConfiguration.getTransitionsGeneration().getStopConditionConfig(), experimentConfiguration.getProblemConfig(), experimentConfiguration.getAlgorithmConfig());
        } else if (generationStrategy == TransitionsGenerationConfiguration.TransitionsGenerationStrategy.PRECOMPUTED_TRANSITIONS_READING) {
            return new PrecomputedTransitionsReader(experimentConfiguration);
        }
        throw new IllegalStateException("Strategy " + generationStrategy.name() + " is not supported");
    }

    public static AbstractTransitionsGenerator createRunTimeTransitionsGenerator(@NotNull StopConditionConfiguration stopConditionConfiguration,
                                                                                 @NotNull ProblemConfig problemConfig,
                                                                                 @NotNull AlgorithmConfig algorithmConfig) {
        StopConditionConfiguration.Strategy stopConditionStrategy = stopConditionConfiguration.getMyStrategy();
        if (stopConditionStrategy == FIXED_RUNS) {
            return new TransitionsGeneratorFixedRuns(stopConditionConfiguration, problemConfig, algorithmConfig);
        } else if (stopConditionStrategy == FIXED_SUCCESS) {
            return new TransitionGenerationFixedSuccess(stopConditionConfiguration, problemConfig, algorithmConfig);
        }
        throw new IllegalStateException("Strategy " + stopConditionStrategy + " is not supported");
    }

    public abstract @NotNull Map<Integer, Double> getTransitionsProbabilities(double r, int piExistenceClassId);
}
