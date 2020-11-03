package pictures.coloring;

import org.ejml.simple.SimpleMatrix;
import org.jetbrains.annotations.NotNull;

public class InitialColoring extends AbstractColoring {
    protected InitialColoring(@NotNull SimpleMatrix matrix) {
        super(matrix);
    }

    @Override
    protected double getValueForRgbColor(int row, int col) {
        final double value = myMatrix.get(row, col);
        final double valueBest = myMatrixWithSortedColumns.get(col).get(0);
        return Math.exp(valueBest - value);
    }
}
