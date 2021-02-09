package pictures;

import org.jetbrains.annotations.NotNull;
import pictures.coloring.AbstractColouring;

import java.awt.*;

public class MatrixDrawer {
    protected final int myXOffsetLeft;
    protected final int myYOffsetUp;
    protected final int myPlotWidth;
    protected final int myPlotHeight;
    protected final @NotNull Graphics myGraphics;
    protected final AbstractColouring myColoring;
    protected final int myOneWidth;
    protected final int myOneHeight;
    protected final Stroke myLineStroke;
    private final int numCols;
    private final int numRows;

    public MatrixDrawer(int xOffsetLeft, int yOffsetUp, int plotWidth, int plotHeight, @NotNull Graphics graphics,
                        AbstractColouring coloring, int numCols, int numRows) {
        this.myXOffsetLeft = xOffsetLeft;
        this.myYOffsetUp = yOffsetUp;
        this.myPlotWidth = plotWidth;
        this.myPlotHeight = plotHeight;
        this.myGraphics = graphics;
        this.myColoring = coloring;
        this.numCols = numCols;
        this.myOneWidth = myPlotWidth / this.numCols;
        this.numRows = numRows;
        this.myOneHeight = myPlotHeight / this.numRows;
        this.myLineStroke = new BasicStroke(1f);
    }

    protected void drawRect(int matrixRowInd, int matrixColInd, int rgdColor) {
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

    protected void drawSegment(int matrixRowIndBegin, int matrixColIndBegin,
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
