package optimal.configuration;


import org.jetbrains.annotations.NotNull;

import javax.naming.ConfigurationException;
import java.util.List;
import java.util.Objects;

public class UsualConfiguration implements ValidatableConfiguration, VisitableConfiguration {
    public final int amountOfThreads;
    public final List<OneExperimentConfiguration> experimentConfigurations;

    public UsualConfiguration(int amountOfThreads, List<OneExperimentConfiguration> experimentConfigurations) {
        this.amountOfThreads = amountOfThreads;
        this.experimentConfigurations = experimentConfigurations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsualConfiguration that = (UsualConfiguration) o;
        return amountOfThreads == that.amountOfThreads &&
                experimentConfigurations.equals(that.experimentConfigurations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amountOfThreads, experimentConfigurations);
    }

    @Override
    public void validate() throws ConfigurationException {
        if (amountOfThreads <= 0) {
            throw new ConfigurationException("Only positive amount of threads is possible");
        }
        for (OneExperimentConfiguration configuration : experimentConfigurations) {
            configuration.validate();
        }
    }

    @Override
    public @NotNull String accept(@NotNull ConfigurationVisitor visitor) {
        return visitor.visitUsualConfiguration(this);
    }
}
