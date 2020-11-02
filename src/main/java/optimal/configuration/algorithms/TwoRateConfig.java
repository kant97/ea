package optimal.configuration.algorithms;

import optimal.oneStepAlgorithms.OneStepAlgorithmsManager;

import java.util.Objects;

public class TwoRateConfig extends AlgorithmConfig {
    private final double lowerBound;

    public TwoRateConfig(OneStepAlgorithmsManager.AlgorithmType type, int lambda,
                         double lowerBound) {
        super(type, lambda);
        this.lowerBound = lowerBound;
    }

    public double getLowerBound() {
        return lowerBound;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TwoRateConfig that = (TwoRateConfig) o;
        return Double.compare(that.lowerBound, lowerBound) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), lowerBound);
    }

    @Override
    public String toString() {
        return "TwoRateConfig{" +
                "lowerBound=" + lowerBound +
                "} " + super.toString();
    }
}
