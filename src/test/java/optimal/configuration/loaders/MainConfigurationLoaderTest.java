package optimal.configuration.loaders;

import optimal.configuration.MainConfiguration;
import optimal.configuration.transitionsGeneration.PrecomputedTransitionsReadingConfiguration;
import optimal.configuration.transitionsGeneration.TransitionsGenerationConfiguration;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.naming.ConfigurationException;
import java.io.IOException;

class MainConfigurationLoaderTest {
    @NotNull
    private MainConfiguration loadConfigFromTestFile(String testFileName) throws IOException,
            ConfigurationException {
        final MainConfigurationLoader loader =
                new MainConfigurationLoader("configuration/oneExperiment/" + testFileName);
        return loader.loadConfigurationFromResources();
    }

    @Test
    public void configurationOfRunTimeTransitionsGenerationTest() throws ConfigurationException, IOException {
        final MainConfiguration configuration = loadConfigFromTestFile("mainConfigRunTime.json");
        Assertions.assertEquals(TransitionsGenerationConfiguration.TransitionsGenerationStrategy.RUN_TIME_TRANSITIONS_GENERATION, configuration.getTransitionsGeneration().getStrategy());
    }

    @Test
    public void configurationOfPrecomputedTransitionsReadingTest() throws  ConfigurationException, IOException {
        final MainConfiguration configuration = loadConfigFromTestFile("mainConfigPrecomputedTransitions.json");
        final PrecomputedTransitionsReadingConfiguration transitionsGeneration = (PrecomputedTransitionsReadingConfiguration) configuration.getTransitionsGeneration();
        Assertions.assertEquals(TransitionsGenerationConfiguration.TransitionsGenerationStrategy.PRECOMPUTED_TRANSITIONS_READING, transitionsGeneration.getStrategy());
        Assertions.assertEquals("/a/b/c/vectors", transitionsGeneration.getDirectoryWithPrecomputedFilesPath());
    }
}