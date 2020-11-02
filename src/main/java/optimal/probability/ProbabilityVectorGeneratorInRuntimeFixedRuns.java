package optimal.probability;

import optimal.configuration.algorithms.AlgorithmConfig;
import optimal.configuration.runs.FixedRunsConfiguration;
import optimal.oneStepAlgorithms.OneStepAlgorithmsManager;
import org.jetbrains.annotations.NotNull;
import problem.Problem;

public class ProbabilityVectorGeneratorInRuntimeFixedRuns extends ProbabilityVectorGeneratorInRuntime {
    protected ProbabilityVectorGeneratorInRuntimeFixedRuns(double probability, int n, int lambda, double lowerBound,
                                                        int amountOfRepetitions, @NotNull Problem problem,
                                                        @NotNull OneStepAlgorithmsManager.AlgorithmType algorithmType) {
        super(probability, n, lambda, lowerBound, amountOfRepetitions, problem, algorithmType);
    }

    protected ProbabilityVectorGeneratorInRuntimeFixedRuns(double probability, @NotNull Problem problem,
                                                        @NotNull AlgorithmConfig algorithmConfig,
                                                        @NotNull FixedRunsConfiguration fixedRunsConfiguration) {
        super(probability, problem, algorithmConfig, fixedRunsConfiguration.getAmountOfRuns());
    }

    @Override
    protected boolean isEnough(int runNumber) {
        return runNumber >= myAmountOfOneStepRepetitions;
    }

    @Override
    protected void onFitnessIncreased(int increaseValue) {
    }
}
