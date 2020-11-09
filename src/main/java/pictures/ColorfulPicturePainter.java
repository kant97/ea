package pictures;

import optimal.configuration.probability.IterativeProbabilityConfiguration;
import optimal.probabilitySampling.ProbabilitySearcher;
import optimal.utils.DataProcessor;
import org.ejml.simple.SimpleMatrix;
import org.jetbrains.annotations.NotNull;
import pictures.coloring.AbstractColouring;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class ColorfulPicturePainter extends Frame {

    private final Path myCsvFile;
    private final Path myImageFile;
    private final AbstractColouring.ColoringStrategy myColouringStrategy;

    public ColorfulPicturePainter(@NotNull Path myCsvFile, @NotNull Path myImageFile,
                                  @NotNull AbstractColouring.ColoringStrategy myColouringStrategy) {
        this.myCsvFile = myCsvFile;
        this.myImageFile = myImageFile;
        this.myColouringStrategy = myColouringStrategy;
    }

    public void drawHeatMap() {
        final MatrixDataProcessor matrixDataProcessor = new MatrixDataProcessor(myCsvFile.toAbsolutePath().toString()
                , 9, 8,
                10);
        matrixDataProcessor.loadData();
        doDrawHeatMap(matrixDataProcessor.getProcessedData());
    }

    private void doDrawHeatMap(@NotNull SimpleMatrix matrix) {
        final int width = 400;
        final int height = 400;

        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g2d = bufferedImage.createGraphics();

        final ViridisPlotDrawer viridisPlotDrawer = new ViridisPlotDrawer(0, 0, width, height, g2d, matrix,
                AbstractColouring.createColoring(matrix, myColouringStrategy)
        );
        viridisPlotDrawer.drawViridisPlot();

//        addChart(viridisPlotDrawer);

        g2d.dispose();
        // Save as PNG
        if (!Files.exists(myImageFile)) {
            try {
                Files.createFile(myImageFile);
            } catch (IOException e) {
                throw new IllegalStateException("Failed to create image file", e);
            }
        }
        final File file = myImageFile.toFile();
        try {
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void main(String[] a) {
//        drawHeatMapsForAllSubdirectories("all2-vectors-ruggedness");
//        drawHeatMapsOneResult("all2-vectors-ruggedness/optimal_for_lambda=16/allIntermediateResults.csv");
        printLambdaToRuntime("all2-vectors-ruggedness");
    }

    private static void drawHeatMapsOneResult(String resultFile) {
        final MatrixDataProcessor matrixDataProcessor = new MatrixDataProcessor(resultFile, 9, 8, 10);
        matrixDataProcessor.loadData();
        try {
            drawChart(matrixDataProcessor.getProcessedData(), AbstractColouring.ColoringStrategy.MODIFIED);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void drawHeatMapsForAllSubdirectories(String directoryWithOptimalResults) {
        final Path path = Paths.get(directoryWithOptimalResults);
        for (File directory : path.toFile().listFiles(File::isDirectory)) {
            if (!directory.getName().startsWith("optimal")) {
                continue;
            }
            final Optional<File> allResultsFile =
                    Arrays.stream(directory.listFiles()).filter(f -> f.getName().startsWith("all")).findFirst();
            if (!allResultsFile.isPresent()) {
                throw new IllegalStateException("File with all results is not found.");
            }
            final File file = allResultsFile.get();
            new ColorfulPicturePainter(file.toPath(),
                    Paths.get(directory.toPath().toAbsolutePath().toString() + "/heatmap.png"),
                    AbstractColouring.ColoringStrategy.MODIFIED).drawHeatMap();
        }
    }

    private static void printLambdaToRuntime(String directoryWithOptimalResults) {
        final Map<Integer, Double> lambdaToRuntime = getLambdaToRuntime(directoryWithOptimalResults);
        System.out.println("lambda, time");
        final List<Integer> sortedLambdas = lambdaToRuntime.keySet().stream().sorted().collect(Collectors.toList());
        for (int lambda : sortedLambdas) {
            System.out.println(lambda + ", " + lambdaToRuntime.get(lambda));
        }
    }

    private static Map<Integer, Double> getLambdaToRuntime(String directoryWithOptimalResults) {
        final Path path = Paths.get(directoryWithOptimalResults);
        Map<Integer, Double> ans = new HashMap<>();
        for (File directory : path.toFile().listFiles(File::isDirectory)) {
            if (!directory.getName().startsWith("optimal")) {
                continue;
            }
            final Optional<File> optimalFile =
                    Arrays.stream(directory.listFiles()).filter(f -> f.getName().equals("results.csv")).findFirst();
            if (!optimalFile.isPresent()) {
                throw new IllegalStateException("File with optimal results is not found.");
            }
            final OptimalDataProcessor optimalDataProcessor =
                    new OptimalDataProcessor(optimalFile.get().toPath().toAbsolutePath().toString(),
                            3, 10, 8);
            optimalDataProcessor.loadData();
            final Map.Entry<Integer, Double> processedData = optimalDataProcessor.getProcessedData();
            ans.put(processedData.getKey(), processedData.getValue());
        }
        return ans;
    }

    private static final class OptimalDataProcessor extends DataProcessor<Map.Entry<Integer, Double>> {
        private final int myLambdaIndex;
        private final int myTimeIndex;
        private final int myFitnessIndex;

        public OptimalDataProcessor(@NotNull String csvFileName, int lambdaIndex, int timeIndex, int fitnessIndex) {
            super(csvFileName);
            myLambdaIndex = lambdaIndex;
            myTimeIndex = timeIndex;
            myFitnessIndex = fitnessIndex;
        }

        @Override
        public Map.Entry<Integer, Double> getProcessedData() {
            if (myRecords == null) {
                throw new IllegalStateException("Data was not loaded");
            }
            int smallestFitness = Integer.MAX_VALUE;
            List<String> recordWithSmallestFitness = null;
            for (List<String> record : myRecords) {
                final int fitness = Integer.parseInt(record.get(myFitnessIndex));
                if (fitness < smallestFitness) {
                    smallestFitness = fitness;
                    recordWithSmallestFitness = record;
                }
            }
            if (recordWithSmallestFitness == null) {
                throw new IllegalStateException("Smallest fitness was not found");
            }
            return new AbstractMap.SimpleEntry<>(
                    Integer.parseInt(recordWithSmallestFitness.get(myLambdaIndex)),
                    Double.parseDouble(recordWithSmallestFitness.get(myTimeIndex))
            );
        }
    }

    public static void drawChart(SimpleMatrix matrix, AbstractColouring.ColoringStrategy coloringStrategy) throws IOException {
        final int width = 400;
        final int height = 400;

        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g2d = bufferedImage.createGraphics();

        final ViridisPlotDrawer viridisPlotDrawer = new ViridisPlotDrawer(0, 0, width, height, g2d, matrix,
                AbstractColouring.createColoring(matrix, coloringStrategy));
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
        viridisPlotDrawer.addChart(mp, Color.BLACK, new MatrixLine(viridisPlotDrawer.getMyRunTimes(),
                ProbabilitySearcher.createProbabilitySearcher(
                        new IterativeProbabilityConfiguration(0.01, 0.5, 0.01))));
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
