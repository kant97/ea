package optimal.configuration.probability;

import optimal.probabilitySampling.ProbabilitySamplingStrategy;
import org.jetbrains.annotations.NotNull;

public abstract class ProbabilitySamplingConfiguration {
    public abstract @NotNull ProbabilitySamplingStrategy getStrategy();

    @Override
    public String toString() {
        return "ProbabilitySamplingConfiguration{" +
                "strategy=" + getStrategy().toString() +
                '}';
    }
}
