package optimal.execution.cluster;

import optimal.configuration.OneExperimentConfiguration;
import optimal.configuration.ProbabilityVectorGenerationConfiguration;
import optimal.configuration.loaders.OneExperimentConfigurationLoader;
import optimal.configuration.loaders.ProbabilityVectorGenerationConfigurationLoader;
import optimal.probabilitySampling.ProbabilitySearcher;

import javax.naming.ConfigurationException;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class ClusterJsonConfigsGenerator {
    private final OneExperimentConfiguration myExperimentConfiguration;
    private final OneExperimentConfigurationLoader myExperimentConfigurationLoader;

    private int minFileId = Integer.MAX_VALUE;
    private int maxFileId = Integer.MIN_VALUE;
    private int amountOfGeneratedFiles;

    public ClusterJsonConfigsGenerator(OneExperimentConfigurationLoader experimentConfigurationLoader) {
        try {
            this.myExperimentConfiguration = experimentConfigurationLoader.getConfiguration();
        } catch (FileNotFoundException | ConfigurationException e) {
            throw new IllegalStateException("Failed to load global configurations");
        }
        this.myExperimentConfigurationLoader = experimentConfigurationLoader;
    }

    public void generateJsonConfigs() {
        final ConfigurationToNumberTranslator configurationToNumberTranslator =
                new ConfigurationToNumberTranslator(myExperimentConfiguration);
        preparePlaceForFiles();
        for (int f = myExperimentConfiguration.beginFitness; f < myExperimentConfiguration.endFitness; f++) {
            final ProbabilitySearcher probabilitySearcher =
                    ProbabilitySearcher.createProbabilitySearcher(myExperimentConfiguration.getProbabilityEnumerationConfiguration());
            for (double p = probabilitySearcher.getInitialProbability(); !probabilitySearcher.isFinished(); p =
                    probabilitySearcher.getNextProb()) {
                final int number = configurationToNumberTranslator.translateFitnessAndMutationRateToNumber(f, p);
                writeConfigToFile(f, p, number);
                onNewFileCreation(number);
            }
        }
    }

    private void onNewFileCreation(int fileId) {
        minFileId = Math.min(minFileId, fileId);
        maxFileId = Math.max(maxFileId, fileId);
        amountOfGeneratedFiles++;
    }

    public int getMinFileId() {
        return minFileId;
    }

    public int getMaxFileId() {
        return maxFileId;
    }

    public int getAmountOfGeneratedFiles() {
        return amountOfGeneratedFiles;
    }

    private void writeConfigToFile(int fitness, double p, int fileId) {
        final String configFileName = fileId + ".json";
        final String resultFileName = fileId + ".csv";
        try (final BufferedWriter writer =
                     new BufferedWriter(new FileWriter(Utils.CONFIGURATIONS_DIRECTORY + configFileName))) {
            final ProbabilityVectorGenerationConfiguration probabilityVectorGenerationConfiguration =
                    new ProbabilityVectorGenerationConfiguration(p, fitness,
                            myExperimentConfiguration.problemConfig, myExperimentConfiguration.algorithmConfig,
                            myExperimentConfiguration.stopConditionConfig,
                            resultFileName);
            final ProbabilityVectorGenerationConfigurationLoader loader =
                    new ProbabilityVectorGenerationConfigurationLoader("tmp");
            writer.write(loader.serializeConfiguration(probabilityVectorGenerationConfiguration));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write experiment configurations to results folder");
        }
    }

    private void preparePlaceForFiles() {
        Utils.createResultsDirectoryInFsIfNotExists();
        try (final BufferedWriter writer =
                     new BufferedWriter(new FileWriter(Utils.RESULTS_DIRECTORY + Utils.GLOBAL_CONFIGS_FILE_NAME_JSON))) {
            final String serializeConfiguration =
                    myExperimentConfigurationLoader.serializeConfiguration(myExperimentConfiguration);
            writer.write(serializeConfiguration);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write experiment configurations to results folder");
        }
        Utils.createConfigsDirectoryInFsIfNotExists();
    }
}
