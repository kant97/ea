package optimal.optimal2.generation;

import algo.Algorithm;
import optimal.configuration.algorithms.AlgorithmConfig;
import optimal.configuration.problems.ProblemConfig;
import optimal.configuration.runs.FixedRunsConfiguration;
import optimal.configuration.runs.StopConditionConfiguration;
import optimal.optimal2.TransitionGenerationRunTime;
import org.jetbrains.annotations.NotNull;

public class TransitionsGeneratorFixedRuns extends TransitionGenerationRunTime {
    private final int myAmountOfOneStepRepetitions;

    public TransitionsGeneratorFixedRuns(@NotNull StopConditionConfiguration stopConditionConfiguration, @NotNull ProblemConfig problemConfig, @NotNull AlgorithmConfig algorithmConfig) {
        super(stopConditionConfiguration, problemConfig, algorithmConfig);
        myAmountOfOneStepRepetitions = ((FixedRunsConfiguration) stopConditionConfiguration).getAmountOfRuns();
    }

    @Override
    protected boolean isStopConditionHit(int runNumber) {
        return runNumber >= myAmountOfOneStepRepetitions;
    }

    @Override
    protected void onIterationFinished(int beginFitness, int newFitness, @NotNull Algorithm algorithm) {
    }
}
