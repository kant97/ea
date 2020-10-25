package optimal.configuration;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import javax.naming.ConfigurationException;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class ConfigurationsLoader {

    private static volatile Configuration CONFIGURATION = null;

    @NotNull
    Configuration loadConfigurationFromResources() throws FileNotFoundException, ConfigurationException {
        final InputStream resourceAsStream =
                getClass().getClassLoader().getResourceAsStream(getConfigurationFilename());
        return doLoadConfigurations(resourceAsStream);
    }

    @NotNull
    private Configuration doLoadConfigurations(InputStream resourceAsStream) throws FileNotFoundException, ConfigurationException {
        if (resourceAsStream == null) {
            throw new FileNotFoundException("File with name " + getConfigurationFilename() + " is not found");
        }
        final Gson gson = new Gson();
        Configuration configuration = gson.fromJson(new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8),
                Configuration.class);
        configuration.validate();
        return configuration;
    }

    Configuration loadConfigurationFromFs() throws FileNotFoundException, ConfigurationException {
        final InputStream inputStream = new FileInputStream(new File(getConfigurationFilename()));
        return doLoadConfigurations(inputStream);
    }

    @NotNull
    String getConfigurationFilename() {
        return "experimentsConfiguration.json";
    }

    @NotNull
    public Configuration getConfiguration() throws FileNotFoundException, ConfigurationException {
        if (CONFIGURATION == null) {
            CONFIGURATION = loadConfigurationFromFs();
        }
        return CONFIGURATION;
    }
}
