package optimal.configuration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import problem.ProblemsManager;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;

class ConfigurationsLoaderTest {

    @Test
    void loadConfiguration() throws FileNotFoundException, URISyntaxException {
        Configuration configuration = new ConfigurationsLoader().loadConfiguration();
        Assertions.assertEquals(10, configuration.amountOfThreads);
        Assertions.assertEquals(ProblemsManager.ProblemType.ONE_MAX_NEUTRALITY_3,
                configuration.experimentConfigurations.get(0).problemType);
    }
}