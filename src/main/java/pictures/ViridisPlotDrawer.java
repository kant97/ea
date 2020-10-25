package pictures;

import optimal.heuristics.OneMaxHeuristics;
import org.ejml.simple.SimpleMatrix;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class ViridisPlotDrawer {
    private final int myXOffsetLeft;
    private final int myYOffsetUp;
    private final int myPlotWidth;
    private final int myPlotHeight;
    private final @NotNull Graphics myGraphics;
    private final @NotNull SimpleMatrix myRunTimes;
    private final int myOneWidth;
    private final int myOneHeight;
    //    private final ArrayList<Double> myBestInColumn;
    private final ArrayList<ArrayList<Double>> myRunTimesWithSortedColumns;

    public ViridisPlotDrawer(int xOffsetLeft, int yOffsetUp,
                             int plotWidth, int plotHeight, @NotNull Graphics graphics,
                             @NotNull SimpleMatrix data) {
        this.myXOffsetLeft = xOffsetLeft;
        this.myYOffsetUp = yOffsetUp;
        this.myPlotWidth = plotWidth;
        this.myPlotHeight = plotHeight;
        this.myGraphics = graphics;
        this.myRunTimes = data;
        this.myOneWidth = myPlotWidth / data.numCols();
        this.myOneHeight = myPlotHeight / data.numRows();
//        this.myBestInColumn = calculateMaxInColumns();
        this.myRunTimesWithSortedColumns = calculateSortedColumns();
    }

    private ArrayList<ArrayList<Double>> calculateSortedColumns() {
        final ArrayList<ArrayList<Double>> sortedColumns = new ArrayList<>();
        for (int c = 0; c < myRunTimes.numCols(); c++) {
            final ArrayList<Double> column = new ArrayList<>();
            for (int r = 0; r < myRunTimes.numRows(); r++) {
                column.add(myRunTimes.get(r, c));
            }
            column.sort(null);
            sortedColumns.add(column);
        }
        return sortedColumns;
    }

    public void drawViridisPlot() {
//        calculateMaxInColumns();
        for (int i = 0; i < myRunTimes.numRows(); i++) {
            for (int j = 0; j < myRunTimes.numCols(); j++) {
                drawRect(i, j, getRgdColor(i, j));
            }
        }
    }

    private ArrayList<Double> calculateMaxInColumns() {
        final ArrayList<Double> bestInColumn = new ArrayList<>();
        for (int c = 0; c < myRunTimes.numCols(); c++) {
            final ArrayList<Double> tmp = new ArrayList<>();
            for (int r = 0; r < myRunTimes.numRows(); r++) {
                tmp.add(myRunTimes.get(r, c));
            }
            bestInColumn.add(Collections.min(tmp));
        }
        return bestInColumn;
    }

    private int getRgdColor(int row, int col) {
//        final double value01 = getValueForRgbColorModification(row, col);
        final double value01 = getValueForRgbColorInitial(row, col);
        if (Double.isNaN(value01)) {
            throw new IllegalStateException("max value = " + myRunTimesWithSortedColumns.get(col).get(0) + ", curr " +
                    "value = " + myRunTimes.get(row, col));
        }
        return value01 > 1. ? 0xff0000 : Viridis$.MODULE$.apply(value01);
    }

    private double getValueForRgbColorModification(int row, int col) {
        final double value = myRunTimes.get(row, col);
        final int k = calculateAmountNotPurple(col);
        final double valueK = myRunTimesWithSortedColumns.get(col).get(k - 1);
        final double value1 = myRunTimesWithSortedColumns.get(col).get(0);
        final double m = Math.min(1, Math.log(0.5) / (value1 - valueK));
        return Math.exp(m * (value1 - value));
    }

    private double getValueForRgbColorInitial(int row, int col) {
        final double value = myRunTimes.get(row, col);
        final double valueBest = myRunTimesWithSortedColumns.get(col).get(0);
        return Math.exp(valueBest - value);
    }

    private int calculateAmountNotPurple(int column) {
        int k = 0;
        for (int r = 0; r < myRunTimes.numRows(); r++) {
            if (!OneMaxHeuristics.isTooBig(myRunTimes.get(r, column))) {
                k++;
            }
        }
        return k;
    }

    private void drawRect(int matrixRowInd, int matrixColInd, int rgdColor) {
        final int x = myXOffsetLeft + matrixColInd * myOneWidth;
        final int y = myYOffsetUp + myPlotHeight - matrixRowInd * myOneHeight - myOneHeight;
        myGraphics.setColor(new Color(rgdColor));
//        myGraphics.drawRect(x, y, myOneWidth, myOneHeight);
        myGraphics.fillRect(x, y, myOneWidth, myOneHeight);
    }

}
