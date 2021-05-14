package optimal.configuration;

import optimal.configuration.algorithms.AlgorithmConfig;
import optimal.configuration.problems.ProblemConfig;
import optimal.configuration.transitionsGeneration.TransitionsGenerationConfiguration;
import org.jetbrains.annotations.NotNull;

import javax.naming.ConfigurationException;
import java.util.Objects;

public class MainConfiguration implements ValidatableConfiguration, VisitableConfiguration {
    private final ProblemConfig problemConfig;
    private final AlgorithmConfig algorithmConfig;
    private final int beginFitness;
    private final int endFitness;
    private final TransitionsGenerationConfiguration transitionsGeneration;

    public MainConfiguration(ProblemConfig problemConfig, AlgorithmConfig algorithmConfig, int beginFitness, int endFitness, TransitionsGenerationConfiguration transitionsGeneration) {
        this.problemConfig = problemConfig;
        this.algorithmConfig = algorithmConfig;
        this.beginFitness = beginFitness;
        this.endFitness = endFitness;
        this.transitionsGeneration = transitionsGeneration;
    }

    public ProblemConfig getProblemConfig() {
        return problemConfig;
    }

    public AlgorithmConfig getAlgorithmConfig() {
        return algorithmConfig;
    }

    public int getBeginFitness() {
        return beginFitness;
    }

    public int getEndFitness() {
        return endFitness;
    }

    public TransitionsGenerationConfiguration getTransitionsGeneration() {
        return transitionsGeneration;
    }

    @Override
    public String toString() {
        return "MainConfiguration{" +
                "problemConfig=" + problemConfig +
                ", algorithmConfig=" + algorithmConfig +
                ", beginFitness=" + beginFitness +
                ", endFitness=" + endFitness +
                ", transitionsGeneration=" + transitionsGeneration +
                '}';
    }

    @Override
    public void validate() throws ConfigurationException {
        problemConfig.validate();
        algorithmConfig.validate();
        transitionsGeneration.validate();
        if (beginFitness > endFitness) {
            throw new ConfigurationException("Error in configuration of test: " + this + ". \"beginFitness\" " +
                    "could not be greater then \"endFitness\"");
        }
    }

    @Override
    public @NotNull String accept(@NotNull ConfigurationVisitor visitor) {
        return visitor.visitMainConfiguration(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MainConfiguration that = (MainConfiguration) o;
        return beginFitness == that.beginFitness && endFitness == that.endFitness && problemConfig.equals(that.problemConfig) && algorithmConfig.equals(that.algorithmConfig) && transitionsGeneration.equals(that.transitionsGeneration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(problemConfig, algorithmConfig, beginFitness, endFitness, transitionsGeneration);
    }
}
