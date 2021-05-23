package pictures.optimal2Pictures;

import optimal.configuration.MainConfiguration;
import optimal.configuration.loaders.MainConfigurationLoader;
import optimal.probabilitySampling.IntegerToProbabilityBijectiveMapping;
import optimal.probabilitySampling.ProbabilitySpace;
import optimal.utils.CorruptedCsvException;
import org.jetbrains.annotations.NotNull;

import javax.naming.ConfigurationException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RegretPlotCreationMain {
    public static void main(String[] args) {
        final String configFileFQN = "5-plateau_k2/processed_lambda=512/config0.json";
        final String experimentDataFileFQN = "5-plateau_k2/processed_lambda=512/result0.csv";
        final Path algorithmTracePath = Paths.get("myAlgorithmTraces/abRun2.csv");
//        final Path algorithmTracePath = Paths.get("myAlgorithmTraces/twoRateRun0.csv");

        createRegretPlot(configFileFQN, experimentDataFileFQN, algorithmTracePath);
    }

    public static void createRegretPlot(@NotNull String configFileFQN,
                                         @NotNull String experimentDataFileFQN,
                                         @NotNull Path algorithmTracePath) {
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
        final AlgorithmTrace algorithmTrace;
        try {
            algorithmTrace = AlgorithmTrace.createAlgorithmTraceByCsvData(algorithmTracePath);
        } catch (CorruptedCsvException e) {
            throw new IllegalStateException("Unable to parse algorithm trace from file " + algorithmTracePath.toAbsolutePath(), e);
        } catch (IOException e) {
            throw new IllegalStateException("IO exception while handling algorithm trace csv data from file " + algorithmTracePath.toAbsolutePath(), e);
        }
        new RegretPlot(heatMap, algorithmTrace).printToCsvFile(Paths.get("regret.csv"));
    }
}
