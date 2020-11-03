package pictures.coloring;

import org.ejml.simple.SimpleMatrix;
import org.jetbrains.annotations.NotNull;
import pictures.Viridis$;

import java.util.ArrayList;

public abstract class AbstractColoring {
    protected final @NotNull SimpleMatrix myMatrix;
    protected final @NotNull ArrayList<ArrayList<Double>> myMatrixWithSortedColumns;

    protected AbstractColoring(@NotNull SimpleMatrix matrix) {
        myMatrix = matrix;
        myMatrixWithSortedColumns = calculateSortedColumns(matrix);
    }

    public enum ColoringStrategy {
        INITIAL, MODIFIED
    }

    public static AbstractColoring createColoring(@NotNull SimpleMatrix matrix, @NotNull ColoringStrategy strategy) {
        if (strategy == ColoringStrategy.INITIAL) {
            return new InitialColoring(matrix);
        } else if (strategy == ColoringStrategy.MODIFIED) {
            return new ModifiedColoring(matrix);
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
}
