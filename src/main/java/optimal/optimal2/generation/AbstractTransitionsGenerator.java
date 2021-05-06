package optimal.optimal2.generation;

import optimal.configuration.OneExperimentConfiguration;
import optimal.configuration.algorithms.AlgorithmConfig;
import optimal.configuration.problems.ProblemConfig;
import optimal.configuration.runs.StopConditionConfiguration;
import optimal.configuration.vectorGeneration.VectorGenerationConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static optimal.configuration.runs.StopConditionConfiguration.Strategy.FIXED_RUNS;
import static optimal.configuration.runs.StopConditionConfiguration.Strategy.FIXED_SUCCESS;

public abstract class AbstractTransitionsGenerator {
    public static AbstractTransitionsGenerator create(@NotNull StopConditionConfiguration stopConditionConfiguration,
                                                      @NotNull ProblemConfig problemConfig,
                                                      @NotNull AlgorithmConfig algorithmConfig,
                                                      @NotNull VectorGenerationConfiguration.VectorGenerationStrategy strategy) {
        if (strategy == VectorGenerationConfiguration.VectorGenerationStrategy.RUN_TIME_VECTOR_GENERATION) {
            StopConditionConfiguration.Strategy configMyStrategy = stopConditionConfiguration.getMyStrategy();
            if (configMyStrategy == FIXED_RUNS) {
                return new TransitionsGeneratorFixedRuns(stopConditionConfiguration, problemConfig, algorithmConfig);
            } else if (configMyStrategy == FIXED_SUCCESS) {
                return new TransitionGenerationFixedSuccess(stopConditionConfiguration, problemConfig, algorithmConfig);
            }
            throw new IllegalStateException("Strategy " + configMyStrategy + " is not supported");
        } else if (strategy == VectorGenerationConfiguration.VectorGenerationStrategy.PRECOMPUTED_VECTOR_READING) {
            throw new IllegalStateException("Strategy " + strategy.name() + " is not supported");
        }
        throw new IllegalStateException("Strategy " + strategy.name() + " is not supported");
    }

    public abstract @NotNull Map<Integer, Double> getTransitionsProbabilities(double r, int piExistenceClassId);
}
