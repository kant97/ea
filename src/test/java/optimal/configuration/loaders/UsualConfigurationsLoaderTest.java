package optimal.configuration.loaders;

import optimal.configuration.OneExperimentConfiguration;
import optimal.configuration.UsualConfiguration;
import optimal.configuration.algorithms.TwoRateConfig;
import optimal.configuration.probability.ExponentialGridConfiguration;
import optimal.probabilitySampling.ProbabilitySamplingStrategy;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import problem.ProblemsManager;

import javax.naming.ConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import static optimal.oneStepAlgorithms.OneStepAlgorithmsManager.AlgorithmType.TWO_RATE;
import static org.mockito.Mockito.when;

class UsualConfigurationsLoaderTest {

    @Test
    void testBasicConfigurationLoading() throws Exception {
        UsualConfiguration configuration = loadConfigFromTestFile("configTest1.json");
        Assertions.assertEquals(10, configuration.amountOfThreads);
        Assertions.assertEquals(ProblemsManager.ProblemType.ONE_MAX_NEUTRALITY_3,
                configuration.experimentConfigurations.get(0).problemConfig.getProblemType());
        Assertions.assertEquals(TWO_RATE,
                configuration.experimentConfigurations.get(0).algorithmConfig.getAlgorithmType());
        Assertions.assertEquals(0.01,
                ((TwoRateConfig) configuration.experimentConfigurations.get(0).algorithmConfig).getLowerBound());
    }

    @Test
    void loadConfiguration() throws IOException, URISyntaxException, ConfigurationException {
        UsualConfiguration configuration = loadConfigFromTestFile("configTest5.json");
        OneExperimentConfiguration configuration1 = configuration.experimentConfigurations.get(0);
        Assertions.assertEquals(ProbabilitySamplingStrategy.EXPONENTIAL_GRID,
                configuration1.probabilityEnumeration.getStrategy());
        ExponentialGridConfiguration probabilityEnumerationConfiguration =
                (ExponentialGridConfiguration) configuration1.getProbabilityEnumerationConfiguration();
        Assertions.assertEquals(10, probabilityEnumerationConfiguration.base);
        Assertions.assertEquals(0.1, probabilityEnumerationConfiguration.precisionForPower);
        OneExperimentConfiguration configuration2 = configuration.experimentConfigurations.get(1);
        Assertions.assertTrue(configuration2.getProbabilityEnumerationConfiguration() instanceof ExponentialGridConfiguration);
        configuration = loadConfigFromTestFile("configTest6.json");
        OneExperimentConfiguration configuration3 = configuration.experimentConfigurations.get(0);
        Assertions.assertEquals(TWO_RATE, configuration3.algorithmConfig.getAlgorithmType());
    }

    @Test
    void loadBadConfiguration() {
        Assertions.assertThrows(ConfigurationException.class, () -> loadConfigFromTestFile("configTest4.json"));
        Assertions.assertThrows(FileNotFoundException.class, () -> loadConfigFromTestFile("configTestNOTFOUND.json"));
    }

    @NotNull
    private UsualConfiguration loadConfigFromTestFile(String testFileName) throws URISyntaxException,
            IOException, ConfigurationException {
        UsualConfigurationsLoader mockedConfigurationLoaderSpy = Mockito.spy(new UsualConfigurationsLoader());
        when(mockedConfigurationLoaderSpy.getConfigurationFilename()).thenReturn("configuration/" + testFileName);
        return mockedConfigurationLoaderSpy.loadConfigurationFromResources();
    }

}