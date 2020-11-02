package optimal.probabilitySampling;

import optimal.configuration.probability.IterativeProbabilityConfiguration;

class IterativeProbabilitySearcher extends ProbabilitySearcher {
    protected IterativeProbabilitySearcher(IterativeProbabilityConfiguration configuration) {
        super(configuration.minMutationProbability, configuration.maxMutationProbability,
                configuration.precisionForProbability);
    }

    @Override
    public double getNextProb() {
        myLastReturnedPrecision = myLastReturnedPrecision + myPrecision;
        return myLastReturnedPrecision;
    }

    @Override
    public double getInitialProbability() {
        return myLeftProb;
    }

    @Override
    public double getProbabilityOnStepN(int n) {
        return myLeftProb + myPrecision * n;
    }
}
