package pictures;

import optimal.heuristics.OneMaxHeuristics;
import optimal.probabilitySampling.ProbabilitySearcher;
import org.ejml.simple.SimpleMatrix;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ViridisPlotDrawer {
    private final int myXOffsetLeft;
    private final int myYOffsetUp;
    private final int myPlotWidth;
    private final int myPlotHeight;
    private final @NotNull Graphics myGraphics;
    private final @NotNull SimpleMatrix myRunTimes; /* myRunTimes[d, r] stores math expectation of
    generations amount to reach the global optima if starting from the distance d with the mutation rate r */
    private final int myOneWidth;
    private final int myOneHeight;
    private final @NotNull ArrayList<ArrayList<Double>> myRunTimesWithSortedColumns;
    private final @NotNull ProbabilitySearcher myProbabilitySampler;
    private final Stroke myLineStroke;

    public ViridisPlotDrawer(int xOffsetLeft, int yOffsetUp,
                             int plotWidth, int plotHeight, @NotNull Graphics graphics,
                             @NotNull SimpleMatrix data,
                             @NotNull ProbabilitySearcher probabilitySampler) {
        this.myXOffsetLeft = xOffsetLeft;
        this.myYOffsetUp = yOffsetUp;
        this.myPlotWidth = plotWidth;
        this.myPlotHeight = plotHeight;
        this.myGraphics = graphics;
        this.myRunTimes = data;
        this.myOneWidth = myPlotWidth / data.numCols();
        this.myOneHeight = myPlotHeight / data.numRows();
        this.myProbabilitySampler = probabilitySampler;
        this.myRunTimesWithSortedColumns = calculateSortedColumns();
        this.myLineStroke = new BasicStroke(1f);
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
        for (int i = 0; i < myRunTimes.numRows(); i++) {
            for (int j = 0; j < myRunTimes.numCols(); j++) {
                drawRect(i, j, getRgdColor(i, j));
            }
        }
    }

    public void addChart(@NotNull Map<Integer, Double> optimaDistanceToMutationRate, @NotNull Color color) {
        final int colorRGB = color.getRGB();
        final List<Integer> sortedFitness =
                optimaDistanceToMutationRate.keySet().stream().sorted().collect(Collectors.toList());
        int prevFitness = sortedFitness.get(0);
        for (int i = 1; i < sortedFitness.size(); i++) {
            final int curFitness = sortedFitness.get(i);
            final double prevMutationRate = optimaDistanceToMutationRate.get(prevFitness);
            final double curMutationRate = optimaDistanceToMutationRate.get(curFitness);
            final int matrixColumnBegin = getMatrixColumnIndOfFitnessDistance(prevFitness);
            final int matrixRowBegin = getMatrixRowIndOfMutationRate(prevMutationRate);
            final int matrixColumnEnd = getMatrixColumnIndOfFitnessDistance(curFitness);
            final int matrixRowEnd = getMatrixRowIndOfMutationRate(curMutationRate);
            drawSegment(matrixRowBegin, matrixColumnBegin, matrixRowEnd, matrixColumnEnd, colorRGB);
            prevFitness = curFitness;
        }
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

    private int getRgdColor(int row, int col) {
        final double value01 = getValueForRgbColorModification(row, col);
//        final double value01 = getValueForRgbColorInitial(row, col);
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
        final int x = getXCoordinateByMatrixColumnIndex(matrixColInd);
        final int y = getYCoordinateByMatrixRowIndex(matrixRowInd);
        myGraphics.setColor(new Color(rgdColor));
        myGraphics.fillRect(x, y, myOneWidth, myOneHeight);
    }

    private int getYCoordinateByMatrixRowIndex(int matrixRowInd) {
        return myYOffsetUp + myPlotHeight - matrixRowInd * myOneHeight - myOneHeight;
    }

    private int getXCoordinateByMatrixColumnIndex(int matrixColInd) {
        return myXOffsetLeft + matrixColInd * myOneWidth;
    }

    private void drawCenteredCircle(int x, int y, int d) {
        x = x - (d / 2);
        y = y - (d / 2);
        myGraphics.fillOval(x, y, d, d);
    }

    private int getXCenterOfSquare(int matrixColInd) {
        return getXCoordinateByMatrixColumnIndex(matrixColInd) + myOneWidth / 2;
    }

    private int getYCenterOfSquare(int matrixRowInd) {
        return getYCoordinateByMatrixRowIndex(matrixRowInd) + myOneWidth / 2;
    }

    private void drawSegment(int matrixRowIndBegin, int matrixColIndBegin,
                             int matrixRowIndEnd, int matrixColIndEnd,
                             int rgbColor) {
        final int xBegin = getXCenterOfSquare(matrixColIndBegin);
        final int yBegin = getYCenterOfSquare(matrixRowIndBegin);
        final int xEnd = getXCenterOfSquare(matrixColIndEnd);
        final int yEnd = getYCenterOfSquare(matrixRowIndEnd);
        myGraphics.setColor(new Color(rgbColor));
        final Graphics2D graphics = (Graphics2D) myGraphics;
        final Stroke defaultStroke = graphics.getStroke();
        graphics.setStroke(myLineStroke);
        myGraphics.drawLine(xBegin, yBegin, xEnd, yEnd);
        graphics.setStroke(defaultStroke);
        final int minL = Math.min(myOneHeight, myOneWidth);
        final int d = 4 * minL / 8;
        drawCenteredCircle(xBegin, yBegin, d);
        drawCenteredCircle(xEnd, yEnd, d);
    }
}
