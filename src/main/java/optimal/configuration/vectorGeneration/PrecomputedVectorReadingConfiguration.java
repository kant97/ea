package optimal.configuration.vectorGeneration;

import org.jetbrains.annotations.NotNull;

import javax.naming.ConfigurationException;
import java.util.Objects;

public class PrecomputedVectorReadingConfiguration extends VectorGenerationConfiguration {
    private final String validationFileName;
    private final String precomputedVectorsDirName;

    public PrecomputedVectorReadingConfiguration(String validationFileName, String precomputedVectorsDirName) {
        this.validationFileName = validationFileName;
        this.precomputedVectorsDirName = precomputedVectorsDirName;
    }

    public String getValidationFileName() {
        return validationFileName;
    }

    public String getPrecomputedVectorsDirName() {
        return precomputedVectorsDirName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrecomputedVectorReadingConfiguration that = (PrecomputedVectorReadingConfiguration) o;
        return validationFileName.equals(that.validationFileName) &&
                precomputedVectorsDirName.equals(that.precomputedVectorsDirName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(validationFileName, precomputedVectorsDirName);
    }

    @Override
    public @NotNull VectorGenerationStrategy getStrategy() {
        return VectorGenerationStrategy.PRECOMPUTED_VECTOR_READING;
    }

    @Override
    public void validate() throws ConfigurationException {

    }
}
