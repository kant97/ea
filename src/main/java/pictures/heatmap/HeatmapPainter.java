package pictures.heatmap;

import org.ejml.simple.SimpleMatrix;
import org.jetbrains.annotations.NotNull;
import pictures.PlottableInMatrixData;
import pictures.coloring.AbstractColouring;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class HeatmapPainter {

    private final @NotNull SimpleMatrix matrix;

    private final static int WIDTH = 800;
    private final static int HEIGHT = 800;

    /* Mutable */
    private ViridisPlotDrawer viridisPlotDrawer = null;
    private Graphics2D g2d;
    private BufferedImage bufferedImage;

    public HeatmapPainter(@NotNull SimpleMatrix matrix) {
        this.matrix = matrix;
    }

    public void addHeatmap(@NotNull AbstractColouring.ColoringStrategy coloringStrategy) {
        bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g2d = bufferedImage.createGraphics();

        viridisPlotDrawer = new ViridisPlotDrawer(0, 0, WIDTH, HEIGHT, g2d, matrix,
                AbstractColouring.createColoring(matrix, coloringStrategy));
        viridisPlotDrawer.drawViridisHeatmap();
    }

    public void addLineChart(@NotNull PlottableInMatrixData data) {
        if (viridisPlotDrawer == null) {
            throw new IllegalStateException("Heatmap is supposed to be added before the line chart, but this is not " +
                    "the case.");
        }
        viridisPlotDrawer.addChart(data, data.getDataInPlotColor());
    }

    public void draw() throws IOException {
        g2d.dispose();
        // Save as PNG
        final File file = new File("myimage.png");
        ImageIO.write(bufferedImage, "png", file);
    }
}
