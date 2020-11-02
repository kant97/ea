package optimal.configuration.vectorGeneration;

import optimal.configuration.runs.StopConditionConfiguration;
import org.jetbrains.annotations.NotNull;

import javax.naming.ConfigurationException;
import java.util.Objects;

public class RunTimeGenerationConfiguration extends VectorGenerationConfiguration {
    private final StopConditionConfiguration stopConditionConfig;

    public RunTimeGenerationConfiguration(StopConditionConfiguration stopConditionConfiguration) {
        this.stopConditionConfig = stopConditionConfiguration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RunTimeGenerationConfiguration that = (RunTimeGenerationConfiguration) o;
        return stopConditionConfig.equals(that.stopConditionConfig);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stopConditionConfig);
    }

    @Override
    public @NotNull VectorGenerationStrategy getStrategy() {
        return VectorGenerationStrategy.RUN_TIME_VECTOR_GENERATION;
    }

    public StopConditionConfiguration getStopConditionConfiguration() {
        return stopConditionConfig;
    }

    @Override
    public void validate() throws ConfigurationException {
        stopConditionConfig.validate();
    }
}
