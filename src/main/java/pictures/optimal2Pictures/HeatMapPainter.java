package pictures.optimal2Pictures;

import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;
import pictures.MatrixDrawer;
import pictures.coloring.AbstractColouring;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class HeatMapPainter {
    private final static int WIDTH = 800;
    private final static int HEIGHT = 808;

    private final AbstractColouring colouring;
    private final List<AlgorithmTrace> traces = new ArrayList<>();
    private final List<Color> lineColorForTraces = new ArrayList<>();

    public HeatMapPainter(@NotNull AbstractColouring colouring) {
        this.colouring = colouring;
    }

    private Pair<MatrixDrawer, BufferedImage> doDrawHeatMap(@NotNull HeatMap heatMap) {
        final BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g2d = bufferedImage.createGraphics();
        final MatrixDrawer matrixDrawer = new MatrixDrawer(0, 0, 800, 808, g2d, colouring, heatMap.getColumnsNumber(), heatMap.getRowsNumber());
        for (int i = 0; i < heatMap.getRowsNumber(); i++) {
            for (int j = 0; j < heatMap.getColumnsNumber(); j++) {
                matrixDrawer.drawRect(i, j, colouring.getRgdColor(i, j));
            }
        }
        return new Pair<>(matrixDrawer, bufferedImage);
    }

    @NotNull
    public BufferedImage getHeatMapImage(@NotNull HeatMap heatMap) {
        return doDrawHeatMap(heatMap).getValue();
    }

    public void addAlgorithmTrace(@NotNull AlgorithmTrace trace, @NotNull Color color) {
        traces.add(trace);
        lineColorForTraces.add(color);
    }

    public void deleteAlgorithmTrace(@NotNull AlgorithmTrace trace) {
        final int i = traces.indexOf(trace);
        traces.remove(i);
        lineColorForTraces.remove(i);
    }

    @NotNull
    public BufferedImage getHeatMapImageWithAlgorithmsTracesAbove(@NotNull HeatMap heatMap) {
        final Pair<MatrixDrawer, BufferedImage> matrixDrawerBufferedImagePair = doDrawHeatMap(heatMap);
        final MatrixDrawer matrixDrawer = matrixDrawerBufferedImagePair.getKey();
        for (int i = 0; i < traces.size(); i++) {
            final AlgorithmTrace trace = traces.get(i);
            final int rgbTraceColor = lineColorForTraces.get(i).getRGB();
            Pair<Integer, Integer> prevRowCol = null;
            for (final Pair<Integer, Double> piClassAndProb : trace.getTrace()) {
                final Pair<Integer, Integer> rowCol = heatMap.getCellRowCol(piClassAndProb.getKey(), piClassAndProb.getValue());
                if (prevRowCol != null && rowCol.getValue() != null) {
                    matrixDrawer.drawSegment(prevRowCol.getKey(), prevRowCol.getValue(), rowCol.getKey(), rowCol.getValue(), rgbTraceColor);
                }
                prevRowCol = rowCol;
            }
        }
        return matrixDrawerBufferedImagePair.getValue();
    }
}
