package optimal.optimal2;

import algo.Algorithm;
import optimal.configuration.OneExperimentConfiguration;
import optimal.configuration.runs.FixedRunsConfiguration;
import org.jetbrains.annotations.NotNull;

public class TransitionsGeneratorFixedRuns extends TransitionGenerationRunTime {
    private final int myAmountOfOneStepRepetitions;

    protected TransitionsGeneratorFixedRuns(@NotNull OneExperimentConfiguration configuration) {
        super(configuration);
        myAmountOfOneStepRepetitions = ((FixedRunsConfiguration) configuration.stopConditionConfig).getAmountOfRuns();
    }

    @Override
    protected boolean isStopConditionHit(int runNumber) {
        return runNumber >= myAmountOfOneStepRepetitions;
    }

    @Override
    protected void onIterationFinished(int beginFitness, int newFitness, @NotNull Algorithm algorithm) {
    }
}
