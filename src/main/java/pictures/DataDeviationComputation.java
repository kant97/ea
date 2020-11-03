package pictures;

import org.ejml.simple.SimpleMatrix;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class DataDeviationComputation {
    private double computeDevian(SimpleMatrix t) {
        double ans = 0.;
        for (int j = 0; j < t.numCols(); j++) {
            final ArrayList<Double> tmp = new ArrayList<>();
            for (int i = 0; i < t.numRows(); i++) {
                tmp.add(t.get(i, j));
            }
            final double tBest = Collections.min(tmp);
            for (int i = 0; i < t.numRows(); i++) {
                final double tji = t.get(i, j);
                ans += getDeviation(tBest, tji);
            }
        }
        return ans;
    }

    private double getDeviation(double tBest, double tji) {
        return tji / Math.exp(tji - tBest);
    }

    public double computeDeviation(SimpleMatrix t1, SimpleMatrix t2) {
        return computeDevian(t1) - computeDevian(t2);
    }

    public void drawHeatMap(SimpleMatrix t1, SimpleMatrix t2) throws IOException {
        final SimpleMatrix t3 = new SimpleMatrix(t1.numRows(), t2.numCols());
        for (int j = 0; j < t1.numCols(); j++) {
            final ArrayList<Double> doubles1 = new ArrayList<>();
            final ArrayList<Double> doubles2 = new ArrayList<>();
            for (int i = 0; i < t1.numRows(); i++) {
                doubles1.add(t1.get(i, j));
                doubles2.add(t2.get(i, j));
            }
            final Double t1Best = Collections.min(doubles1);
            final Double t2Best = Collections.min(doubles2);
            for (int i = 0; i < t1.numRows(); i++) {
//                t3.set(i, j, Math.abs((t1.get(i, j) - t1Best) - (t2.get(i, j) - t2Best)));
                t3.set(i, j, Math.abs((t1.get(i, j)) - (t2.get(i, j))));
            }
        }
        ColorfulPicturePainter.drawChart(t3);
    }

    public static void main(String[] args) throws IOException {
        final MatrixDataProcessor t1Processor = new MatrixDataProcessor("tmp/A1.csv", 9, 8, 10);
        t1Processor.loadData();
        final SimpleMatrix t1 = t1Processor.getProcessedData();
        final MatrixDataProcessor t2Processor = new MatrixDataProcessor("tmp/A2.csv", 9, 8, 10);
        t2Processor.loadData();
        final SimpleMatrix t2 = t2Processor.getProcessedData();
        final DataDeviationComputation dataDeviationComputation = new DataDeviationComputation();
        System.out.println(dataDeviationComputation.computeDeviation(t1, t2));
        dataDeviationComputation.drawHeatMap(t1, t2);
    }
}
