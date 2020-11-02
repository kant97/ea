package optimal.configuration.loaders;

import optimal.configuration.OptimalMutationRateSearchingSingleExperimentConfiguration;
import optimal.configuration.vectorGeneration.RunTimeGenerationConfiguration;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.naming.ConfigurationException;
import java.io.FileNotFoundException;

import static optimal.configuration.runs.StopConditionConfiguration.Strategy.FIXED_SUCCESS;
import static optimal.configuration.vectorGeneration.VectorGenerationConfiguration.VectorGenerationStrategy.RUN_TIME_VECTOR_GENERATION;

class OptimalMutationRateSearchingSingleExperimentConfigurationLoaderTest {

    @Test
    void testBasicConfigsLoading() throws Exception {
        final OptimalMutationRateSearchingSingleExperimentConfiguration optimalMutationRateSearchingSingleExperimentConfiguration = loadConfigFromTestFile("1.json");
        Assertions.assertEquals(RUN_TIME_VECTOR_GENERATION,
                optimalMutationRateSearchingSingleExperimentConfiguration.getVectorGenerationConfig().getStrategy());
        Assertions.assertEquals(FIXED_SUCCESS,
                ((RunTimeGenerationConfiguration) optimalMutationRateSearchingSingleExperimentConfiguration.getVectorGenerationConfig())
                        .getStopConditionConfiguration()
                        .getMyStrategy());
    }

    @NotNull
    private OptimalMutationRateSearchingSingleExperimentConfiguration loadConfigFromTestFile(String testFileName) throws FileNotFoundException,
            ConfigurationException {
        final OptimalMutationRateSearchingSingleExperimentConfigurationLoader mockedConfigurationLoaderSpy =
                new OptimalMutationRateSearchingSingleExperimentConfigurationLoader("configuration/optimalMRSearching" +
                        "/" + testFileName);
        return mockedConfigurationLoaderSpy.loadConfigurationFromResources();
    }

}