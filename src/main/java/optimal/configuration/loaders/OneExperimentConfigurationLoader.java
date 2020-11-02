package optimal.configuration.loaders;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import optimal.configuration.OneExperimentConfiguration;
import optimal.configuration.algorithms.AlgorithmConfig;
import optimal.configuration.loaders.adapters.AlgorithmConfigurationAdapter;
import optimal.configuration.loaders.adapters.ProbabilitySamplingAdapter;
import optimal.configuration.loaders.adapters.ProblemConfigurationAdapter;
import optimal.configuration.loaders.adapters.StopConditionConfigurationAdapter;
import optimal.configuration.probability.ProbabilitySamplingConfiguration;
import optimal.configuration.problems.ProblemConfig;
import optimal.configuration.runs.StopConditionConfiguration;
import org.jetbrains.annotations.NotNull;

public class OneExperimentConfigurationLoader extends ConfigurationsLoader<OneExperimentConfiguration> {
    private final String myFileName;

    public OneExperimentConfigurationLoader(@NotNull String myFileName) {
        this.myFileName = myFileName;
    }

    @Override
    protected Class<OneExperimentConfiguration> getConfigurationTypeClass() {
        return OneExperimentConfiguration.class;
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
                .create();

    }

    @Override
    protected Gson createGsonInstanceForSerialization() {
        return new GsonBuilder()
                .registerTypeAdapter(StopConditionConfiguration.class, new StopConditionConfigurationAdapter())
                .registerTypeAdapter(ProbabilitySamplingConfiguration.class, new ProbabilitySamplingAdapter())
                .create();
    }
}
