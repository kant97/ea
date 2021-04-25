package optimal.oneStepAlgorithms;

import algo.Algorithm;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import utils.BestCalculatedPatch;

public interface OneStepAlgorithm extends Algorithm {
    void resetState();
    @Nullable BestCalculatedPatch getMutatedIndividual();
}
