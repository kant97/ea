package optimal.probability;

import optimal.oneStepAlgorithms.OneStepAlgorithmsManager;
import org.jetbrains.annotations.NotNull;
import problem.Problem;

public class ProbabilityVectorGeneratorFixedRuns extends ProbabilityVectorGenerator {
    public ProbabilityVectorGeneratorFixedRuns(double probability, int n, int lambda, double lowerBound,
                                               int amountOfRepetitions, @NotNull Problem problem,
                                               @NotNull OneStepAlgorithmsManager.AlgorithmType algorithmType) {
        super(probability, n, lambda, lowerBound, amountOfRepetitions, problem, algorithmType);
    }

    @Override
    protected boolean isEnough(int runNumber) {
        return runNumber >= myAmountOfOneStepRepetitions;
    }

    @Override
    protected void onFitnessIncreased(int increaseValue) {}
}
