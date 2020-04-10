package optimal.configuration;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import javax.naming.ConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class ConfigurationsLoader {

    private static volatile Configuration CONFIGURATION = null;

    @NotNull
    Configuration loadConfiguration() throws URISyntaxException, FileNotFoundException, ConfigurationException {
        File jsonFile;
        URL resource = getClass().getClassLoader().getResource(getConfigurationFilename());
        if (resource == null) {
            throw new FileNotFoundException("File with name " + getConfigurationFilename() + " is not found");
        }
        jsonFile = Paths.get(resource.toURI()).toFile();
        Gson gson = new Gson();
        Configuration configuration = gson.fromJson(new FileReader(jsonFile), Configuration.class);
        configuration.validate();
        return configuration;
    }

    @NotNull
    String getConfigurationFilename() {
        return "experimentsConfiguration.json";
    }

    @NotNull
    public Configuration getConfiguration() throws FileNotFoundException, URISyntaxException, ConfigurationException {
        if (CONFIGURATION == null) {
            CONFIGURATION = loadConfiguration();
        }
        return CONFIGURATION;
    }
}
