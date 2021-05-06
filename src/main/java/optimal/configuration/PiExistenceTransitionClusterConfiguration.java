package optimal.configuration;

import optimal.configuration.algorithms.AlgorithmConfig;
import optimal.configuration.problems.ProblemConfig;
import optimal.configuration.runs.StopConditionConfiguration;
import org.jetbrains.annotations.NotNull;

public class PiExistenceTransitionClusterConfiguration extends ProbabilityVectorGenerationConfiguration {
    private final int piExistenceClassId;
    public PiExistenceTransitionClusterConfiguration(double probability, int fitness, @NotNull ProblemConfig problemConfig, @NotNull AlgorithmConfig algorithmConfig, @NotNull StopConditionConfiguration stopConditionConfig, String outputFileName, String outputDirectory, int piExistenceClassId) {
        super(probability, fitness, problemConfig, algorithmConfig, stopConditionConfig, outputFileName, outputDirectory);
        this.piExistenceClassId = piExistenceClassId;
    }

    public int getPiExistenceClassId() {
        return piExistenceClassId;
    }
}
