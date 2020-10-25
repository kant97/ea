package pictures;

import org.ejml.simple.SimpleMatrix;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ColorfulPicturePainter extends Frame {

    public static void main(String[] a) throws IOException {
        final DataProcessor dataProcessor = new DataProcessor("data.csv", 9, 8, 10);
        dataProcessor.loadData();
        drawChart(dataProcessor.getRunTimesAsMatrix());
    }

    public static void drawChart(SimpleMatrix matrix) throws IOException {
        final int width = 400;
        final int height = 400;

        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g2d = bufferedImage.createGraphics();

        final ViridisPlotDrawer viridisPlotDrawer = new ViridisPlotDrawer(0, 0, width, height, g2d, matrix);
        viridisPlotDrawer.drawViridisPlot();

        g2d.dispose();
        // Save as PNG
        final File file = new File("myimage.png");
        ImageIO.write(bufferedImage, "png", file);
    }
}
