package optimal.configuration.runs;

import org.jetbrains.annotations.NotNull;

import javax.naming.ConfigurationException;
import java.util.Objects;

public class FixedRunsConfiguration extends StopConditionConfiguration {
    private final int amountOfRuns;

    public FixedRunsConfiguration(int amountOfRuns) {
        this.amountOfRuns = amountOfRuns;
    }

    public int getAmountOfRuns() {
        return amountOfRuns;
    }

    @Override
    public @NotNull Strategy getMyStrategy() {
        return Strategy.FIXED_RUNS;
    }

    @Override
    public String toString() {
        return "FixedRunsConfiguration{" +
                "amountOfRuns=" + amountOfRuns +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FixedRunsConfiguration that = (FixedRunsConfiguration) o;
        return amountOfRuns == that.amountOfRuns;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amountOfRuns);
    }

    @Override
    public void validate() throws ConfigurationException {
        if (amountOfRuns < 0) {
            throw new ConfigurationException("amount of runs amount should be greater than 0, but it is " + amountOfRuns);
        }
    }
}
