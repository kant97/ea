package optimal;

import algo.OneStepTwoRate;
import org.jetbrains.annotations.NotNull;
import problem.Problem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ProbabilityVectorGenerator {
    public static final int AMOUNT_OF_RUNS = 10000;

    private final double myProbability;
    private final int myLambda;
    private final double myLowerBound;
    private final Problem myProblem;
    private final OneStepTwoRate myAlgorithm;

    public ProbabilityVectorGenerator(double probability, int n, int lambda, double lowerBound,
                                      @NotNull Problem problem) {
        myProbability = probability;
        myLambda = lambda;
        myLowerBound = lowerBound;
        myProblem = problem;
        myAlgorithm = new OneStepTwoRate(myProbability, myLowerBound, myLambda, myProblem);
    }

    @NotNull
    public ArrayList<Double> getProbabilityVector() {
        int beginFitness = myProblem.getFitness();

        HashMap<Integer, Integer> increaseToAmount = new HashMap<>();

        for (int i = 0; i < AMOUNT_OF_RUNS; i++) {
            myAlgorithm.makeIteration();
            int newFitness = myAlgorithm.getFitness();
            int fitnessIncrease = newFitness - beginFitness;
            increaseToAmount.computeIfPresent(fitnessIncrease, (k, v) -> v + 1);
            increaseToAmount.putIfAbsent(fitnessIncrease, 1);
            myAlgorithm.resetState();
        }

        int maxIncrease = Collections.max(increaseToAmount.keySet());
        ArrayList<Double> ans = new ArrayList<>(maxIncrease + 1);
        for (int i = 0; i <= maxIncrease; i++) {
            ans.add(0.);
        }
        increaseToAmount.forEach((increase, amount) -> ans.set(increase, (double) (int) amount / (double) AMOUNT_OF_RUNS));
        return ans;
    }
}
