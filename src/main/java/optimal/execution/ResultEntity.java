package optimal.execution;

import optimal.configuration.OneExperimentConfiguration;

public class ResultEntity {
    public final OneExperimentConfiguration configuration;
    public final int fitness;
    public final double bestProbability;
    public final double optimizationTime;

    public ResultEntity(OneExperimentConfiguration configuration, int fitness, double bestProbability, double optimizationTime) {
        this.configuration = configuration;
        this.fitness = fitness;
        this.bestProbability = bestProbability;
        this.optimizationTime = optimizationTime;
    }
}
