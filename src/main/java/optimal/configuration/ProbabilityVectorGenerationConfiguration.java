package optimal.configuration;

import optimal.configuration.algorithms.AlgorithmConfig;
import optimal.configuration.problems.ProblemConfig;
import optimal.configuration.runs.StopConditionConfiguration;
import org.jetbrains.annotations.NotNull;

import javax.naming.ConfigurationException;
import java.util.Objects;

public class ProbabilityVectorGenerationConfiguration implements ValidatableConfiguration, VisitableConfiguration {
    private final double probability;
    private final int fitness;
    private final ProblemConfig problemConfig;
    private final AlgorithmConfig algorithmConfig;
    private final StopConditionConfiguration stopConditionConfig;
    private final String outputFileName;
    private final String outputDirectory;

    public ProbabilityVectorGenerationConfiguration(double probability, int fitness,
                                                    @NotNull ProblemConfig problemConfig,
                                                    @NotNull AlgorithmConfig algorithmConfig,
                                                    @NotNull StopConditionConfiguration stopConditionConfig,
                                                    String outputFileName, String outputDirectory) {
        this.probability = probability;
        this.fitness = fitness;
        this.problemConfig = problemConfig;
        this.algorithmConfig = algorithmConfig;
        this.stopConditionConfig = stopConditionConfig;
        this.outputFileName = outputFileName;
        this.outputDirectory = outputDirectory;
    }

    public double getProbability() {
        return probability;
    }

    public int getFitness() {
        return fitness;
    }

    public ProblemConfig getProblemConfig() {
        return problemConfig;
    }

    public AlgorithmConfig getAlgorithmConfig() {
        return algorithmConfig;
    }

    public StopConditionConfiguration getStopConditionConfig() {
        return stopConditionConfig;
    }

    @Override
    public void validate() throws ConfigurationException {
        if (probability < 0) {
            throw new ConfigurationException("Probability should be positive");
        }
        if (probability > 1.) {
            throw new ConfigurationException("Probability can not be more than 1, but it is " + probability);
        }
        if (outputFileName == null) {
            throw new ConfigurationException("Output file name is not configured");
        }
        if (outputDirectory == null) {
            throw new ConfigurationException("Output directory name is not configured");
        }
        problemConfig.validate();
        algorithmConfig.validate();
        stopConditionConfig.validate();
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProbabilityVectorGenerationConfiguration that = (ProbabilityVectorGenerationConfiguration) o;
        return Double.compare(that.probability, probability) == 0 &&
                fitness == that.fitness &&
                problemConfig.equals(that.problemConfig) &&
                algorithmConfig.equals(that.algorithmConfig) &&
                stopConditionConfig.equals(that.stopConditionConfig) &&
                outputFileName.equals(that.outputFileName) &&
                outputDirectory.equals(that.outputDirectory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(probability, fitness, problemConfig, algorithmConfig, stopConditionConfig, outputFileName
                , outputDirectory);
    }

    @Override
    public String toString() {
        return "ProbabilityVectorGenerationConfiguration{" +
                "probability=" + probability +
                ", fitness=" + fitness +
                ", problemConfig=" + problemConfig +
                ", algorithmConfig=" + algorithmConfig +
                ", stopConditionConfig=" + stopConditionConfig +
                ", outputFileName='" + outputFileName + '\'' +
                ", outputDirectory='" + outputDirectory + '\'' +
                '}';
    }

    @Override
    public @NotNull String accept(@NotNull ConfigurationVisitor visitor) {
        return visitor.visitProbabilityVectorGenerationConfiguration(this);
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }
}
