package optimal.optimal2.generation;

import optimal.configuration.MainConfiguration;
import optimal.configuration.OneExperimentConfiguration;
import optimal.configuration.loaders.OneExperimentConfigurationLoader;
import optimal.configuration.transitionsGeneration.PrecomputedTransitionsReadingConfiguration;
import optimal.utils.CorruptedCsvException;
import optimal.utils.PiExistenceTransitionsProcessor;
import org.jetbrains.annotations.NotNull;

import javax.naming.ConfigurationException;
import java.io.IOException;
import java.util.Map;

public class PrecomputedTransitionsReader extends AbstractTransitionsGenerator {
    private final MainConfiguration mainConfiguration;
    private final PrecomputedTransitionsReadingConfiguration transitionsConfiguration;
    private final MyConventionCsvNamesGenerator csvNamesGenerator;

    public PrecomputedTransitionsReader(@NotNull MainConfiguration mainConfiguration) {
        this.mainConfiguration = mainConfiguration;
        this.transitionsConfiguration = (PrecomputedTransitionsReadingConfiguration) mainConfiguration.getTransitionsGeneration();
        this.csvNamesGenerator = new MyConventionCsvNamesGenerator(transitionsConfiguration.getProbabilityEnumeration());
    }

    @Override
    public @NotNull Map<Integer, Double> getTransitionsProbabilities(double r, int piExistenceClassId) {
        final String validationConfigPath = transitionsConfiguration.getConfigForWhichTheFilesWereComputedPath();
        if (!isTheSameEnumerationConfig(validationConfigPath)) {
            throw new IllegalStateException("Current configuration does not match with the one for the precomputed transitions: " + validationConfigPath);
        }
        final String csvFileName = csvNamesGenerator.getCsvFileName(piExistenceClassId, r);
        try {
            return new PiExistenceTransitionsProcessor().loadAndGetProcessedData(transitionsConfiguration.getDirectoryWithPrecomputedFilesPath() + "/" + csvFileName + ".csv");
        } catch (IOException | CorruptedCsvException e) {
            throw new IllegalStateException("Unable to process transitions for r=" + r + " and piClass=" + piExistenceClassId, e);
        }
    }

    private boolean isCompatible(@NotNull OneExperimentConfiguration storedConfiguration) {
        return storedConfiguration.probabilityEnumeration.equals(transitionsConfiguration.getProbabilityEnumeration()) &&
                storedConfiguration.problemConfig.equals(mainConfiguration.getProblemConfig()) &&
                storedConfiguration.algorithmConfig.equals(mainConfiguration.getAlgorithmConfig()) &&
                storedConfiguration.stopConditionConfig.equals(mainConfiguration.getTransitionsGeneration().getStopConditionConfig()) &&
                storedConfiguration.endFitness == mainConfiguration.getEndFitness();
    }

    private boolean isCompatible(@NotNull MainConfiguration otherConfiguration) {
        return otherConfiguration.getProblemConfig().equals(mainConfiguration.getProblemConfig()) &&
                otherConfiguration.getAlgorithmConfig().equals(mainConfiguration.getAlgorithmConfig()) &&
                otherConfiguration.getTransitionsGeneration().equals(mainConfiguration.getTransitionsGeneration()) &&
                otherConfiguration.getEndFitness() == mainConfiguration.getEndFitness();
    }

    protected boolean isTheSameEnumerationConfig(String filePath) {
        final OneExperimentConfiguration storedConfiguration;
        try {
            storedConfiguration = new OneExperimentConfigurationLoader(filePath).getConfiguration();
        } catch (IOException | ConfigurationException e) {
            throw new IllegalStateException("Unable to load validation config for transitions", e);
        }
        return isCompatible(storedConfiguration);
    }
}
