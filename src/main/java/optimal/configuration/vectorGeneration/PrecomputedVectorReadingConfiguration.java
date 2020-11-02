package optimal.configuration.vectorGeneration;

import optimal.configuration.ConfigurationVisitor;
import org.jetbrains.annotations.NotNull;

import javax.naming.ConfigurationException;
import java.util.Objects;

public class PrecomputedVectorReadingConfiguration extends VectorGenerationConfiguration {
    private final String validationFileName;
    private final String precomputedVectorsDir; // ends on /

    public PrecomputedVectorReadingConfiguration(String validationFileName, String precomputedVectorsDir) {
        this.validationFileName = validationFileName;
        this.precomputedVectorsDir = precomputedVectorsDir;
    }

    public String getValidationFileName() {
        return validationFileName;
    }

    public String getPrecomputedVectorsDir() {
        return precomputedVectorsDir;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrecomputedVectorReadingConfiguration that = (PrecomputedVectorReadingConfiguration) o;
        return validationFileName.equals(that.validationFileName) &&
                precomputedVectorsDir.equals(that.precomputedVectorsDir);
    }

    @Override
    public int hashCode() {
        return Objects.hash(validationFileName, precomputedVectorsDir);
    }

    @Override
    public @NotNull VectorGenerationStrategy getStrategy() {
        return VectorGenerationStrategy.PRECOMPUTED_VECTOR_READING;
    }

    @Override
    public void validate() throws ConfigurationException {

    }

    @Override
    public @NotNull String accept(@NotNull ConfigurationVisitor visitor) {
        return visitor.visitPrecomputedVectorReadingConfig(this);
    }
}
