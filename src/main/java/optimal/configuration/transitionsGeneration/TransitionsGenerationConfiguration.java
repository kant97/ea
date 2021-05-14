package optimal.configuration.transitionsGeneration;

import optimal.configuration.ConfigurationVisitor;
import optimal.configuration.ValidatableConfiguration;
import optimal.configuration.VisitableConfiguration;
import optimal.configuration.probability.ProbabilitySamplingConfiguration;
import optimal.configuration.runs.StopConditionConfiguration;
import org.jetbrains.annotations.NotNull;

import javax.naming.ConfigurationException;
import java.util.Objects;

public abstract class TransitionsGenerationConfiguration implements ValidatableConfiguration, VisitableConfiguration {
    public enum TransitionsGenerationStrategy {
        PRECOMPUTED_TRANSITIONS_READING,
        RUN_TIME_TRANSITIONS_GENERATION
    }

    private final TransitionsGenerationStrategy strategy;
    private final ProbabilitySamplingConfiguration probabilityEnumeration;
    private final StopConditionConfiguration stopConditionConfig;

    public TransitionsGenerationConfiguration(@NotNull TransitionsGenerationStrategy strategy, @NotNull ProbabilitySamplingConfiguration probabilityEnumeration, @NotNull StopConditionConfiguration stopConditionConfig) {
        this.strategy = strategy;
        this.probabilityEnumeration = probabilityEnumeration;
        this.stopConditionConfig = stopConditionConfig;
    }

    @Override
    public void validate() throws ConfigurationException {
        probabilityEnumeration.validate();
        stopConditionConfig.validate();
    }

    @Override
    public @NotNull String accept(@NotNull ConfigurationVisitor visitor) {
        return visitor.visitTransitionsGenerationConfiguration(this);
    }

    @Override
    public String toString() {
        return "TransitionsGenerationConfiguration{" +
                "strategy=" + strategy +
                ", probabilityEnumeration=" + probabilityEnumeration +
                ", stopConditionConfig=" + stopConditionConfig +
                '}';
    }

    public TransitionsGenerationStrategy getStrategy() {
        return strategy;
    }

    public ProbabilitySamplingConfiguration getProbabilityEnumeration() {
        return probabilityEnumeration;
    }

    public StopConditionConfiguration getStopConditionConfig() {
        return stopConditionConfig;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final TransitionsGenerationConfiguration that = (TransitionsGenerationConfiguration) o;
        return strategy == that.strategy && probabilityEnumeration.equals(that.probabilityEnumeration) && stopConditionConfig.equals(that.stopConditionConfig);
    }

    @Override
    public int hashCode() {
        return Objects.hash(strategy, probabilityEnumeration, stopConditionConfig);
    }
}
