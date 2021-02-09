package optimal.configuration.loaders;

import optimal.configuration.OneExperimentConfiguration;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.naming.ConfigurationException;
import java.io.*;

class OneExperimentConfigurationLoaderTest {

    @Test
    void testDeserializationOfSerializedConfigs() throws Exception {
        final OneExperimentConfiguration configuration = loadConfigFromTestFile("1.json");
        final OneExperimentConfigurationLoader loader =
                new OneExperimentConfigurationLoader("ignore");
        final String s = loader.serializeConfiguration(configuration);
        System.out.println(s);
        File file = File.createTempFile("some-prefix", "some-ext");
        file.deleteOnExit();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(s);
        writer.close();
        final OneExperimentConfiguration deserializedConfigurations =
                loader.doLoadConfigurations(new FileInputStream(file));
        Assertions.assertEquals(configuration.toString(), deserializedConfigurations.toString());
    }

    @NotNull
    private OneExperimentConfiguration loadConfigFromTestFile(String testFileName) throws IOException,
            ConfigurationException {
        final OneExperimentConfigurationLoader mockedConfigurationLoaderSpy =
                new OneExperimentConfigurationLoader("configuration/oneExperiment/" + testFileName);
        return mockedConfigurationLoaderSpy.loadConfigurationFromResources();
    }

}