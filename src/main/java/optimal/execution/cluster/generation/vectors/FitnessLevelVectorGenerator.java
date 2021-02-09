package optimal.execution.cluster.generation.vectors;

import optimal.configuration.ProbabilityVectorGenerationConfiguration;
import optimal.configuration.loaders.ProbabilityVectorGenerationConfigurationLoader;
import optimal.execution.cluster.Utils;
import optimal.probability.ProbabilityVectorGeneratorInRuntime;
import optimal.probability.ProbabilityVectorGeneratorManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import problem.Problem;
import problem.ProblemsManager;

import javax.naming.ConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FitnessLevelVectorGenerator {
    private static final String CSV_FILE_HEADER = "fitnessIncrease,probability\n";

    private final String myConfigFileName;
    private final String myDirectoryForVectorDirectoriesName; //without / at the end
    private final Strategy myStrategy;
    private final List<Consumer<VectorGenerationEvent>> myVectorGenerationListeners = new ArrayList<>();

    private @Nullable ProbabilityVectorGenerationConfiguration configuration = null;
    private @Nullable ProbabilityVectorGeneratorInRuntime myProbabilityVectorGenerator = null;

    protected FitnessLevelVectorGenerator(String configFileName, String dirsForVectorDirsName, Strategy strategy) {
        this.myConfigFileName = configFileName;
        myDirectoryForVectorDirectoriesName = dirsForVectorDirsName;
        myStrategy = strategy;
    }

    public static FitnessLevelVectorGenerator createFitnessLevelVectorGenerator(@NotNull String configFileName,
                                                                                @NotNull Strategy strategy) {
        return createFitnessLevelVectorGenerator(configFileName, ".", strategy);
    }

    public static FitnessLevelVectorGenerator createFitnessLevelVectorGenerator(@NotNull String configFileName,
                                                                                @NotNull String dirsForVectorDirsName,
                                                                                @NotNull Strategy strategy) {
        if (strategy == Strategy.GENERATE_ALL) {
            return new FitnessLevelVectorGenerator(configFileName, dirsForVectorDirsName, strategy);
        } else if (strategy == Strategy.RECOVERY) {
            return new RecoveryFitnessLevelVectorGenerator(configFileName, dirsForVectorDirsName);
        }
        throw new IllegalArgumentException("Unknown strategy " + strategy.name());
    }

    public enum Strategy {
        GENERATE_ALL, RECOVERY
    }

    private void setUpTheState() {
        loadConfigurations();
        final Problem problem = ProblemsManager.createProblemInstanceWithFixedFitness(configuration.getProblemConfig(),
                configuration.getFitness());
        myProbabilityVectorGenerator =
                ProbabilityVectorGeneratorManager.getProbabilityVectorGeneratorInRuntime(configuration.getProbability(),
                        problem, configuration.getAlgorithmConfig(), configuration.getStopConditionConfig());
        Utils.createResultsDirectoryInFsIfNotExists(getResultsDirectory());
    }

    private void loadConfigurations() {
        if (configuration != null) {
            return;
        }
        final ProbabilityVectorGenerationConfigurationLoader probabilityVectorGenerationConfigurationLoader =
                new ProbabilityVectorGenerationConfigurationLoader(myConfigFileName);
        try {
            configuration = probabilityVectorGenerationConfigurationLoader.getConfiguration();
        } catch (ConfigurationException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @NotNull
    private String getResultsDirectory() {
        if (configuration == null) {
            throw new IllegalStateException("Load configurations at first");
        }
        return myDirectoryForVectorDirectoriesName + "/" + configuration.getOutputDirectory();
    }

    protected ArrayList<Double> runVectorGeneration() {
        if (myProbabilityVectorGenerator == null) {
            throw new IllegalStateException("Set up the state at first");
        }
        notifyListeners(VectorGenerationEvent.GENERATION_STARTED);
        return myProbabilityVectorGenerator.getProbabilityVector();
    }

    private void writeResults(@NotNull ArrayList<Double> probabilityVector) {
        if (configuration == null) {
            throw new IllegalStateException("Set up the state at first");
        }
        try (final BufferedWriter writer =
                     new BufferedWriter(new FileWriter(getResultsDirectory() + configuration.getOutputFileName()))) {
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
        notifyListeners(VectorGenerationEvent.GENERATION_DONE);
    }

    private void writeResultsWithWriter(@NotNull ArrayList<Double> probabilityVector, Writer writer) throws IOException {
        writer.write(CSV_FILE_HEADER);
        for (int i = 0; i < probabilityVector.size(); i++) {
            writer.write(i + "," + probabilityVector.get(i) + "\n");
        }
    }

    protected boolean needGenerateVector(@NotNull String vectorFqn) {
        return true;
    }

    public void generateFitnessIncreasesVector() {
        loadConfigurations();
        assert configuration != null;
        if (!needGenerateVector(getResultsDirectory() + configuration.getOutputFileName())) {
            notifyListeners(VectorGenerationEvent.ABORT_GENERATION_WHEN_NO_RECOVERY_NEEDED);
            return;
        }
        setUpTheState();
        final ArrayList<Double> probabilityVector = runVectorGeneration();
        writeResults(probabilityVector);
    }

    public void addVectorGenerationStateListener(Consumer<VectorGenerationEvent> listener) {
        myVectorGenerationListeners.add(listener);
    }

    private void notifyListeners(VectorGenerationEvent event) {
        for (Consumer<VectorGenerationEvent> consumer : myVectorGenerationListeners) {
            consumer.accept(event);
        }
    }
}
