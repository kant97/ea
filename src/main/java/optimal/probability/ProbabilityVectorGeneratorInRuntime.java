package optimal.probability;

import optimal.configuration.algorithms.AlgorithmConfig;
import optimal.configuration.runs.FixedRunsConfiguration;
import optimal.configuration.runs.FixedSuccessConfiguration;
import optimal.configuration.runs.StopConditionConfiguration;
import optimal.oneStepAlgorithms.OneStepAlgorithm;
import optimal.oneStepAlgorithms.OneStepAlgorithmsManager;
import org.jetbrains.annotations.NotNull;
import problem.Problem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public abstract class ProbabilityVectorGeneratorInRuntime implements ProbabilityVectorGenerator {

    protected final int myAmountOfOneStepRepetitions;
    private final Problem myProblem;
    private final OneStepAlgorithm myAlgorithm;

    protected ProbabilityVectorGeneratorInRuntime(double probability, int n, int lambda, double lowerBound,
                                                  int amountOfRepetitions,
                                                  @NotNull Problem problem,
                                                  @NotNull OneStepAlgorithmsManager.AlgorithmType algorithmType) {
        myAmountOfOneStepRepetitions = amountOfRepetitions;
        myProblem = problem;
        myAlgorithm = OneStepAlgorithmsManager.createAlgorithm(probability, lowerBound, lambda, problem, algorithmType);
    }

    protected ProbabilityVectorGeneratorInRuntime(double probability, @NotNull Problem problem,
                                                  @NotNull AlgorithmConfig algorithmConfig,
                                                  int amountOfOneStepRepetitions) {
        myAmountOfOneStepRepetitions = amountOfOneStepRepetitions;
        myProblem = problem;
        myAlgorithm = OneStepAlgorithmsManager.createAlgorithm(probability, algorithmConfig, problem);
    }

    protected static ProbabilityVectorGeneratorInRuntime createProbabilityVectorGeneratorByStrategy(double probability,
                                                                                                 @NotNull Problem problem,
                                                                                                 @NotNull AlgorithmConfig algorithmConfig,
                                                                                                 @NotNull StopConditionConfiguration stopConditionConfiguration) {
        if (stopConditionConfiguration.getMyStrategy() == StopConditionConfiguration.Strategy.FIXED_RUNS) {
            return new ProbabilityVectorGeneratorInRuntimeFixedRuns(probability, problem, algorithmConfig,
                    ((FixedRunsConfiguration) stopConditionConfiguration));
        } else if (stopConditionConfiguration.getMyStrategy() == StopConditionConfiguration.Strategy.FIXED_SUCCESS) {
            return new ProbabilityVectorGeneratorInRuntimeFixedSuccess(probability, problem,
                    algorithmConfig, ((FixedSuccessConfiguration) stopConditionConfiguration));
        }
        throw new IllegalArgumentException("Probability vector generator with strategy " + stopConditionConfiguration.getMyStrategy() + " is not supported");
    }

    @NotNull
    @Override
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
