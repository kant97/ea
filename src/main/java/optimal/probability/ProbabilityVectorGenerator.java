package optimal.probability;

import optimal.oneStepAlgorithms.OneStepAlgorithm;
import optimal.oneStepAlgorithms.OneStepAlgorithmsManager;
import org.jetbrains.annotations.NotNull;
import problem.Problem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public abstract class ProbabilityVectorGenerator {

    private final double myProbability;
    private final int myLambda;
    private final double myLowerBound;
    protected final int myAmountOfOneStepRepetitions;
    private final Problem myProblem;
    private final OneStepAlgorithm myAlgorithm;

    public ProbabilityVectorGenerator(double probability, int n, int lambda, double lowerBound,
                                      int amountOfRepetitions,
                                      @NotNull Problem problem,
                                      @NotNull OneStepAlgorithmsManager.AlgorithmType algorithmType) {
        myProbability = probability;
        myLambda = lambda;
        myLowerBound = lowerBound;
        myAmountOfOneStepRepetitions = amountOfRepetitions;
        myProblem = problem;
        myAlgorithm = OneStepAlgorithmsManager.createAlgorithm(probability, lowerBound, lambda, problem, algorithmType);
    }

    @NotNull
    public ArrayList<Double> getProbabilityVector() {
        int beginFitness = myProblem.getFitness();

        HashMap<Integer, Integer> increaseToAmount = new HashMap<>();
        int curRunNumber = 0;
        for (; !isEnough(curRunNumber); curRunNumber++) {
            myAlgorithm.makeIteration();
            int newFitness = myAlgorithm.getFitness();
            int fitnessIncrease = newFitness - beginFitness;
            if (fitnessIncrease > 0) {
                onFitnessIncreased(fitnessIncrease);
            }
            increaseToAmount.computeIfPresent(fitnessIncrease, (k, v) -> v + 1);
            increaseToAmount.putIfAbsent(fitnessIncrease, 1);
            myAlgorithm.resetState();
        }

        int maxIncrease = Collections.max(increaseToAmount.keySet());
        ArrayList<Double> ans = new ArrayList<>(maxIncrease + 1);
        for (int i = 0; i <= maxIncrease; i++) {
            ans.add(0.);
        }
        final int amountOfRuns = curRunNumber;
        increaseToAmount.forEach((increase, amount) -> ans.set(increase, (double) (int) amount / (double) amountOfRuns));
        return ans;
    }

    protected abstract boolean isEnough(int runNumber);

    protected abstract void onFitnessIncreased(int increaseValue);
}
