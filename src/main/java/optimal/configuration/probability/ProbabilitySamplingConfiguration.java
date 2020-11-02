package optimal.configuration.probability;

import optimal.configuration.ValidatableConfiguration;
import optimal.configuration.VisitableConfiguration;
import optimal.probabilitySampling.ProbabilitySamplingStrategy;
import org.jetbrains.annotations.NotNull;

public abstract class ProbabilitySamplingConfiguration implements ValidatableConfiguration, VisitableConfiguration {
    public abstract @NotNull ProbabilitySamplingStrategy getStrategy();

    @Override
    public String toString() {
        return "ProbabilitySamplingConfiguration{" +
                "strategy=" + getStrategy().toString() +
                '}';
    }
}
