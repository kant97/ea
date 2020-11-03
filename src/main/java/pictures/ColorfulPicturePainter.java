package pictures;

import optimal.configuration.probability.IterativeProbabilityConfiguration;
import optimal.probabilitySampling.ProbabilitySearcher;
import org.ejml.simple.SimpleMatrix;
import org.jetbrains.annotations.NotNull;
import pictures.coloring.AbstractColoring;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ColorfulPicturePainter extends Frame {

    public static void main(String[] a) throws IOException {
        final MatrixDataProcessor matrixDataProcessor = new MatrixDataProcessor("tmp/A1.csv", 9, 8, 10);
        matrixDataProcessor.loadData();
        drawChart(matrixDataProcessor.getProcessedData());
    }

    public static void drawChart(SimpleMatrix matrix) throws IOException {
        final int width = 400;
        final int height = 400;

        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g2d = bufferedImage.createGraphics();

        final ViridisPlotDrawer viridisPlotDrawer = new ViridisPlotDrawer(0, 0, width, height, g2d, matrix,
                AbstractColoring.createColoring(matrix, AbstractColoring.ColoringStrategy.MODIFIED),
                ProbabilitySearcher.createProbabilitySearcher(new IterativeProbabilityConfiguration(0.01, 0.5,
                        0.01)));
        viridisPlotDrawer.drawViridisPlot();

//        addChart(viridisPlotDrawer);

        g2d.dispose();
        // Save as PNG
        final File file = new File("myimage.png");
        ImageIO.write(bufferedImage, "png", file);
    }

    public static void addChart(ViridisPlotDrawer viridisPlotDrawer) {
        //        final Map<Integer, Double> mp = getIntegerDoubleMap();
        final ChartDataProcessor chartDataProcessor = new ChartDataProcessor("twoRateMap.csv", 100);
        chartDataProcessor.loadData();
        final Map<Integer, Double> mp = chartDataProcessor.getProcessedData();
        viridisPlotDrawer.addChart(mp, Color.BLACK);
    }

    @NotNull
    public static Map<Integer, Double> getIntegerRandomDoubleMap() {
        final Map<Integer, Double> mp = new HashMap<>();
        final Random random = new Random();
        final double rangeMin = 0.001;
        final double rangeMax = 0.6;
        for (int fitnessDistance = 1; fitnessDistance <= 50; fitnessDistance++) {
            final double randomDouble = rangeMin + (rangeMax - rangeMin) * random.nextDouble();
            mp.put(fitnessDistance, randomDouble);
        }
        return mp;
    }
}
