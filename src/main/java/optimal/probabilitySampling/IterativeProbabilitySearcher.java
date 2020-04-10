package optimal.probabilitySampling;

import optimal.configuration.probability.IterativeProbabilityConfiguration;

class IterativeProbabilitySearcher extends ProbabilitySearcher {
    protected IterativeProbabilitySearcher(IterativeProbabilityConfiguration configuration) {
        super(configuration.minMutationProbability, configuration.maxMutationProbability,
                configuration.precisionForProbability);
    }

    @Override
    public double getNextProb(double feedback) {
        myLastReturnedPrecision = myLastReturnedPrecision + myPrecision;
        return myLastReturnedPrecision;
    }

    @Override
    public double getInitialProbability() {
        return myLeftProb;
    }
}
