package optimal.optimal2.generation;

import algo.Algorithm;
import optimal.configuration.algorithms.AlgorithmConfig;
import optimal.configuration.problems.ProblemConfig;
import optimal.configuration.runs.FixedSuccessConfiguration;
import optimal.configuration.runs.StopConditionConfiguration;
import optimal.optimal2.TransitionGenerationRunTime;
import org.jetbrains.annotations.NotNull;

public class TransitionGenerationFixedSuccess extends TransitionGenerationRunTime {
    private final int globalRunsMaximum;
    private final int numberOfOneStepRepetitions;
    private int numberOfIncreases = 0;

    protected TransitionGenerationFixedSuccess(@NotNull StopConditionConfiguration stopConditionConfiguration, @NotNull ProblemConfig problemConfig, @NotNull AlgorithmConfig algorithmConfig) {
        super(stopConditionConfiguration, problemConfig, algorithmConfig);
        final FixedSuccessConfiguration fixedSuccessConfiguration = (FixedSuccessConfiguration) stopConditionConfiguration;
        globalRunsMaximum = fixedSuccessConfiguration.getGlobalMaximumRuns();
        numberOfOneStepRepetitions = fixedSuccessConfiguration.getAmountOfSuccess();
    }

    @Override
    protected boolean isStopConditionHit(int runNumber) {
        if (runNumber >= globalRunsMaximum) {
            return true;
        }
        return numberOfIncreases > numberOfOneStepRepetitions;
    }

    @Override
    protected void onIterationFinished(int beginFitness, int newFitness, @NotNull Algorithm algorithm) {
        if (newFitness > beginFitness) {
            numberOfIncreases++;
        }
    }
}
