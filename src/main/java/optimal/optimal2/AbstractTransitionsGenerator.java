package optimal.optimal2;

import optimal.configuration.OneExperimentConfiguration;
import optimal.configuration.runs.StopConditionConfiguration;
import optimal.configuration.vectorGeneration.VectorGenerationConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static optimal.configuration.runs.StopConditionConfiguration.Strategy.FIXED_RUNS;
import static optimal.configuration.runs.StopConditionConfiguration.Strategy.FIXED_SUCCESS;

public abstract class AbstractTransitionsGenerator {
    public static AbstractTransitionsGenerator create(@NotNull OneExperimentConfiguration configuration,@NotNull VectorGenerationConfiguration.VectorGenerationStrategy strategy) {
        if (strategy == VectorGenerationConfiguration.VectorGenerationStrategy.RUN_TIME_VECTOR_GENERATION) {
            StopConditionConfiguration.Strategy configMyStrategy = configuration.stopConditionConfig.getMyStrategy();
            if (configMyStrategy == FIXED_RUNS) {
                return new TransitionsGeneratorFixedRuns(configuration);
            } else if (configMyStrategy == FIXED_SUCCESS) {
                throw new IllegalStateException("Strategy " + configMyStrategy + " is not supported");
            }
            throw new IllegalStateException("Strategy " + configMyStrategy + " is not supported");
        } else if (strategy == VectorGenerationConfiguration.VectorGenerationStrategy.PRECOMPUTED_VECTOR_READING) {
            throw new IllegalStateException("Strategy " + strategy.name() + " is not supported");
        }
        throw new IllegalStateException("Strategy " + strategy.name() + " is not supported");
    }

    public abstract @NotNull Map<Integer, Double> getTransitionsProbabilities(double r, int piExistenceClassId);
}
