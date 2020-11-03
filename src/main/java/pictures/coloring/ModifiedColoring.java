package pictures.coloring;

import optimal.heuristics.OneMaxHeuristics;
import org.ejml.simple.SimpleMatrix;
import org.jetbrains.annotations.NotNull;

public class ModifiedColoring extends AbstractColoring {
    protected ModifiedColoring(@NotNull SimpleMatrix matrix) {
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
}
