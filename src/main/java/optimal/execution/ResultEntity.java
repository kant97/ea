package optimal.execution;

import optimal.configuration.AbstractSingleExperimentConfiguration;
import optimal.configuration.OneExperimentConfiguration;

public class ResultEntity {
    public final AbstractSingleExperimentConfiguration configuration;
    public final int fitness;
    public final double bestProbability;
    public final double optimizationTime;

    public ResultEntity(AbstractSingleExperimentConfiguration configuration, int fitness, double bestProbability,
                        double optimizationTime) {
        this.configuration = configuration;
        this.fitness = fitness;
        this.bestProbability = bestProbability;
        this.optimizationTime = optimizationTime;
    }

    @Override
    public String toString() {
        return "ResultEntity{" +
                "configuration=" + configuration.toString() +
                ", fitness=" + fitness +
                ", bestProbability=" + bestProbability +
                ", optimizationTime=" + optimizationTime +
                '}';
    }
}
