package pictures.coloring;

import optimal.heuristics.OneMaxHeuristics;
import org.ejml.simple.SimpleMatrix;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public abstract class AbstractColouring {
    protected final @NotNull SimpleMatrix myMatrix;
    protected final @NotNull ArrayList<ArrayList<Double>> myMatrixWithSortedColumns;

    protected AbstractColouring(@NotNull SimpleMatrix matrix) {
        myMatrix = matrix;
        myMatrixWithSortedColumns = calculateSortedColumns(matrix);
    }

    public enum ColoringStrategy {
        INITIAL, MODIFIED
    }

    public static AbstractColouring createColoring(@NotNull SimpleMatrix matrix, @NotNull ColoringStrategy strategy) {
        if (strategy == ColoringStrategy.INITIAL) {
            return new InitialColouring(matrix);
        } else if (strategy == ColoringStrategy.MODIFIED) {
            return new ModifiedColouring(matrix);
        }
        throw new IllegalArgumentException("Strategy " + strategy.name() + " is not supported.");
    }

    private ArrayList<ArrayList<Double>> calculateSortedColumns(@NotNull SimpleMatrix matrix) {
        final ArrayList<ArrayList<Double>> sortedColumns = new ArrayList<>();
        for (int c = 0; c < matrix.numCols(); c++) {
            final ArrayList<Double> column = new ArrayList<>();
            for (int r = 0; r < matrix.numRows(); r++) {
                column.add(matrix.get(r, c));
            }
            column.sort(null);
            sortedColumns.add(column);
        }
        return sortedColumns;
    }

    public int getRgdColor(int row, int col) {
        final double value01 = getValueForRgbColor(row, col);
        if (Double.isNaN(value01)) {
            throw new IllegalStateException("max value = " + myMatrixWithSortedColumns.get(col).get(0) + ", curr " +
                    "value = " + myMatrix.get(row, col));
        }
        return value01 > 1. ? 0xff0000 : Viridis$.MODULE$.apply(value01);
    }

    protected abstract double getValueForRgbColor(int row, int col);

    public abstract @NotNull ColoringStrategy getColoringStrategy();

    private static class InitialColouring extends AbstractColouring {
        protected InitialColouring(@NotNull SimpleMatrix matrix) {
            super(matrix);
        }

        @Override
        protected double getValueForRgbColor(int row, int col) {
            final double value = myMatrix.get(row, col);
            final double valueBest = myMatrixWithSortedColumns.get(col).get(0);
            return Math.exp(valueBest - value);
        }

        @Override
        public @NotNull ColoringStrategy getColoringStrategy() {
            return ColoringStrategy.INITIAL;
        }
    }

    private static class ModifiedColouring extends AbstractColouring {
        protected ModifiedColouring(@NotNull SimpleMatrix matrix) {
            super(matrix);
        }

        private int calculateAmountNotPurple(int column) {
            int k = 0;
            for (int r = 0; r < myMatrix.numRows(); r++) {
                if (!OneMaxHeuristics.isTooBig(myMatrix.get(r, column))) {
                    k++;
                }
            }
            return k;
        }

        @Override
        protected double getValueForRgbColor(int row, int col) {
            final double value = myMatrix.get(row, col);
            final int k = calculateAmountNotPurple(col);
            final double valueK = myMatrixWithSortedColumns.get(col).get(k - 1);
            final double value1 = myMatrixWithSortedColumns.get(col).get(0);
            final double m = Math.min(1, Math.log(0.5) / (value1 - valueK));
            return Math.exp(m * (value1 - value));
        }

        @Override
        public @NotNull ColoringStrategy getColoringStrategy() {
            return ColoringStrategy.MODIFIED;
        }
    }
}
