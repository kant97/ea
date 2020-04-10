package optimal.configuration.probability;

import optimal.probabilitySampling.ProbabilitySamplingStrategy;
import org.jetbrains.annotations.NotNull;

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
    public String toString() {
        return "ExponentialGridConfiguration{" +
                "base=" + base +
                ", minPowerValue=" + minPowerValue +
                ", maxPowerValue=" + maxPowerValue +
                ", precisionForPower=" + precisionForPower +
                ", strategy=" + getStrategy().toString() +
                '}';
    }
}
