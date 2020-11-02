package optimal.configuration.runs;

import org.jetbrains.annotations.NotNull;

import javax.naming.ConfigurationException;
import java.util.Objects;

public class FixedSuccessConfiguration extends StopConditionConfiguration {
    private final int amountOfSuccess;
    private final int globalMaximumRuns;

    public FixedSuccessConfiguration(int amountOfSuccess, int globalMaximumRuns) {
        this.amountOfSuccess = amountOfSuccess;
        this.globalMaximumRuns = globalMaximumRuns;
    }

    public int getAmountOfSuccess() {
        return amountOfSuccess;
    }

    public int getGlobalMaximumRuns() {
        return globalMaximumRuns;
    }

    @Override
    public @NotNull Strategy getMyStrategy() {
        return Strategy.FIXED_SUCCESS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FixedSuccessConfiguration that = (FixedSuccessConfiguration) o;
        return amountOfSuccess == that.amountOfSuccess &&
                globalMaximumRuns == that.globalMaximumRuns;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amountOfSuccess, globalMaximumRuns);
    }

    @Override
    public String toString() {
        return "FixedSuccessConfiguration{" +
                "amountOfSuccess=" + amountOfSuccess +
                ", globalMaximumRuns=" + globalMaximumRuns +
                '}';
    }

    @Override
    public void validate() throws ConfigurationException {
        if (amountOfSuccess < 0) {
            throw new ConfigurationException("Amount of success amount should not be smaller than 0, but it is " + amountOfSuccess);
        }
        if (globalMaximumRuns < 0) {
            throw new ConfigurationException("Global amount of runs should not be smaller than 0, but it is " + globalMaximumRuns);
        }
        if (amountOfSuccess > globalMaximumRuns) {
            throw new ConfigurationException("Amount of success should not exceed global maximum runs amount, but it " +
                    "does: amountOfSuccess=" + amountOfSuccess + " globalMaximRuns=" + globalMaximumRuns);
        }
    }
}
