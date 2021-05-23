package pictures.optimal2Pictures;

import javafx.util.Pair;
import optimal.configuration.MainConfiguration;
import optimal.configuration.loaders.MainConfigurationLoader;
import optimal.probabilitySampling.IntegerToProbabilityBijectiveMapping;
import optimal.probabilitySampling.ProbabilitySpace;
import optimal.utils.CorruptedCsvException;
import org.jetbrains.annotations.NotNull;
import pictures.coloring.AbstractColouring;

import javax.naming.ConfigurationException;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class HeatmapCreationMain {
    public static void main(String[] args) {
        final String configFileFQN = "5-plateau_k2/processed_lambda=32/config0.json";
        final String experimentDataFileFQN = "5-plateau_k2/processed_lambda=32/result0.csv";
        final AbstractColouring.ColoringStrategy coloringStrategy = AbstractColouring.ColoringStrategy.MODIFIED;
        final ArrayList<Pair<Path, Color>> tracesData = new ArrayList<>();
        tracesData.add(new Pair<>(Paths.get("myAlgorithmTraces/abRun2.csv"), Color.BLACK));
        tracesData.add(new Pair<>(Paths.get("myAlgorithmTraces/twoRateRun0.csv"), Color.RED));

        drawHeatMap(configFileFQN, experimentDataFileFQN, coloringStrategy, tracesData);
    }

    private static void drawHeatMap(@NotNull String configFileFQN,
                                    @NotNull String experimentDataFileFQN,
                                    @NotNull AbstractColouring.ColoringStrategy coloringStrategy,
                                    @NotNull ArrayList<Pair<Path, Color>> tracesData) {
        final MainConfigurationLoader loader = new MainConfigurationLoader(configFileFQN);
        final MainConfiguration configuration;
        try {
            configuration = loader.getConfiguration();
        } catch (IOException | ConfigurationException e) {
            throw new IllegalStateException("Unable to parse configurations for the experiment data", e);
        }
        final IntegerToProbabilityBijectiveMapping bijection = ProbabilitySpace.createProbabilitySpace(configuration.getTransitionsGeneration().getProbabilityEnumeration()).createBijectionToIntegers();
        final HeatMap heatMap;
        try {
            heatMap = HeatMap.createHeatMapByCsvData(Paths.get(experimentDataFileFQN), bijection);
        } catch (CorruptedCsvException e) {
            throw new IllegalStateException("Unable to parse results of experiments", e);
        } catch (IOException e) {
            throw new IllegalStateException("IO exception while handling experiment csv data", e);
        }
        final HeatMapPainter painter = new HeatMapPainter(heatMap.createColouring(coloringStrategy));
        for (Pair<Path, Color> trace : tracesData) {
            final Path path = trace.getKey();
            final AlgorithmTrace algorithmTrace;
            try {
                algorithmTrace = AlgorithmTrace.createAlgorithmTraceByCsvData(path);
            } catch (CorruptedCsvException e) {
                throw new IllegalStateException("Unable to parse algorithm trace from file " + path.toAbsolutePath(), e);
            } catch (IOException e) {
                throw new IllegalStateException("IO exception while handling algorithm trace csv data from file " + path.toAbsolutePath(), e);
            }
            painter.addAlgorithmTrace(algorithmTrace, trace.getValue());
        }
        try {
            heatMap.saveToFile(painter, Paths.get("myHeatmap.png"));
        } catch (IOException e) {
            throw new IllegalStateException("Unable to save heatmap in file", e);
        }
    }
}
