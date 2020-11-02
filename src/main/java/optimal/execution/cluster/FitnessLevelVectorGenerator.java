package optimal.execution.cluster;

import optimal.configuration.ProbabilityVectorGenerationConfiguration;
import optimal.configuration.loaders.ProbabilityVectorGenerationConfigurationLoader;
import optimal.probability.ProbabilityVectorGeneratorInRuntime;
import optimal.probability.ProbabilityVectorGeneratorManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import problem.Problem;
import problem.ProblemsManager;

import javax.naming.ConfigurationException;
import java.io.*;
import java.util.ArrayList;

public class FitnessLevelVectorGenerator {
    private static final String CSV_FILE_HEADER = "fitnessIncrease,probability\n";

    private final String myConfigFileName;
    private @Nullable ProbabilityVectorGenerationConfiguration configuration = null;
    private @Nullable ProbabilityVectorGeneratorInRuntime myProbabilityVectorGenerator = null;

    public FitnessLevelVectorGenerator(String configFileName) {
        this.myConfigFileName = configFileName;
    }

    private void setUpTheState() {
        final ProbabilityVectorGenerationConfigurationLoader probabilityVectorGenerationConfigurationLoader =
                new ProbabilityVectorGenerationConfigurationLoader(myConfigFileName);
        try {
            configuration = probabilityVectorGenerationConfigurationLoader.getConfiguration();
        } catch (FileNotFoundException | ConfigurationException e) {
            throw new IllegalStateException(e);
        }
        final Problem problem = ProblemsManager.createProblemInstanceWithFixedFitness(configuration.getProblemConfig(),
                configuration.getFitness());
        myProbabilityVectorGenerator =
                ProbabilityVectorGeneratorManager.getProbabilityVectorGeneratorInRuntime(configuration.getProbability(),
                        problem, configuration.getAlgorithmConfig(), configuration.getStopConditionConfig());
        Utils.createResultsDirectoryInFsIfNotExists();
    }

    private ArrayList<Double> runVectorGeneration() {
        if (myProbabilityVectorGenerator == null) {
            throw new IllegalStateException("Set up the state at first");
        }
        return myProbabilityVectorGenerator.getProbabilityVector();
    }

    private void writeResults(@NotNull ArrayList<Double> probabilityVector) {
        if (configuration == null) {
            throw new IllegalStateException("Set up the state at first");
        }
        try (final BufferedWriter writer =
                     new BufferedWriter(new FileWriter(Utils.RESULTS_DIRECTORY + configuration.getOutputFileName()))) {
            writeResultsWithWriter(probabilityVector, writer);
        } catch (IOException e) {
            try {
                System.out.println("Failed to write results to file, so writing them to stdout");
                System.out.println("Current configuration is: " + configuration.toString());
                writeResultsWithWriter(probabilityVector, new PrintWriter(new OutputStreamWriter(System.out)));
            } catch (IOException ioException) {
                throw new IllegalStateException("Failed to write results even to stdout for configuration:\n" + configuration.toString());
            }
        }
    }

    private void writeResultsWithWriter(@NotNull ArrayList<Double> probabilityVector, Writer writer) throws IOException {
        writer.write(CSV_FILE_HEADER);
        for (int i = 0; i < probabilityVector.size(); i++) {
            writer.write(i + "," + probabilityVector.get(i) + "\n");
        }
    }

    public void generateFitnessIncreasesVector() {
        setUpTheState();
        final ArrayList<Double> probabilityVector = runVectorGeneration();
        writeResults(probabilityVector);
    }
}
