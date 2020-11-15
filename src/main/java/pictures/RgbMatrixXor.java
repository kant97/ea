package pictures;

import org.ejml.simple.SimpleMatrix;
import org.jetbrains.annotations.NotNull;
import pictures.coloring.AbstractColouring;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class RgbMatrixXor {

    @NotNull
    public SimpleMatrix getRgbMatrix(Path csvFile) {
        final SimpleMatrix matrixWithTimes = getMatrix(csvFile);
        final AbstractColouring coloring = AbstractColouring.createColoring(matrixWithTimes,
                AbstractColouring.ColoringStrategy.MODIFIED);
        final SimpleMatrix matrixWithRgb = new SimpleMatrix(matrixWithTimes);

        for (int i = 0; i < matrixWithTimes.numRows(); i++) {
            for (int j = 0; j < matrixWithTimes.numCols(); j++) {
                matrixWithRgb.set(i, j, coloring.getRgdColor(i, j));
            }
        }
        return matrixWithRgb;
    }

    public static void main(String[] args) {
//        final RgbMatrixXor rgbMatrixXor = new RgbMatrixXor();
        final SimpleMatrix m1 = getMatrix(Paths.get("tmp/A1.csv"));
        final SimpleMatrix m2 = getMatrix(Paths.get("tmp/A2.csv"));
        final SimpleMatrix m3 = new SimpleMatrix(m2);
        for (int i = 0; i < m1.numRows(); i++) {
            for (int j = 0; j < m1.numCols(); j++) {
                final double v1 = m1.get(i, j);
                final double v2 = m2.get(i, j);
                m3.set(i, j, v1 / v2);
            }
        }
//        final ColorfulPicturePainter colorfulPicturePainter = new ColorfulPicturePainter(null, Paths.get("xorImage" +
//                ".png"), AbstractColouring.ColoringStrategy.MODIFIED);
//        colorfulPicturePainter.doDrawHeatMap(m3);
        printMatrix(m3);
    }

    private static void printMatrix(SimpleMatrix m) {
        ArrayList<ArrayList<String>> table = new ArrayList<>();
        ArrayList<String> headers = new ArrayList<>();
        for (int i = 1; i <= m.numCols(); i++) {
            headers.add(String.valueOf(i));
        }
        Path csvFile = Paths.get("tmp/A1.csv");
        final SimpleMatrix m1 = getMatrix(csvFile);
        for (int i = m.numRows() - 1; i >= 0; i--) {
            final ArrayList<String> tmp = new ArrayList<>();
            for (int j = 0; j < m.numCols(); j++) {
                final String s = String.valueOf(m.get(i, j));
                tmp.add(s + ", " + m1.get(i, j));
            }
            table.add(tmp);
        }
        new ConsoleTable(headers, table).printTable();
    }

    private static SimpleMatrix getMatrix(Path csvFile) {
        final MatrixDataProcessor matrixDataProcessor = new MatrixDataProcessor(csvFile.toAbsolutePath().toString()
                , 9, 8, 10);
        matrixDataProcessor.loadData();
        return matrixDataProcessor.getProcessedData();
    }
}
