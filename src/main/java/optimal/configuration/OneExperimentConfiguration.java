package optimal.configuration;

import optimal.configuration.probability.ExponentialGridConfiguration;
import optimal.configuration.probability.IterativeProbabilityConfiguration;
import optimal.configuration.probability.ProbabilitySamplingConfiguration;
import optimal.oneStepAlgorithms.OneStepAlgorithmsManager;
import optimal.probabilitySampling.ProbabilitySamplingStrategy;
import org.jetbrains.annotations.NotNull;
import problem.ProblemsManager;

import javax.naming.ConfigurationException;

public class OneExperimentConfiguration {
    public static final int DEFAULT_NUMBER_OF_ONE_STEP_REPETITIONS = 10000;
    public final ProblemsManager.ProblemType problemType;
    public final OneStepAlgorithmsManager.AlgorithmType algorithmType;
    private Integer numberOfStepRepetitions;
    public final int problemSize;
    public final int lambda;
    public final int beginFitness;
    public final int endFitness;
    public final ProbabilitySamplingStrategy probabilityEnumerationStrategy;
    private final ExponentialGridConfiguration exponentialGridProbabilityConfiguration;
    private final IterativeProbabilityConfiguration iterativeProbabilityConfiguration;

    public OneExperimentConfiguration(ProblemsManager.ProblemType problemType,
                                      OneStepAlgorithmsManager.AlgorithmType algorithmType,
                                      Integer numberOfStepRepetitions, int problemSize, int lambda,
                                      int beginFitness, int endFitness,
                                      ProbabilitySamplingStrategy probabilityEnumerationStrategy,
                                      ExponentialGridConfiguration exponentialGridProbabilityConfiguration,
                                      IterativeProbabilityConfiguration iterativeProbabilityConfiguration) {
        this.problemType = problemType;
        this.algorithmType = algorithmType;
        this.numberOfStepRepetitions = numberOfStepRepetitions;
        this.problemSize = problemSize;
        this.lambda = lambda;
        this.beginFitness = beginFitness;
        this.endFitness = endFitness;
        this.probabilityEnumerationStrategy = probabilityEnumerationStrategy;
        this.exponentialGridProbabilityConfiguration = exponentialGridProbabilityConfiguration;
        this.iterativeProbabilityConfiguration = iterativeProbabilityConfiguration;
    }

    public Integer getNumberOfStepRepetitions() {
        return numberOfStepRepetitions;
    }

    @NotNull
    public ProbabilitySamplingConfiguration getProbabilityEnumerationConfiguration() {
        if (probabilityEnumerationStrategy == ProbabilitySamplingStrategy.ITERATIVE) {
            return iterativeProbabilityConfiguration;
        } else if (probabilityEnumerationStrategy == ProbabilitySamplingStrategy.EXPONENTIAL_GRID) {
            return exponentialGridProbabilityConfiguration;
        }
        return iterativeProbabilityConfiguration; // default value
    }

    @Override
    public String toString() {
        return "OneExperimentConfiguration{" +
                "problemType=" + problemType +
                ", algorithmType=" + algorithmType +
                ", problemSize=" + problemSize +
                ", numberOfStepRepetitions=" + numberOfStepRepetitions +
                ", lambda=" + lambda +
                ", beginFitness=" + beginFitness +
                ", endFitness=" + endFitness +
                ", probabilitySamplingConfiguration=" + getProbabilityEnumerationConfiguration().toString() +
                '}';
    }

    private String toStringForError() {
        return "OneExperimentConfiguration{" +
                "problemType=" + problemType +
                ", algorithmType=" + algorithmType +
                ", problemSize=" + problemSize +
                ", numberOfStepRepetitions=" + numberOfStepRepetitions +
                ", lambda=" + lambda +
                ", beginFitness=" + beginFitness +
                ", endFitness=" + endFitness +
                ", probabilityEnumerationStrategy=" + mayBeNull(probabilityEnumerationStrategy) +
                '}';
    }

    private static <T> String mayBeNull(T strategy) {
        if (strategy == null) {
            return "null";
        }
        return strategy.toString();
    }

    public void validate() throws ConfigurationException {
        if (probabilityEnumerationStrategy == null) {
            throw new ConfigurationException("Error in configuration of test: " + toString() + ". Probability " +
                    "enumeration strategy is not chosen");
        }
        if (((probabilityEnumerationStrategy == ProbabilitySamplingStrategy.ITERATIVE) && (iterativeProbabilityConfiguration == null)) ||
                (probabilityEnumerationStrategy == ProbabilitySamplingStrategy.EXPONENTIAL_GRID) && (exponentialGridProbabilityConfiguration == null)) {
            throw new ConfigurationException("Error in configuration of test: " + toStringForError() + ". The " +
                    "probabilities " +
                    "enumerations strategy is " + mayBeNull(probabilityEnumerationStrategy) +
                    ", but the configuration for this strategy is not set");
        }
        if ((probabilityEnumerationStrategy != ProbabilitySamplingStrategy.ITERATIVE) &&
                (probabilityEnumerationStrategy != ProbabilitySamplingStrategy.EXPONENTIAL_GRID)) {
            throw new ConfigurationException("Error in configuration of test: " + toStringForError() + ". Probability" +
                    " " +
                    "enumeration strategy " + mayBeNull(probabilityEnumerationStrategy) +
                    " is not supported yet");
        }
        if (problemType == null) {
            throw new ConfigurationException("Problem type is not chosen, set value to the key \"problemType\"");
        }
        if (algorithmType == null) {
            throw new ConfigurationException("Algorithm type is not chosen for test " + toString() + ", set value to " +
                    "the key \"algorithmType\"");
        }
        if (numberOfStepRepetitions == null) {
            numberOfStepRepetitions = DEFAULT_NUMBER_OF_ONE_STEP_REPETITIONS;
            System.err.println("Number of repetitions of one step of the algorithm is not set for the test " + toString() +
                    ", so default value " + DEFAULT_NUMBER_OF_ONE_STEP_REPETITIONS + " is used");
        }
        if (problemSize <= 0) {
            throw new ConfigurationException("Error in configuration of test: " + toString() + ". Only positive " +
                    "problem sizes are possible, set value to the key \"problemSize\"");
        }
        if (lambda <= 0) {
            throw new ConfigurationException(
                    "Error in configuration of test: " + toString() + ". Only positive values of \"lambda\" are " +
                            "possible, set value to the key \"lambda\"");
        }
        if ((lambda == 1) && (algorithmType == OneStepAlgorithmsManager.AlgorithmType.TWO_RATE)) {
            throw new ConfigurationException("Error in configuration of test: " + toString() + ". \"lambda\" = 1 is " +
                    "not possible with TWO_RATE algorithm");
        }
        if (beginFitness > endFitness) {
            throw new ConfigurationException("Error in configuration of test: " + toString() + ". \"beginFitness\" " +
                    "could not be greater then \"endFitness\"");
        }
    }
}
