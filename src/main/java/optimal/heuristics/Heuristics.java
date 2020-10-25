package optimal.heuristics;

import optimal.execution.ResultEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import problem.ProblemsManager;

public abstract class Heuristics {
    protected ExperimentState prevExperimentState;
    protected ExperimentState curExperimentState;

    public abstract boolean isSupposedToBeInfOnThisExperiment();

    public abstract void acceptResult(@NotNull ResultEntity resultEntity);

    public void acceptNewExperimentState(@NotNull ExperimentState experimentState) {
        prevExperimentState = curExperimentState;
        curExperimentState = experimentState;
    }

    @Nullable
    public static Heuristics createHeuristics(ProblemsManager.ProblemType problemType) {
        if (problemType == ProblemsManager.ProblemType.ONE_MAX) {
            return new OneMaxHeuristics();
        }
        return null;
    }
}
