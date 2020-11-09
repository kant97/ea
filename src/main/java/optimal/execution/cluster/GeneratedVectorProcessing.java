package optimal.execution.cluster;

import optimal.ExperimentWithPrecomputedVector;
import optimal.configuration.OneExperimentConfiguration;
import optimal.configuration.OptimalMutationRateSearchingSingleExperimentConfiguration;
import optimal.configuration.loaders.OneExperimentConfigurationLoader;
import optimal.configuration.vectorGeneration.PrecomputedVectorReadingConfiguration;
import optimal.execution.cluster.generation.VectorsDirectoryNameGenerator;
import org.jetbrains.annotations.NotNull;

import javax.naming.ConfigurationException;
import java.io.FileNotFoundException;
import java.nio.file.StandardOpenOption;

public class GeneratedVectorProcessing {
    private final String myNameOfAllVectorsDir; // without / at the end
    private final VectorsDirectoryNameGenerator vectorsDirectoryNameGenerator = new VectorsDirectoryNameGenerator();
    private int offset = 0;

    public GeneratedVectorProcessing(String myNameOfAllVectorsDir) {
        this.myNameOfAllVectorsDir = myNameOfAllVectorsDir;
    }

    private void recoverMissedFilesInDirectory(OneExperimentConfiguration configuration,
                                               String directory) {
        final MissedFilesRecovering missedFilesRecovering = new MissedFilesRecovering(myNameOfAllVectorsDir,
                configuration, directory, offset);
        final int recoveredFilesCount = missedFilesRecovering.recoverMissedFiles();
        offset += missedFilesRecovering.getMaxFileId() + 1;
        System.out.println(recoveredFilesCount + " files were recovered for directory " + directory);
    }

    protected void processDirectory(String directory) {
        System.out.println("----------------------------- " +
                "Processing directory " + directory +
                " -----------------------------");
        OneExperimentConfigurationLoader loader =
                new OneExperimentConfigurationLoader(directory + "/" + Utils.GLOBAL_CONFIGS_FILE_NAME_JSON);
        final OneExperimentConfiguration configuration;
        try {
            configuration = loader.getConfiguration();
        } catch (FileNotFoundException | ConfigurationException e) {
            throw new IllegalStateException(e);
        }
        recoverMissedFilesInDirectory(configuration, directory);
        OptimalMutationRateSearchingSingleExperimentConfiguration configuration1 =
                new OptimalMutationRateSearchingSingleExperimentConfiguration(configuration.problemConfig,
                        configuration.algorithmConfig,
                        configuration.beginFitness,
                        configuration.endFitness,
                        configuration.probabilityEnumeration,
                        new PrecomputedVectorReadingConfiguration(Utils.GLOBAL_CONFIGS_FILE_NAME_JSON, directory));
        System.out.println("Running experiment");
        final String resultsDirName = getResultsDirName(configuration);
        new ExperimentWithPrecomputedVector(resultsDirName, configuration1, StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING).runExperiment();
        System.out.println("Done. Results were written to " + resultsDirName);
        System.out.println();
    }

    private String getResultsDirName(OneExperimentConfiguration configuration) {
        return myNameOfAllVectorsDir + "/optimal_for_lambda=" + configuration.algorithmConfig.getLambda();
    }

    public void processAllVectors() {
        for (String vectorDir = vectorsDirectoryNameGenerator.generateNewResultsDirectoryName();
             Utils.doesDirectoryExist(getVectorDirectoryFqn(vectorDir));
             vectorDir = vectorsDirectoryNameGenerator.generateNewResultsDirectoryName()) {
            processDirectory(getVectorDirectoryFqn(vectorDir));
        }
    }

    @NotNull
    private String getVectorDirectoryFqn(String vectorDir) {
        return myNameOfAllVectorsDir + "/" + vectorDir;
    }
}
