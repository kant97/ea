package optimal.configuration.probability;

import optimal.probabilitySampling.ProbabilitySamplingStrategy;
import org.jetbrains.annotations.NotNull;

public class IterativeProbabilityConfiguration extends ProbabilitySamplingConfiguration {
    public final double minMutationProbability;
    public final double maxMutationProbability;
    public final double precisionForProbability;

    public IterativeProbabilityConfiguration(double minMutationProbability,
                                             double maxMutationProbability, double precisionForProbability) {
        this.minMutationProbability = minMutationProbability;
        this.maxMutationProbability = maxMutationProbability;
        this.precisionForProbability = precisionForProbability;
    }

    @NotNull
    @Override
    public ProbabilitySamplingStrategy getStrategy() {
        return ProbabilitySamplingStrategy.ITERATIVE;
    }

    @Override
    public String toString() {
        return "IterativeProbabilityConfiguration{" +
                "minMutationProbability=" + minMutationProbability +
                ", maxMutationProbability=" + maxMutationProbability +
                ", precisionForProbability=" + precisionForProbability +
                ", strategy=" + getStrategy().toString() +
                '}';
    }
}
