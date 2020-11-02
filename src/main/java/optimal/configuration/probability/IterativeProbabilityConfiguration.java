package optimal.configuration.probability;

import optimal.probabilitySampling.ProbabilitySamplingStrategy;
import org.jetbrains.annotations.NotNull;

import javax.naming.ConfigurationException;
import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IterativeProbabilityConfiguration that = (IterativeProbabilityConfiguration) o;
        return Double.compare(that.minMutationProbability, minMutationProbability) == 0 &&
                Double.compare(that.maxMutationProbability, maxMutationProbability) == 0 &&
                Double.compare(that.precisionForProbability, precisionForProbability) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minMutationProbability, maxMutationProbability, precisionForProbability);
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

    @Override
    public void validate() throws ConfigurationException {
        if (minMutationProbability == 0. &&
                maxMutationProbability == 0. &&
                precisionForProbability == 0.) {
            throw new ConfigurationException("Probabilities were not initialized");
        }
        if (maxMutationProbability < minMutationProbability) {
            throw new ConfigurationException("Max mutation probability should not be smaller than min, min = " + minMutationProbability + " max = " + maxMutationProbability);
        }
    }
}
