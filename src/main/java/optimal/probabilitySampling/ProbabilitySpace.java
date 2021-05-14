package optimal.probabilitySampling;

import optimal.configuration.probability.ExponentialGridConfiguration;
import optimal.configuration.probability.IterativeProbabilityConfiguration;
import optimal.configuration.probability.ProbabilitySamplingConfiguration;
import org.jetbrains.annotations.NotNull;

public abstract class ProbabilitySpace {
    protected final double myLeftProb;
    protected final double myRightProb;
    protected final double myPrecision;
    protected double myLastReturnedPrecision;

    protected ProbabilitySpace(double leftProb, double rightProb, double precision) {
        myLeftProb = leftProb;
        myRightProb = rightProb;
        myPrecision = precision;
        myLastReturnedPrecision = leftProb;
    }

    @NotNull
    public static ProbabilitySpace createProbabilitySpace(@NotNull ProbabilitySamplingConfiguration probabilitySamplingConfiguration) {
        ProbabilitySamplingStrategy strategy = probabilitySamplingConfiguration.getStrategy();
        if (strategy == ProbabilitySamplingStrategy.ITERATIVE) {
            return new IterativeProbabilitySpace((IterativeProbabilityConfiguration) probabilitySamplingConfiguration);
        } else if (strategy == ProbabilitySamplingStrategy.TERNARY_SEARCH) {
            throw new IllegalStateException("Ternary search is not implemented yet");
        } else if (strategy == ProbabilitySamplingStrategy.EXPONENTIAL_GRID) {
            return new ExponentialGridProbabilitySpace((ExponentialGridConfiguration) probabilitySamplingConfiguration);
        }
        throw new IllegalArgumentException("Unknown strategy");
    }

    public abstract double getNextProb();

    public abstract double getInitialProbability();

    // if n = 0 then the initial probability is returned
    public abstract double getProbabilityOnStepN(int n);

    public abstract IntegerToProbabilityBijectiveMapping createBijectionToIntegers();

    public boolean isFinished() {
        double difference = myLastReturnedPrecision - myRightProb;
        return difference > myPrecision / 2.;
    }
}
