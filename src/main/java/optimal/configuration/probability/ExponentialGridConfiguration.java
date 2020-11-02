package optimal.configuration.probability;

import optimal.configuration.ConfigurationVisitor;
import optimal.configuration.VisitableConfiguration;
import optimal.probabilitySampling.ProbabilitySamplingStrategy;
import org.jetbrains.annotations.NotNull;

import javax.naming.ConfigurationException;
import java.util.Objects;

public class ExponentialGridConfiguration extends ProbabilitySamplingConfiguration {
    public final double base;
    public final double minPowerValue;
    public final double maxPowerValue;
    public final double precisionForPower;

    public ExponentialGridConfiguration(double base, double minPowerValue, double maxPowerValue,
                                        double precisionForPower) {
        this.base = base;
        this.minPowerValue = minPowerValue;
        this.maxPowerValue = maxPowerValue;
        this.precisionForPower = precisionForPower;
    }

    @NotNull
    @Override
    public ProbabilitySamplingStrategy getStrategy() {
        return ProbabilitySamplingStrategy.EXPONENTIAL_GRID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExponentialGridConfiguration that = (ExponentialGridConfiguration) o;
        return Double.compare(that.base, base) == 0 &&
                Double.compare(that.minPowerValue, minPowerValue) == 0 &&
                Double.compare(that.maxPowerValue, maxPowerValue) == 0 &&
                Double.compare(that.precisionForPower, precisionForPower) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(base, minPowerValue, maxPowerValue, precisionForPower);
    }

    @Override
    public String toString() {
        return "ExponentialGridConfiguration{" +
                "base=" + base +
                ", minPowerValue=" + minPowerValue +
                ", maxPowerValue=" + maxPowerValue +
                ", precisionForPower=" + precisionForPower +
                "} ";
    }

    @Override
    public void validate() throws ConfigurationException {
        if (base == 0. &&
                minPowerValue == 0. &&
                maxPowerValue == 0. &&
                precisionForPower == 0.) {
            throw new ConfigurationException("Fields are not initialized");
        }
        if (minPowerValue > maxPowerValue) {
            throw new ConfigurationException("Max mutation power should not be smaller than min, min = " + minPowerValue + " max = " + maxPowerValue);
        }
    }

    @Override
    public @NotNull String accept(@NotNull ConfigurationVisitor visitor) {
        return visitor.visitExponentialGridConfig(this);
    }

    public double getBase() {
        return base;
    }

    public double getMinPowerValue() {
        return minPowerValue;
    }

    public double getMaxPowerValue() {
        return maxPowerValue;
    }

    public double getPrecisionForPower() {
        return precisionForPower;
    }
}
