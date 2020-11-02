package optimal.configuration;

import optimal.configuration.algorithms.AlgorithmConfig;
import optimal.configuration.probability.ProbabilitySamplingConfiguration;
import optimal.configuration.problems.ProblemConfig;
import optimal.oneStepAlgorithms.OneStepAlgorithmsManager;
import org.jetbrains.annotations.NotNull;

import javax.naming.ConfigurationException;
import java.util.Objects;

public class AbstractSingleExperimentConfiguration implements ValidatableConfiguration {
    public final ProblemConfig problemConfig;
    public final AlgorithmConfig algorithmConfig;
    public final int beginFitness;
    public final int endFitness;
    public final ProbabilitySamplingConfiguration probabilityEnumeration;

    public AbstractSingleExperimentConfiguration(ProblemConfig problemConfig, AlgorithmConfig algorithmConfig,
                                                 int beginFitness,
                                                 int endFitness,
                                                 ProbabilitySamplingConfiguration probabilityEnumeration) {
        this.problemConfig = problemConfig;
        this.algorithmConfig = algorithmConfig;
        this.beginFitness = beginFitness;
        this.endFitness = endFitness;
        this.probabilityEnumeration = probabilityEnumeration;
    }

    @NotNull
    public ProbabilitySamplingConfiguration getProbabilityEnumerationConfiguration() {
        return probabilityEnumeration; // default value
    }

    @Override
    public String toString() {
        return "OneExperimentConfiguration{" +
                "problemConfig=" + problemConfig +
                ", algorithmConfig=" + algorithmConfig +
                ", beginFitness=" + beginFitness +
                ", endFitness=" + endFitness +
                ", probabilityEnumeration=" + probabilityEnumeration +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractSingleExperimentConfiguration that = (AbstractSingleExperimentConfiguration) o;
        return beginFitness == that.beginFitness &&
                endFitness == that.endFitness &&
                problemConfig.equals(that.problemConfig) &&
                algorithmConfig.equals(that.algorithmConfig) &&
                probabilityEnumeration.equals(that.probabilityEnumeration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(problemConfig, algorithmConfig, beginFitness, endFitness, probabilityEnumeration);
    }

    @Override
    public void validate() throws ConfigurationException {
        if (probabilityEnumeration == null) {
            throw new ConfigurationException("Error in configuration of test: " + toString() + ". Probability " +
                    "enumeration strategy is not chosen");
        }
        if (problemConfig.getProblemType() == null) {
            throw new ConfigurationException("Problem type is not chosen, set value to the key \"problemType\"");
        }
        if (algorithmConfig.getAlgorithmType() == null) {
            throw new ConfigurationException("Algorithm type is not chosen for test " + toString() + ", set value to " +
                    "the key \"algorithmType\"");
        }
        if (problemConfig.getSize() <= 0) {
            throw new ConfigurationException("Error in configuration of test: " + toString() + ". Only positive " +
                    "problem sizes are possible, set value to the key \"problemSize\"");
        }
        if (algorithmConfig.getLambda() <= 0) {
            throw new ConfigurationException(
                    "Error in configuration of test: " + toString() + ". Only positive values of \"lambda\" are " +
                            "possible, set value to the key \"lambda\"");
        }
        if ((algorithmConfig.getLambda() == 1) && (algorithmConfig.getAlgorithmType() == OneStepAlgorithmsManager.AlgorithmType.TWO_RATE)) {
            throw new ConfigurationException("Error in configuration of test: " + toString() + ". \"lambda\" = 1 is " +
                    "not possible with TWO_RATE algorithm");
        }
        if (beginFitness > endFitness) {
            throw new ConfigurationException("Error in configuration of test: " + toString() + ". \"beginFitness\" " +
                    "could not be greater then \"endFitness\"");
        }
        probabilityEnumeration.validate();
    }
}
