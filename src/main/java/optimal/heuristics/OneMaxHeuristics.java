package optimal.heuristics;

import optimal.execution.ResultEntity;
import org.jetbrains.annotations.NotNull;

public class OneMaxHeuristics extends Heuristics {
    private boolean isPrevResultInf = false;

    @Override
    public boolean isSupposedToBeInfOnThisExperiment() {
        if (prevExperimentState == null) {
            return false;
        }
        return (prevExperimentState.getProbability() < curExperimentState.getProbability()) &&
                (prevExperimentState.getFitness() == curExperimentState.getFitness()) && isPrevResultInf;
    }

    @Override
    public void acceptResult(@NotNull ResultEntity resultEntity) {
        isPrevResultInf = isTooBig(resultEntity);
    }

    public static boolean isTooBig(@NotNull ResultEntity resultEntity) {
        return isTooBig(resultEntity.optimizationTime);
    }

    public static boolean isTooBig(final double optimizationTime) {
        return Double.isInfinite(optimizationTime) || Math.abs(optimizationTime - Double.MAX_VALUE) < 0.00001;
    }
}
