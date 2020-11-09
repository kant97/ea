package optimal.execution.cluster.generation.configs;

import optimal.configuration.OneExperimentConfiguration;
import optimal.configuration.ProbabilityVectorGenerationConfiguration;
import optimal.configuration.loaders.ManyExperimentsConfigurationLoader;
import optimal.configuration.loaders.OneExperimentConfigurationLoader;
import optimal.configuration.loaders.ProbabilityVectorGenerationConfigurationLoader;
import optimal.execution.cluster.ConfigurationToNumberTranslator;
import optimal.execution.cluster.Utils;
import optimal.execution.cluster.generation.VectorsDirectoryNameGenerator;
import optimal.probabilitySampling.ProbabilitySearcher;

import javax.naming.ConfigurationException;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ClusterJsonConfigsGenerator {
    private final List<OneExperimentConfiguration> myExperimentConfigurations;
    private final OneExperimentConfigurationLoader myExperimentConfigurationSerializer;
    private final VectorsDirectoryNameGenerator vectorsDirectoryNameGenerator = new VectorsDirectoryNameGenerator();

    private int minFileId = Integer.MAX_VALUE;
    private int maxFileId = Integer.MIN_VALUE;
    private int amountOfGeneratedFiles;

    public ClusterJsonConfigsGenerator(ManyExperimentsConfigurationLoader experimentConfigurationLoader) {
        try {
            this.myExperimentConfigurations = experimentConfigurationLoader.getConfiguration().getConfigurations();
        } catch (FileNotFoundException | ConfigurationException e) {
            throw new IllegalStateException("Failed to load global configurations");
        }
        this.myExperimentConfigurationSerializer =
                experimentConfigurationLoader.getOneExperimentConfigurationSerializer();
    }

    public void generateJsonConfigs() {
        int offset = 0;
        for (OneExperimentConfiguration oneExperimentConfiguration : myExperimentConfigurations) {
            final ConfigurationToNumberTranslator configurationToNumberTranslator =
                    new ConfigurationToNumberTranslator(oneExperimentConfiguration);
            String resultDirectory = "./" + vectorsDirectoryNameGenerator.generateNewResultsDirectoryName() + "/";
            preparePlaceForFiles(oneExperimentConfiguration, resultDirectory);
            for (int f = oneExperimentConfiguration.beginFitness; f < oneExperimentConfiguration.endFitness; f++) {
                final ProbabilitySearcher probabilitySearcher =
                        ProbabilitySearcher.createProbabilitySearcher(oneExperimentConfiguration.getProbabilityEnumerationConfiguration());
                for (double p = probabilitySearcher.getInitialProbability(); !probabilitySearcher.isFinished(); p =
                        probabilitySearcher.getNextProb()) {
                    final int number = configurationToNumberTranslator.translateFitnessAndMutationRateToNumber(f, p);
                    final int configFileId = offset + number;
                    writeConfigToFile(oneExperimentConfiguration, f, p, configFileId, number, resultDirectory);
                    onNewFileCreation(configFileId);
                }
            }
            offset = maxFileId + 1;
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

    private void writeConfigToFile(OneExperimentConfiguration configuration, int fitness, double p, int configFileId,
                                   int resultFileId, String resultsDir) {
        final String configFileName = configFileId + ".json";
        final String resultFileName = resultFileId + ".csv";
        try (final BufferedWriter writer =
                     new BufferedWriter(new FileWriter(Utils.CONFIGURATIONS_DIRECTORY + configFileName))) {
            final ProbabilityVectorGenerationConfiguration probabilityVectorGenerationConfiguration =
                    new ProbabilityVectorGenerationConfiguration(p, fitness,
                            configuration.problemConfig, configuration.algorithmConfig,
                            configuration.stopConditionConfig,
                            resultFileName,
                            resultsDir);
            final ProbabilityVectorGenerationConfigurationLoader loader =
                    new ProbabilityVectorGenerationConfigurationLoader("tmp");
            writer.write(loader.serializeConfiguration(probabilityVectorGenerationConfiguration));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write experiment configurations to results folder");
        }
    }

    private void preparePlaceForFiles(OneExperimentConfiguration configuration, String resultsDir) {
        Utils.createResultsDirectoryInFsIfNotExists(resultsDir);
        try (final BufferedWriter writer =
                     new BufferedWriter(new FileWriter(resultsDir + Utils.GLOBAL_CONFIGS_FILE_NAME_JSON))) {
            final String serializeConfiguration =
                    myExperimentConfigurationSerializer.serializeConfiguration(configuration);
            writer.write(serializeConfiguration);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write experiment configurations to results folder");
        }
        Utils.createConfigsDirectoryInFsIfNotExists();
    }
}
