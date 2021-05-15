package pictures.optimal2Pictures;

import optimal.configuration.MainConfiguration;
import optimal.configuration.loaders.MainConfigurationLoader;
import optimal.probabilitySampling.IntegerToProbabilityBijectiveMapping;
import optimal.probabilitySampling.ProbabilitySpace;
import optimal.utils.CorruptedCsvException;
import pictures.coloring.AbstractColouring;

import javax.naming.ConfigurationException;
import java.io.IOException;
import java.nio.file.Paths;

public class HeatmapCreationMain {
    public static void main(String[] args) {
        final String configFileFQN = "allVectorsPlateau/processed_lambda=512/config0.json";
        final String experimentDataFileFQN = "allVectorsPlateau/processed_lambda=512/result0.csv";
        final AbstractColouring.ColoringStrategy coloringStrategy = AbstractColouring.ColoringStrategy.MODIFIED;

        drawHeatMap(configFileFQN, experimentDataFileFQN, coloringStrategy);
    }

    private static void drawHeatMap(String configFileFQN, String experimentDataFileFQN, AbstractColouring.ColoringStrategy coloringStrategy) {
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
        try {
            heatMap.saveToFile(new HeatMapPainter(heatMap.createColouring(coloringStrategy)), Paths.get("heatmap.png"));
        } catch (IOException e) {
            throw new IllegalStateException("Unable to save heatmap in file", e);
        }
    }
}
