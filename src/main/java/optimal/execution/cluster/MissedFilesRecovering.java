package optimal.execution.cluster;

import optimal.configuration.OneExperimentConfiguration;
import optimal.configuration.ProbabilityVectorGenerationConfiguration;
import optimal.configuration.loaders.ProbabilityVectorGenerationConfigurationLoader;
import optimal.execution.ExecutionManager;
import optimal.execution.cluster.generation.vectors.FitnessLevelVectorGenerator;
import optimal.execution.cluster.generation.vectors.VectorGenerationEvent;
import optimal.probabilitySampling.ProbabilitySearcher;
import org.jetbrains.annotations.NotNull;

import javax.naming.ConfigurationException;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MissedFilesRecovering {
    private final String myNameOfAllVectorsDir;
    private final OneExperimentConfiguration myOneExperimentConfiguration;
    private final ConfigurationToNumberTranslator myConfigurationToNumberTranslator;
    private final String myCurrentDirName; //without / at the end
    private final int myOffset;
    private int myMaxFileId = Integer.MIN_VALUE;
    private volatile int myNumberOfFilesRecovered = 0;

    public MissedFilesRecovering(String myNameOfAllVectorsDir,
                                 OneExperimentConfiguration oneExperimentConfiguration,
                                 String myCurrentDirName, int offset) {
        this.myNameOfAllVectorsDir = myNameOfAllVectorsDir;
        myOneExperimentConfiguration = oneExperimentConfiguration;
        myConfigurationToNumberTranslator = new ConfigurationToNumberTranslator(oneExperimentConfiguration);
        this.myCurrentDirName = myCurrentDirName;
        this.myOffset = offset;
    }

    public int recoverMissedFiles() {
        myNumberOfFilesRecovered = 0;
        ExecutionManager.createThreadPool(4);
        int num = 50;
        final List<Future<?>> futures = new ArrayList<>();
        for (int f = myOneExperimentConfiguration.endFitness - 1; f >= myOneExperimentConfiguration.beginFitness; f--) {
            final ProbabilitySearcher probabilitySearcher =
                    ProbabilitySearcher.createProbabilitySearcher(myOneExperimentConfiguration.probabilityEnumeration);
            for (double p = probabilitySearcher.getInitialProbability(); !probabilitySearcher.isFinished();
                 p = probabilitySearcher.getNextProb()) {
                final int fileId = myConfigurationToNumberTranslator.translateFitnessAndMutationRateToNumber(f, p);
                if (fileId != num) {
                    throw new IllegalStateException("Everything is broken");
                }
                myMaxFileId = Math.max(myOffset + fileId, myMaxFileId);
                futures.add(ExecutionManager.executeOnThreadPool(() -> recoverPossibleCorruptedVector(fileId)));
                num++;
            }
        }
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new IllegalStateException(e);
            }
        }
        ExecutionManager.stopThreadPool();
        return myNumberOfFilesRecovered;
    }

    @NotNull
    private String getFileFqn(int fileId) {
        return myCurrentDirName + "/" + fileId + ".csv";
    }

    private static boolean doesFileExist(String fileFqn) {
        return Files.exists(Paths.get(fileFqn));
    }

    private void recoverPossibleCorruptedVector(int skippedFileId) {
        final int newFileId = myOffset + skippedFileId;
        final String jsonConfigFileForSkippedVector = Utils.CONFIGURATIONS_DIRECTORY + newFileId +
                ".json";
        if (!doesFileExist(jsonConfigFileForSkippedVector)) {
            throw new IllegalStateException("Json config for file with id " + newFileId + " does not exist");
        }
        final ProbabilityVectorGenerationConfigurationLoader loader =
                new ProbabilityVectorGenerationConfigurationLoader(jsonConfigFileForSkippedVector);
        final ProbabilityVectorGenerationConfiguration configuration;
        try {
            configuration = loader.getConfiguration();
        } catch (FileNotFoundException | ConfigurationException e) {
            throw new IllegalStateException(e);
        }
        if (!isConfigurationGood(configuration)) {
            throw new IllegalStateException("Configuration " + configuration + " is not good");
        }
        final FitnessLevelVectorGenerator fitnessLevelVectorGenerator =
                FitnessLevelVectorGenerator.createFitnessLevelVectorGenerator(jsonConfigFileForSkippedVector,
                        myNameOfAllVectorsDir,
                        FitnessLevelVectorGenerator.Strategy.RECOVERY);
        fitnessLevelVectorGenerator.addVectorGenerationStateListener(e -> {
            if (e == VectorGenerationEvent.GENERATION_STARTED) {
                synchronized (System.out) {
                    System.out.println("Recovering of missed file " + getFileFqn(skippedFileId) + " ...");
                }
            }
            if (e == VectorGenerationEvent.GENERATION_DONE) {
                synchronized (System.out) {
                    System.out.println("Done: file " + getFileFqn(skippedFileId) + " recovered");
                    myNumberOfFilesRecovered++;
                }
            }
        });
        fitnessLevelVectorGenerator.generateFitnessIncreasesVector();
    }

    private boolean isConfigurationGood(ProbabilityVectorGenerationConfiguration configuration) {
        return configuration.getProblemConfig().equals(myOneExperimentConfiguration.problemConfig)
                && configuration.getAlgorithmConfig().equals(myOneExperimentConfiguration.algorithmConfig)
                && configuration.getStopConditionConfig().equals(myOneExperimentConfiguration.stopConditionConfig)
                && (myOneExperimentConfiguration.beginFitness <= configuration.getFitness())
                && (configuration.getFitness() <= myOneExperimentConfiguration.endFitness);
    }

    public int getMaxFileId() {
        return myMaxFileId;
    }
}
