package optimal.probabilitySampling;

import optimal.configuration.probability.ExponentialGridConfiguration;
import optimal.configuration.probability.IterativeProbabilityConfiguration;
import optimal.configuration.probability.ProbabilitySamplingConfiguration;
import org.jetbrains.annotations.NotNull;

public abstract class ProbabilitySearcher {
    protected final double myLeftProb;
    protected final double myRightProb;
    protected final double myPrecision;
    protected double myLastReturnedPrecision;

    protected ProbabilitySearcher(double leftProb, double rightProb, double precision) {
        myLeftProb = leftProb;
        myRightProb = rightProb;
        myPrecision = precision;
        myLastReturnedPrecision = leftProb;
    }

    @NotNull
    public static ProbabilitySearcher createProbabilitySearcher(@NotNull ProbabilitySamplingConfiguration probabilitySamplingConfiguration) {
        ProbabilitySamplingStrategy strategy = probabilitySamplingConfiguration.getStrategy();
        if (strategy == ProbabilitySamplingStrategy.ITERATIVE) {
            return new IterativeProbabilitySearcher((IterativeProbabilityConfiguration) probabilitySamplingConfiguration);
        } else if (strategy == ProbabilitySamplingStrategy.TERNARY_SEARCH) {
            throw new IllegalStateException("Ternary search is not implemented yet");
        } else if (strategy == ProbabilitySamplingStrategy.EXPONENTIAL_GRID) {
            return new ExponentialGridProbabilitySearcher((ExponentialGridConfiguration) probabilitySamplingConfiguration);
        }
        throw new IllegalArgumentException("Unknown strategy");
    }

    // if feedback > 0 then the previously returned probability improved something
    public abstract double getNextProb(double feedback);

    public abstract double getInitialProbability();

    // if n = 0 then the initial probability is returned
    public abstract double getProbabilityOnStepN(int n);

    public boolean isFinished() {
        double difference = myLastReturnedPrecision - myRightProb;
        return difference > myPrecision / 2.;
    }
}
