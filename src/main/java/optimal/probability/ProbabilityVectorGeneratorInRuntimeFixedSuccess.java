package optimal.probability;

import optimal.configuration.algorithms.AlgorithmConfig;
import optimal.configuration.runs.FixedSuccessConfiguration;
import optimal.oneStepAlgorithms.OneStepAlgorithmsManager;
import org.jetbrains.annotations.NotNull;
import problem.Problem;

public class ProbabilityVectorGeneratorInRuntimeFixedSuccess extends ProbabilityVectorGeneratorInRuntime {
    private final int myGlobalRunsMaximum;
    private int myAmountOfFitnessIncreases = 0;

    protected ProbabilityVectorGeneratorInRuntimeFixedSuccess(double probability, int n, int lambda, double lowerBound,
                                                           int amountOfRepetitions, @NotNull Problem problem,
                                                           @NotNull OneStepAlgorithmsManager.AlgorithmType algorithmType) {
        super(probability, n, lambda, lowerBound, amountOfRepetitions, problem, algorithmType);
        myGlobalRunsMaximum = 5 * (int) 1E6;
    }

    protected ProbabilityVectorGeneratorInRuntimeFixedSuccess(double probability, @NotNull Problem problem,
                                                           @NotNull AlgorithmConfig algorithmConfig,
                                                           @NotNull FixedSuccessConfiguration fixedSuccessConfiguration) {
        super(probability, problem, algorithmConfig, fixedSuccessConfiguration.getAmountOfSuccess());
        myGlobalRunsMaximum = fixedSuccessConfiguration.getGlobalMaximumRuns();
    }

    @Override
    protected boolean isEnough(int runNumber) {
        if (runNumber >= myGlobalRunsMaximum) {
            return true;
        }
        return myAmountOfFitnessIncreases > myAmountOfOneStepRepetitions;
    }

    @Override
    protected void onFitnessIncreased(int increaseValue) {
        myAmountOfFitnessIncreases++;
    }
}
