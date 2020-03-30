package optimal.probabilitySampling;

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
        myLastReturnedPrecision = leftProb - precision;
    }

    @NotNull
    public static ProbabilitySearcher createProbabilitySearcher(double leftProb, double rightProb, double precision,
                                                                ProbabilitySamplingStrategy strategy) {
        if (strategy.equals(ProbabilitySamplingStrategy.ITERATIVE)) {
            return new IterativeProbabilitySearcher(leftProb, rightProb, precision);
        } else if (strategy.equals(ProbabilitySamplingStrategy.TERNARY_SEARCH)) {
            throw new IllegalStateException("Ternary search is not implemented yet");
        }
        throw new IllegalArgumentException("Unknown strategy");
    }

    // if feedback > 0 then previous returned probability improved something
    public abstract double getNextProb(double feedback);

    public abstract double getInitialProbability();

    public abstract boolean isFinished();
}
