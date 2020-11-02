package optimal.configuration.loaders;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import optimal.configuration.UsualConfiguration;
import optimal.configuration.algorithms.AlgorithmConfig;
import optimal.configuration.loaders.adapters.AlgorithmConfigurationAdapter;
import optimal.configuration.loaders.adapters.ProbabilitySamplingAdapter;
import optimal.configuration.loaders.adapters.ProblemConfigurationAdapter;
import optimal.configuration.loaders.adapters.StopConditionConfigurationAdapter;
import optimal.configuration.probability.ProbabilitySamplingConfiguration;
import optimal.configuration.problems.ProblemConfig;
import optimal.configuration.runs.StopConditionConfiguration;
import optimal.probabilitySampling.ProbabilitySamplingStrategy;
import org.jetbrains.annotations.NotNull;

public class UsualConfigurationsLoader extends ConfigurationsLoader<UsualConfiguration> {
    private static final String EXPERIMENTS_CONFIGURATION_JSON = "experimentsConfiguration.json";
    private final OneExperimentConfigurationLoader myOneExperimentConfigurationLoader;

    public UsualConfigurationsLoader() {
        myOneExperimentConfigurationLoader = new OneExperimentConfigurationLoader(EXPERIMENTS_CONFIGURATION_JSON);
    }

    @Override
    protected Class<UsualConfiguration> getConfigurationTypeClass() {
        return UsualConfiguration.class;
    }

    @Override
    protected @NotNull String getConfigurationFilename() {
        return EXPERIMENTS_CONFIGURATION_JSON;
    }

    @Override
    protected @NotNull Gson createGsonInstanceForDeserialization() {
        return myOneExperimentConfigurationLoader.createGsonInstanceForDeserialization();

    }

    @Override
    protected Gson createGsonInstanceForSerialization() {
        return myOneExperimentConfigurationLoader.createGsonInstanceForSerialization();
    }
}
