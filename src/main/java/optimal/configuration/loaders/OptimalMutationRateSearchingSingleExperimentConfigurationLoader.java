package optimal.configuration.loaders;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import optimal.configuration.OptimalMutationRateSearchingSingleExperimentConfiguration;
import optimal.configuration.algorithms.AlgorithmConfig;
import optimal.configuration.loaders.adapters.*;
import optimal.configuration.probability.ProbabilitySamplingConfiguration;
import optimal.configuration.problems.ProblemConfig;
import optimal.configuration.runs.StopConditionConfiguration;
import optimal.configuration.vectorGeneration.VectorGenerationConfiguration;
import org.jetbrains.annotations.NotNull;

public class OptimalMutationRateSearchingSingleExperimentConfigurationLoader extends ConfigurationsLoader<OptimalMutationRateSearchingSingleExperimentConfiguration> {
    private final String myConfigFileName;

    public OptimalMutationRateSearchingSingleExperimentConfigurationLoader(@NotNull String myConfigFileName) {
        this.myConfigFileName = myConfigFileName;
    }

    @Override
    protected Class<OptimalMutationRateSearchingSingleExperimentConfiguration> getConfigurationTypeClass() {
        return OptimalMutationRateSearchingSingleExperimentConfiguration.class;
    }

    @Override
    protected @NotNull String getConfigurationFilename() {
        return myConfigFileName;
    }

    @Override
    protected @NotNull Gson createGsonInstanceForDeserialization() {
        return new GsonBuilder()
                .registerTypeAdapter(StopConditionConfiguration.class, new StopConditionConfigurationAdapter())
                .registerTypeAdapter(AlgorithmConfig.class, new AlgorithmConfigurationAdapter())
                .registerTypeAdapter(ProblemConfig.class, new ProblemConfigurationAdapter())
                .registerTypeAdapter(ProbabilitySamplingConfiguration.class, new ProbabilitySamplingAdapter())
                .registerTypeAdapter(VectorGenerationConfiguration.class, new VectorGenerationConfigurationAdapter())
                .create();
    }

    @Override
    protected Gson createGsonInstanceForSerialization() {
        return new GsonBuilder()
                .registerTypeAdapter(StopConditionConfiguration.class, new StopConditionConfigurationAdapter())
                .registerTypeAdapter(ProbabilitySamplingConfiguration.class, new ProbabilitySamplingAdapter())
                .registerTypeAdapter(VectorGenerationConfiguration.class, new VectorGenerationConfigurationAdapter())
                .create();
    }
}
