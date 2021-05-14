package optimal.probabilitySampling;

import org.jetbrains.annotations.NotNull;

public abstract class IncreasingIntegersBijectiveProbabilitySpace implements IntegerToProbabilityBijectiveMapping {
    private final int largestInteger;
    private final double myEpsilon;

    protected IncreasingIntegersBijectiveProbabilitySpace(@NotNull ProbabilitySpace space) {
        double prevP = 0.;
        double epsilon = Double.MAX_VALUE;
        int probabilityDistance = 0;
        for (double p = space.getInitialProbability(); !space.isFinished(); p = space.getNextProb()) {
            epsilon = Math.min(epsilon, Math.abs(prevP - p) / 4.);
            prevP = p;
            probabilityDistance++;
        }
        this.largestInteger = probabilityDistance;
        this.myEpsilon = epsilon;
    }

    private boolean isAInAreaOfB(double a, double b) {
        return Math.abs(a - b) < myEpsilon;
    }

    private boolean isALessOrInAreaOfB(double a, double b) {
        if (isAInAreaOfB(a, b)) {
            return true;
        }
        return a < b;
    }

    @Override
    public int probabilityToInteger(double probability) {
        int left = 0;
        int right = largestInteger + 1;
        while (right - left > 1) {
            int m = (left + right) / 2;
            if (isALessOrInAreaOfB(integerToProbability(m), probability)) {
                left = m;
            } else {
                right = m;
            }
        }
        return left;
    }

    @Override
    public abstract double integerToProbability(int n);
}
