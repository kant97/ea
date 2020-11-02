package optimal.configuration.probability;

import optimal.configuration.ValidatableConfiguration;
import optimal.probabilitySampling.ProbabilitySamplingStrategy;
import org.jetbrains.annotations.NotNull;

public abstract class ProbabilitySamplingConfiguration implements ValidatableConfiguration {
    public abstract @NotNull ProbabilitySamplingStrategy getStrategy();

    @Override
    public String toString() {
        return "ProbabilitySamplingConfiguration{" +
                "strategy=" + getStrategy().toString() +
                '}';
    }
}
