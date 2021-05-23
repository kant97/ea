package optimal.execution.cluster.generation.configs;

import optimal.configuration.OneExperimentConfiguration;
import optimal.configuration.PiExistenceTransitionClusterConfiguration;
import optimal.configuration.ProbabilityVectorGenerationConfiguration;
import optimal.configuration.loaders.ManyExperimentsConfigurationLoader;
import optimal.configuration.loaders.OneExperimentConfigurationLoader;
import optimal.configuration.loaders.ProbabilityVectorGenerationConfigurationLoader;
import optimal.execution.cluster.ConfigurationToNumberTranslator;
import optimal.execution.cluster.Utils;
import optimal.execution.cluster.generation.VectorsDirectoryNameGenerator;
import optimal.optimal2.AbstractPiExistenceClassesManager;
import optimal.optimal2.generation.MyConventionCsvNamesGenerator;
import optimal.probabilitySampling.ProbabilitySpace;
import org.jetbrains.annotations.NotNull;

import javax.naming.ConfigurationException;
import java.io.BufferedWriter;
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
        } catch (ConfigurationException | IOException e) {
            throw new IllegalStateException("Failed to load global configurations");
        }
        this.myExperimentConfigurationSerializer =
                experimentConfigurationLoader.getOneExperimentConfigurationSerializer();
    }

    public void generateJsonConfigs() {
        int offset = 0;
        amountOfGeneratedFiles = 0;
        for (OneExperimentConfiguration oneExperimentConfiguration : myExperimentConfigurations) {
            if (oneExperimentConfiguration.problemConfig.isNonAcyclicFunction()) {
                generateForNonAcyclicFitnessFunction(oneExperimentConfiguration);
            } else {
                offset = generateForAcyclicFitnessFunction(offset, oneExperimentConfiguration);
            }
        }
    }

    //    Files will be c<pi existence class id>_r<mutation rate distance value>.json
    private void generateForNonAcyclicFitnessFunction(@NotNull OneExperimentConfiguration oneExperimentConfiguration) {
        final String resultDirectory = "./" + vectorsDirectoryNameGenerator.generateNewResultsDirectoryName() + "/";
        final MyConventionCsvNamesGenerator namesGenerator = new MyConventionCsvNamesGenerator(oneExperimentConfiguration.getProbabilityEnumerationConfiguration());
        preparePlaceForFiles(oneExperimentConfiguration, resultDirectory);
        final AbstractPiExistenceClassesManager piExistenceClassesManager = AbstractPiExistenceClassesManager.create(oneExperimentConfiguration.problemConfig);
        for (int fitness = oneExperimentConfiguration.endFitness - 1; fitness >= oneExperimentConfiguration.beginFitness; fitness--) {
            final ProbabilitySpace ps = ProbabilitySpace.createProbabilitySpace(oneExperimentConfiguration.probabilityEnumeration);
            for (double r = ps.getInitialProbability(); !ps.isFinished(); r = ps.getNextProb()) {
                final List<Integer> curPiExistenceClasses = piExistenceClassesManager.getPiExistenceClassesWithFitness(fitness);
                for (int piExistenceClassId : curPiExistenceClasses) {
                    final String fileName = namesGenerator.getCsvFileName(piExistenceClassId, r);
                    writeNonAcyclicConfigToFile(new PiExistenceTransitionClusterConfiguration(
                                    r,
                                    fitness,
                                    oneExperimentConfiguration.problemConfig,
                                    oneExperimentConfiguration.algorithmConfig,
                                    oneExperimentConfiguration.stopConditionConfig,
                                    fileName + ".csv",
                                    resultDirectory,
                                    piExistenceClassId),
                            fileName + "_" + amountOfGeneratedFiles + ".json");
                    onNewFileCreation(0);
                }
            }
        }
    }

    private int generateForAcyclicFitnessFunction(int offset, OneExperimentConfiguration oneExperimentConfiguration) {
        final ConfigurationToNumberTranslator configurationToNumberTranslator =
                new ConfigurationToNumberTranslator(oneExperimentConfiguration);
        String resultDirectory = "./" + vectorsDirectoryNameGenerator.generateNewResultsDirectoryName() + "/";
        preparePlaceForFiles(oneExperimentConfiguration, resultDirectory);
        for (int f = oneExperimentConfiguration.beginFitness; f < oneExperimentConfiguration.endFitness; f++) {
            final ProbabilitySpace probabilitySpace =
                    ProbabilitySpace.createProbabilitySpace(oneExperimentConfiguration.getProbabilityEnumerationConfiguration());
            for (double p = probabilitySpace.getInitialProbability(); !probabilitySpace.isFinished(); p =
                    probabilitySpace.getNextProb()) {
                final int number = configurationToNumberTranslator.translateFitnessAndMutationRateToNumber(f, p);
                final int configFileId = offset + number;
                writeConfigToFile(oneExperimentConfiguration, f, p, configFileId, number, resultDirectory);
                onNewFileCreation(configFileId);
            }
        }
        offset = maxFileId + 1;
        return offset;
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

    private void writeNonAcyclicConfigToFile(PiExistenceTransitionClusterConfiguration configuration, String configFileName) {
        try (final BufferedWriter writer =
                     new BufferedWriter(new FileWriter(Utils.CONFIGURATIONS_DIRECTORY + configFileName))) {
            final ProbabilityVectorGenerationConfigurationLoader loader =
                    new ProbabilityVectorGenerationConfigurationLoader("tmp");
            writer.write(loader.serializeConfiguration(configuration));
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
