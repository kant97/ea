package optimal.configuration.loaders;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import optimal.configuration.ValidatableConfiguration;
import optimal.configuration.algorithms.AlgorithmConfig;
import optimal.configuration.loaders.adapters.AlgorithmConfigurationAdapter;
import optimal.configuration.loaders.adapters.ProblemConfigurationAdapter;
import optimal.configuration.loaders.adapters.StopConditionConfigurationAdapter;
import optimal.configuration.problems.ProblemConfig;
import optimal.configuration.runs.StopConditionConfiguration;
import org.jetbrains.annotations.NotNull;

import javax.naming.ConfigurationException;
import java.io.*;
import java.nio.charset.StandardCharsets;

public abstract class ConfigurationsLoader<T extends ValidatableConfiguration> {

    private volatile T CONFIGURATION = null;

    @NotNull
    protected T loadConfigurationFromResources() throws IOException, ConfigurationException {
        final InputStream resourceAsStream =
                getClass().getClassLoader().getResourceAsStream(getConfigurationFilename());
        assert resourceAsStream != null;
        T configuration = doLoadConfigurations(resourceAsStream);
        resourceAsStream.close();
        return configuration;
    }

    @NotNull
    protected T doLoadConfigurations(InputStream resourceAsStream) throws FileNotFoundException,
            ConfigurationException {
        if (resourceAsStream == null) {
            throw new FileNotFoundException("File with name " + getConfigurationFilename() + " is not found");
        }
        final Gson gson = createGsonInstanceForDeserialization();
        final T configuration = gson.fromJson(new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8),
                getConfigurationTypeClass());
        configuration.validate();
        return configuration;
    }

    @NotNull
    protected Gson createGsonInstanceForDeserialization() {
        return new GsonBuilder()
                .registerTypeAdapter(StopConditionConfiguration.class, new StopConditionConfigurationAdapter())
                .registerTypeAdapter(AlgorithmConfig.class, new AlgorithmConfigurationAdapter())
                .registerTypeAdapter(ProblemConfig.class, new ProblemConfigurationAdapter())
                .create();
    }

    protected abstract Class<T> getConfigurationTypeClass();

    protected T loadConfigurationFromFs() throws IOException, ConfigurationException {
        final InputStream inputStream = new FileInputStream(getConfigurationFilename());
        T configuration = doLoadConfigurations(inputStream);
        inputStream.close();
        return configuration;
    }

    @NotNull
    protected abstract String getConfigurationFilename();

    @NotNull
    public T getConfiguration() throws IOException, ConfigurationException {
        if (CONFIGURATION == null) {
            CONFIGURATION = loadConfigurationFromFs();
        }
        return CONFIGURATION;
    }

    @NotNull
    public T getConfigurationFromResources() throws IOException, ConfigurationException {
        if (CONFIGURATION == null) {
            CONFIGURATION = loadConfigurationFromResources();
        }
        return CONFIGURATION;
    }

    public String serializeConfiguration(T configuration) {
        final Gson gson = createGsonInstanceForSerialization();
        return gson.toJson(configuration);
    }

    protected Gson createGsonInstanceForSerialization() {
        return new GsonBuilder().create();
    }
}
