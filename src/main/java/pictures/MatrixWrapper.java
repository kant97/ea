package pictures;

import optimal.configuration.probability.ProbabilitySamplingConfiguration;
import optimal.probabilitySampling.ProbabilitySearcher;
import org.ejml.simple.SimpleMatrix;
import org.jetbrains.annotations.NotNull;

public class MatrixWrapper {
    @NotNull
    private final SimpleMatrix matrix;
    @NotNull
    private final ProbabilitySamplingConfiguration probabilityConfiguration;

    public MatrixWrapper(@NotNull SimpleMatrix matrix,
                          @NotNull ProbabilitySamplingConfiguration probabilityConfiguration) {
        this.matrix = matrix;
        this.probabilityConfiguration = probabilityConfiguration;
    }


    @NotNull
    public SimpleMatrix getMatrix() {
        return matrix;
    }

    public int getColumn(int fitnessDistance) {
        if (fitnessDistance <= 0) {
            throw new IllegalArgumentException("Fitness distance is supposed to be at least one, but it is " + fitnessDistance);
        }
        return fitnessDistance - 1;
    }

    public int getRow(double mutationRate) {
        final ProbabilitySearcher probabilitySearcher =
                ProbabilitySearcher.createProbabilitySearcher(probabilityConfiguration);
        if (mutationRate < probabilitySearcher.getInitialProbability()) {
            return 0;
        }
        if (mutationRate >= probabilitySearcher.getProbabilityOnStepN(matrix.numRows())) {
            return matrix.numRows();
        }
        int leftK = 0;
        int rightK = matrix.numRows();
        while (rightK - leftK > 1) {
            final int m = (leftK + rightK) / 2;
            final double curBoxMutationRate = probabilitySearcher.getProbabilityOnStepN(m);
            if (curBoxMutationRate <= mutationRate) {
                leftK = m;
            } else {
                rightK = m;
            }
        }
        final double curBoxMutationRate = probabilitySearcher.getProbabilityOnStepN(leftK);
        final double nextBoxMutationRate = probabilitySearcher.getProbabilityOnStepN(leftK + 1);
        if (!(curBoxMutationRate <= mutationRate) || !(mutationRate < nextBoxMutationRate)) {
            throw new IllegalStateException("Failed to correctly calculate box for mutation rate " + mutationRate);
        }
        return leftK;
    }

    public double getExpectedTime(int fitnessDistance, double mutationRate) {
        return getExpectedTime(getRow(mutationRate), getColumn(fitnessDistance));
    }

    public double getExpectedTime(int row, int col) {
        return matrix.get(row, col);
    }

    public double getMinMutationRate() {
        return ProbabilitySearcher.createProbabilitySearcher(probabilityConfiguration).getInitialProbability();
    }

    public int getMaxFitnessDist() {
        return matrix.numCols();
    }
}
