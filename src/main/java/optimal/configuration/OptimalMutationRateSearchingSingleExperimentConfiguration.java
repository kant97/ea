package optimal.configuration;

import optimal.configuration.algorithms.AlgorithmConfig;
import optimal.configuration.probability.ProbabilitySamplingConfiguration;
import optimal.configuration.problems.ProblemConfig;
import optimal.configuration.runs.StopConditionConfiguration;
import optimal.configuration.vectorGeneration.RunTimeGenerationConfiguration;
import optimal.configuration.vectorGeneration.VectorGenerationConfiguration;
import org.jetbrains.annotations.Nullable;

import javax.naming.ConfigurationException;
import java.util.Objects;

public class OptimalMutationRateSearchingSingleExperimentConfiguration extends AbstractSingleExperimentConfiguration {
    private final VectorGenerationConfiguration vectorGenerationConfig;

    public OptimalMutationRateSearchingSingleExperimentConfiguration(ProblemConfig problemConfig,
                                                                     AlgorithmConfig algorithmConfig,
                                                                     int beginFitness, int endFitness,
                                                                     ProbabilitySamplingConfiguration probabilityEnumeration, VectorGenerationConfiguration vectorGenerationConfig) {
        super(problemConfig, algorithmConfig, beginFitness, endFitness, probabilityEnumeration);
        this.vectorGenerationConfig = vectorGenerationConfig;
    }

    public VectorGenerationConfiguration getVectorGenerationConfig() {
        return vectorGenerationConfig;
    }

    public @Nullable StopConditionConfiguration getStopConditionConfiguration() {
        if (vectorGenerationConfig.getStrategy() == VectorGenerationConfiguration.VectorGenerationStrategy.RUN_TIME_VECTOR_GENERATION) {
            return ((RunTimeGenerationConfiguration) vectorGenerationConfig).getStopConditionConfiguration();
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        OptimalMutationRateSearchingSingleExperimentConfiguration that =
                (OptimalMutationRateSearchingSingleExperimentConfiguration) o;
        return vectorGenerationConfig.equals(that.vectorGenerationConfig);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), vectorGenerationConfig);
    }

    @Override
    public void validate() throws ConfigurationException {
        super.validate();
        vectorGenerationConfig.validate();
    }
}
