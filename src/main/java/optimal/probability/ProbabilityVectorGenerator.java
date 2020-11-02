package optimal.probability;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public interface ProbabilityVectorGenerator {
    @NotNull ArrayList<Double> getProbabilityVector();
}
