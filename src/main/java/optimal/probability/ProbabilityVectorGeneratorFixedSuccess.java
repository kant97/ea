package optimal.probability;

import optimal.oneStepAlgorithms.OneStepAlgorithmsManager;
import org.jetbrains.annotations.NotNull;
import problem.Problem;

public class ProbabilityVectorGeneratorFixedSuccess extends ProbabilityVectorGenerator {
    private static final int GLOBAL_RUNS_MAXIMUM = 5 * (int) 1E6;
    private int myAmountOfFitnessIncreases = 0;

    public ProbabilityVectorGeneratorFixedSuccess(double probability, int n, int lambda, double lowerBound,
                                                  int amountOfRepetitions, @NotNull Problem problem,
                                                  @NotNull OneStepAlgorithmsManager.AlgorithmType algorithmType) {
        super(probability, n, lambda, lowerBound, amountOfRepetitions, problem, algorithmType);
    }

    @Override
    protected boolean isEnough(int runNumber) {
        if (runNumber >= GLOBAL_RUNS_MAXIMUM) {
            return true;
        }
        return myAmountOfFitnessIncreases > myAmountOfOneStepRepetitions;
    }

    @Override
    protected void onFitnessIncreased(int increaseValue) {
        myAmountOfFitnessIncreases++;
    }
}
