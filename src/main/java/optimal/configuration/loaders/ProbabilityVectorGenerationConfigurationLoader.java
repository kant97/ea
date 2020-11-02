package optimal.configuration.loaders;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import optimal.configuration.ProbabilityVectorGenerationConfiguration;
import optimal.configuration.algorithms.AlgorithmConfig;
import optimal.configuration.loaders.adapters.AlgorithmConfigurationAdapter;
import optimal.configuration.loaders.adapters.ProblemConfigurationAdapter;
import optimal.configuration.loaders.adapters.StopConditionConfigurationAdapter;
import optimal.configuration.problems.ProblemConfig;
import optimal.configuration.runs.StopConditionConfiguration;
import org.jetbrains.annotations.NotNull;

public class ProbabilityVectorGenerationConfigurationLoader extends ConfigurationsLoader<ProbabilityVectorGenerationConfiguration> {
    private final String myFileName;

    public ProbabilityVectorGenerationConfigurationLoader(String myFileName) {
        this.myFileName = myFileName;
    }

    @Override
    protected Class<ProbabilityVectorGenerationConfiguration> getConfigurationTypeClass() {
        return ProbabilityVectorGenerationConfiguration.class;
    }

    @Override
    @NotNull
    protected String getConfigurationFilename() {
        return myFileName;
    }

    @Override
    protected Gson createGsonInstanceForSerialization() {
        return new GsonBuilder()
                .registerTypeAdapter(StopConditionConfiguration.class, new StopConditionConfigurationAdapter())
                .create();
    }

    @Override
    protected @NotNull Gson createGsonInstanceForDeserialization() {
        return new GsonBuilder()
                .registerTypeAdapter(StopConditionConfiguration.class, new StopConditionConfigurationAdapter())
                .registerTypeAdapter(AlgorithmConfig.class, new AlgorithmConfigurationAdapter())
                .registerTypeAdapter(ProblemConfig.class, new ProblemConfigurationAdapter())
                .create();
    }
}
