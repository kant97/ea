package optimal.configuration.loaders;

import optimal.configuration.ProbabilityVectorGenerationConfiguration;
import optimal.configuration.problems.PlateauConfig;
import optimal.configuration.problems.ProblemConfig;
import optimal.configuration.problems.RuggednessConfig;
import optimal.configuration.runs.FixedRunsConfiguration;
import optimal.configuration.runs.StopConditionConfiguration;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import problem.ProblemsManager;

import javax.naming.ConfigurationException;
import java.io.*;

class ProbabilityVectorGenerationConfigurationLoaderTest {

    @Test
    public void testConfigurationOfFixedSuccessStrategy() throws Exception {
        final ProbabilityVectorGenerationConfiguration configuration = loadConfigFromTestFile("1.json");
        Assertions.assertEquals(StopConditionConfiguration.Strategy.FIXED_SUCCESS,
                configuration.getStopConditionConfig().getMyStrategy());
    }

    @Test
    public void testConfigurationOfFixedRunsStrategy() throws Exception {
        final ProbabilityVectorGenerationConfiguration configuration = loadConfigFromTestFile("2.json");
        Assertions.assertEquals(StopConditionConfiguration.Strategy.FIXED_RUNS,
                configuration.getStopConditionConfig().getMyStrategy());
        Assertions.assertEquals(7000,
                ((FixedRunsConfiguration) configuration.getStopConditionConfig()).getAmountOfRuns());
    }

    @Test
    public void testConfigurationOfRuggedness() throws Exception {
        final ProbabilityVectorGenerationConfiguration configuration = loadConfigFromTestFile("3.json");
        final ProblemConfig problemConfig = configuration.getProblemConfig();
        Assertions.assertEquals(ProblemsManager.ProblemType.ONE_MAX_RUGGEDNESS, problemConfig.getProblemType());
        final RuggednessConfig config = (RuggednessConfig) problemConfig;
        Assertions.assertEquals(2, config.getR());
    }

    @Test
    public void testConfigurationOfPlateau() throws Exception {
        final ProbabilityVectorGenerationConfiguration configuration = loadConfigFromTestFile("5.json");
        final ProblemConfig problemConfig = configuration.getProblemConfig();
        Assertions.assertEquals(ProblemsManager.ProblemType.ONE_MAX_PLATEAU, problemConfig.getProblemType());
        final PlateauConfig config = (PlateauConfig) problemConfig;
        Assertions.assertEquals(3, config.getK());
    }

    @Test
    public void testConfigurationOfOutputFileName() throws Exception {
        final ProbabilityVectorGenerationConfiguration configuration = loadConfigFromTestFile("4.json");
        Assertions.assertEquals("output", configuration.getOutputFileName());
    }

    @Test
    public void testDeserializationOfSerializedConfigs() throws Exception {
        final ProbabilityVectorGenerationConfiguration configuration = loadConfigFromTestFile("3.json");
        final ProbabilityVectorGenerationConfigurationLoader loader =
                new ProbabilityVectorGenerationConfigurationLoader("ignore");
        final String s = loader.serializeConfiguration(configuration);
        File file = File.createTempFile( "some-prefix", "some-ext");
        file.deleteOnExit();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(s);
        writer.close();
        final ProbabilityVectorGenerationConfiguration deserializedConfigurations =
                loader.doLoadConfigurations(new FileInputStream(file));
        Assertions.assertEquals(configuration.toString(), deserializedConfigurations.toString());
    }

    @NotNull
    private ProbabilityVectorGenerationConfiguration loadConfigFromTestFile(String testFileName) throws IOException,
            ConfigurationException {
        final ProbabilityVectorGenerationConfigurationLoader probabilityVectorGenerationConfigurationLoader =
                new ProbabilityVectorGenerationConfigurationLoader(
                "configuration/cluster/" + testFileName);
        return probabilityVectorGenerationConfigurationLoader.loadConfigurationFromResources();
    }
}