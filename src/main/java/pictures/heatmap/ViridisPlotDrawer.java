package pictures.heatmap;

import org.ejml.simple.SimpleMatrix;
import org.jetbrains.annotations.NotNull;
import pictures.MatrixCellCoordinate;
import pictures.MatrixDrawer;
import pictures.PlottableInMatrixData;
import pictures.coloring.AbstractColouring;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ViridisPlotDrawer extends MatrixDrawer {
    private final @NotNull SimpleMatrix myRunTimes; /* myRunTimes[r][d] stores math expectation of
    generations amount to reach the global optima if starting from the distance d with the mutation rate r */

    @NotNull
    public SimpleMatrix getMyRunTimes() {
        return myRunTimes;
    }

    public ViridisPlotDrawer(int xOffsetLeft, int yOffsetUp,
                             int plotWidth, int plotHeight, @NotNull Graphics graphics,
                             @NotNull SimpleMatrix data,
                             AbstractColouring coloring) {
        super(xOffsetLeft, yOffsetUp, plotWidth, plotHeight, graphics, coloring, data.numCols(), data.numRows());
        this.myRunTimes = data;
    }

    public void drawViridisHeatmap() {
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

    public void addChart(@NotNull PlottableInMatrixData data, @NotNull Color color) {
        final int colorRGB = color.getRGB();
        final List<MatrixCellCoordinate> orderedMatrixCoordinates = data.getOrderedMatrixCoordinates(myRunTimes);
        int prevColumn = -1;
        int prevRow = -1;
        for (MatrixCellCoordinate coordinate : orderedMatrixCoordinates) {
            final int col = coordinate.getCol();
            final int row = coordinate.getRow();
            if (prevRow != -1) {
                drawSegment(prevRow, prevColumn, row, col, colorRGB);
            }
            prevColumn = col;
            prevRow = row;
        }
    }

}
