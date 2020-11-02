package optimal.probabilitySampling;

import optimal.configuration.probability.ExponentialGridConfiguration;

public class ExponentialGridProbabilitySearcher extends ProbabilitySearcher {
    private final double a;

    protected ExponentialGridProbabilitySearcher(ExponentialGridConfiguration configuration) {
        super(configuration.minPowerValue, configuration.maxPowerValue, configuration.precisionForPower);
        a = configuration.base;
    }

    private double f(double power) {
        return Math.pow(a, power);
    }

    @Override
    public double getNextProb() {
        myLastReturnedPrecision += myPrecision;
        return f(myLastReturnedPrecision);
    }

    @Override
    public double getInitialProbability() {
        return f(myLeftProb);
    }

    @Override
    public double getProbabilityOnStepN(int n) {
        return f(myLeftProb + myPrecision * n);
    }
}
