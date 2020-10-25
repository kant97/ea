package optimal.configuration;

import optimal.configuration.probability.ExponentialGridConfiguration;
import optimal.probabilitySampling.ProbabilitySamplingStrategy;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import problem.ProblemsManager;

import javax.naming.ConfigurationException;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

import static optimal.oneStepAlgorithms.OneStepAlgorithmsManager.AlgorithmType.TWO_RATE;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

class ConfigurationsLoaderTest {

    private ConfigurationsLoader mockedConfigurationLoader;

    @Test
    void loadConfiguration() throws FileNotFoundException, URISyntaxException, ConfigurationException {
        Configuration configuration = loadConfigFromTestFile("configTest1.json");
        Assertions.assertEquals(10, configuration.amountOfThreads);
        Assertions.assertEquals(ProblemsManager.ProblemType.ONE_MAX_NEUTRALITY_3,
                configuration.experimentConfigurations.get(0).problemType);
        configuration = loadConfigFromTestFile("configTest5.json");
        OneExperimentConfiguration configuration1 = configuration.experimentConfigurations.get(0);
        Assertions.assertEquals(ProbabilitySamplingStrategy.EXPONENTIAL_GRID,
                configuration1.probabilityEnumerationStrategy);
        ExponentialGridConfiguration probabilityEnumerationConfiguration =
                (ExponentialGridConfiguration) configuration1.getProbabilityEnumerationConfiguration();
        Assertions.assertEquals(10, probabilityEnumerationConfiguration.base);
        Assertions.assertEquals(0.1, probabilityEnumerationConfiguration.precisionForPower);
        OneExperimentConfiguration configuration2 = configuration.experimentConfigurations.get(1);
        Assertions.assertTrue(configuration2.getProbabilityEnumerationConfiguration() instanceof ExponentialGridConfiguration);
        configuration = loadConfigFromTestFile("configTest6.json");
        OneExperimentConfiguration configuration3 = configuration.experimentConfigurations.get(0);
        Assertions.assertEquals(TWO_RATE, configuration3.algorithmType);
    }

    @Test
    void loadBadConfiguration() {
        Assertions.assertThrows(ConfigurationException.class, () -> loadConfigFromTestFile("configTest2.json"));
        Assertions.assertThrows(ConfigurationException.class, () -> loadConfigFromTestFile("configTest3.json"));
        Assertions.assertThrows(ConfigurationException.class, () -> loadConfigFromTestFile("configTest4.json"));
        Assertions.assertThrows(FileNotFoundException.class, () -> loadConfigFromTestFile("configTestNOTFOUND.json"));
        Assertions.assertThrows(ConfigurationException.class, () -> loadConfigFromTestFile("configTest7.json"));
        Assertions.assertThrows(ConfigurationException.class, () -> loadConfigFromTestFile("configTest8.json"));
    }

    @NotNull
    private Configuration loadConfigFromTestFile(String testFileName) throws URISyntaxException,
            FileNotFoundException, ConfigurationException {
        when(mockedConfigurationLoader.getConfigurationFilename()).thenReturn("configuration/" + testFileName);
        return mockedConfigurationLoader.loadConfigurationFromResources();
    }

    @BeforeEach
    void setUp() throws FileNotFoundException, ConfigurationException, URISyntaxException {
        mockedConfigurationLoader = Mockito.mock(ConfigurationsLoader.class);
        doCallRealMethod().when(mockedConfigurationLoader).loadConfigurationFromResources();
    }

}