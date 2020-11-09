package pictures;

import org.ejml.simple.SimpleMatrix;
import org.jetbrains.annotations.NotNull;
import pictures.coloring.AbstractColouring;

import java.awt.*;
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
    private final AbstractColouring myColoring;
    private final int myOneWidth;
    private final int myOneHeight;
    private final Stroke myLineStroke;

    @NotNull
    public SimpleMatrix getMyRunTimes() {
        return myRunTimes;
    }

    public ViridisPlotDrawer(int xOffsetLeft, int yOffsetUp,
                             int plotWidth, int plotHeight, @NotNull Graphics graphics,
                             @NotNull SimpleMatrix data,
                             AbstractColouring coloring) {
        this.myXOffsetLeft = xOffsetLeft;
        this.myYOffsetUp = yOffsetUp;
        this.myPlotWidth = plotWidth;
        this.myPlotHeight = plotHeight;
        this.myGraphics = graphics;
        this.myRunTimes = data;
        this.myOneWidth = myPlotWidth / data.numCols();
        this.myOneHeight = myPlotHeight / data.numRows();
        this.myColoring = coloring;
        this.myLineStroke = new BasicStroke(1f);
    }

    public void drawViridisPlot() {
        for (int i = 0; i < myRunTimes.numRows(); i++) {
            for (int j = 0; j < myRunTimes.numCols(); j++) {
                drawRect(i, j, myColoring.getRgdColor(i, j));
            }
        }
    }

    public SimpleMatrix getMatrixWithRbgColors() {
        final SimpleMatrix rgbMatrix = new SimpleMatrix(myRunTimes);
        for (int i = 0; i < myRunTimes.numRows(); i++) {
            for (int j = 0; j < myRunTimes.numCols(); j++) {
                rgbMatrix.set(i, j, myColoring.getRgdColor(i, j));
            }
        }
        return rgbMatrix;
    }

    public void addChart(@NotNull Map<Integer, Double> optimaDistanceToMutationRate,
                         @NotNull Color color,
                         @NotNull MatrixLine matrixLine) {
        final int colorRGB = color.getRGB();
        final List<Integer> sortedFitness =
                optimaDistanceToMutationRate.keySet().stream().sorted().collect(Collectors.toList());
        int prevFitness = sortedFitness.get(0);
        for (int i = 1; i < sortedFitness.size(); i++) {
            final int curFitness = sortedFitness.get(i);
            final double prevMutationRate = optimaDistanceToMutationRate.get(prevFitness);
            final double curMutationRate = optimaDistanceToMutationRate.get(curFitness);
            final int matrixColumnBegin = matrixLine.getMatrixColumnIndOfFitnessDistance(prevFitness);
            final int matrixRowBegin = matrixLine.getMatrixRowIndOfMutationRate(prevMutationRate);
            final int matrixColumnEnd = matrixLine.getMatrixColumnIndOfFitnessDistance(curFitness);
            final int matrixRowEnd = matrixLine.getMatrixRowIndOfMutationRate(curMutationRate);
            drawSegment(matrixRowBegin, matrixColumnBegin, matrixRowEnd, matrixColumnEnd, colorRGB);
            prevFitness = curFitness;
        }
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
