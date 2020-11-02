package optimal.configuration.vectorGeneration;

import optimal.configuration.ValidatableConfiguration;
import org.jetbrains.annotations.NotNull;

public abstract class VectorGenerationConfiguration implements ValidatableConfiguration {
    public enum VectorGenerationStrategy {
        PRECOMPUTED_VECTOR_READING,
        RUN_TIME_VECTOR_GENERATION
    }

    public abstract @NotNull VectorGenerationStrategy getStrategy();
}
