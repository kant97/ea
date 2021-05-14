package optimal.configuration.loaders;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import optimal.configuration.MainConfiguration;
import optimal.configuration.algorithms.AlgorithmConfig;
import optimal.configuration.loaders.adapters.*;
import optimal.configuration.probability.ProbabilitySamplingConfiguration;
import optimal.configuration.problems.ProblemConfig;
import optimal.configuration.runs.StopConditionConfiguration;
import optimal.configuration.transitionsGeneration.TransitionsGenerationConfiguration;
import org.jetbrains.annotations.NotNull;

public class MainConfigurationLoader extends ConfigurationsLoader<MainConfiguration> {
    private final String myFileName;

    public MainConfigurationLoader(String myFileName) {
        this.myFileName = myFileName;
    }

    @Override
    protected Class<MainConfiguration> getConfigurationTypeClass() {
        return MainConfiguration.class;
    }

    @Override
    protected @NotNull String getConfigurationFilename() {
        return myFileName;
    }

    @Override
    protected @NotNull Gson createGsonInstanceForDeserialization() {
        return new GsonBuilder()
                .registerTypeAdapter(StopConditionConfiguration.class, new StopConditionConfigurationAdapter())
                .registerTypeAdapter(AlgorithmConfig.class, new AlgorithmConfigurationAdapter())
                .registerTypeAdapter(ProblemConfig.class, new ProblemConfigurationAdapter())
                .registerTypeAdapter(ProbabilitySamplingConfiguration.class, new ProbabilitySamplingAdapter())
                .registerTypeAdapter(TransitionsGenerationConfiguration.class, new TransitionsGenerationConfigurationAdapter())
                .create();
    }

    @Override
    protected Gson createGsonInstanceForSerialization() {
        return new GsonBuilder()
                .registerTypeAdapter(StopConditionConfiguration.class, new StopConditionConfigurationAdapter())
                .registerTypeAdapter(ProbabilitySamplingConfiguration.class, new ProbabilitySamplingAdapter())
                .registerTypeAdapter(TransitionsGenerationConfiguration.class, new TransitionsGenerationConfigurationAdapter())
                .create();
    }
}
