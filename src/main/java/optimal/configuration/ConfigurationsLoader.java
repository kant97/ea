package optimal.configuration;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class ConfigurationsLoader {

    private static volatile Configuration configuration = null;

    @NotNull
    Configuration loadConfiguration() throws URISyntaxException, FileNotFoundException {
        File jsonFile;
        URL resource = ClassLoader.getSystemResource("experimentsConfiguration.json");
        jsonFile = Paths.get(resource.toURI()).toFile();
        Gson gson = new Gson();
        return gson.fromJson(new FileReader(jsonFile), Configuration.class);
    }

    @NotNull
    public Configuration getConfiguration() throws FileNotFoundException, URISyntaxException {
        if (configuration == null) {
            configuration = loadConfiguration();
        }
        return configuration;
    }
}
