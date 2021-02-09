package pictures.heatmap;

import optimal.probabilitySampling.ProbabilitySearcher;
import org.ejml.simple.SimpleMatrix;
import org.jetbrains.annotations.NotNull;

public class MatrixLine {
    private final @NotNull SimpleMatrix myRunTimes; /* myRunTimes[r][d] stores math expectation of
    generations amount to reach the global optima if starting from the distance d with the mutation rate r */
    private final @NotNull ProbabilitySearcher myProbabilitySampler;

    public MatrixLine(@NotNull SimpleMatrix myRunTimes, @NotNull ProbabilitySearcher myProbabilitySampler) {
        this.myRunTimes = myRunTimes;
        this.myProbabilitySampler = myProbabilitySampler;
    }

    protected int getMatrixColumnIndOfFitnessDistance(int fitnessDistance) {
        if (fitnessDistance <= 0) {
            throw new IllegalArgumentException("Fitness distance is supposed to be at least one, but it is " + fitnessDistance);
        }
        return fitnessDistance - 1;
    }

    protected int getMatrixRowIndOfMutationRate(double mutationRate) {
        if (mutationRate < myProbabilitySampler.getInitialProbability()) {
            return -1;
        }
        if (mutationRate >= myProbabilitySampler.getProbabilityOnStepN(myRunTimes.numRows())) {
            return myRunTimes.numRows();
        }
        int leftK = 0;
        int rightK = myRunTimes.numRows();
        while (rightK - leftK > 1) {
            final int m = (leftK + rightK) / 2;
            final double curBoxMutationRate = myProbabilitySampler.getProbabilityOnStepN(m);
            if (curBoxMutationRate <= mutationRate) {
                leftK = m;
            } else {
                rightK = m;
            }
        }
        final double curBoxMutationRate = myProbabilitySampler.getProbabilityOnStepN(leftK);
        final double nextBoxMutationRate = myProbabilitySampler.getProbabilityOnStepN(leftK + 1);
        if (!(curBoxMutationRate <= mutationRate) || !(mutationRate < nextBoxMutationRate)) {
            throw new IllegalStateException("Failed to correctly calculate box for mutation rate " + mutationRate);
        }
        return leftK;
    }
}