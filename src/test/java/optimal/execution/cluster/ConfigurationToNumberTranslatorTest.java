package optimal.execution.cluster;

import optimal.configuration.OneExperimentConfiguration;
import optimal.configuration.loaders.OneExperimentConfigurationLoader;
import optimal.probabilitySampling.ProbabilitySearcher;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;

import javax.naming.ConfigurationException;
import java.io.IOException;
import java.util.Random;
import java.util.function.BiConsumer;

class ConfigurationToNumberTranslatorTest {

    @RepeatedTest(50)
    public void testTranslationToNumber() throws IOException, ConfigurationException {
        final OneExperimentConfiguration configuration = getExperimentConfiguration("basicConfig.json");
        final ConfigurationToNumberTranslator configurationToNumberTranslator =
                new ConfigurationToNumberTranslator(configuration);
        int num = 50;
        for (int fitness = configuration.endFitness - 1; fitness >= configuration.beginFitness; fitness--) {
            final ProbabilitySearcher probabilitySearcher =
                    ProbabilitySearcher.createProbabilitySearcher(configuration.probabilityEnumeration);
            for (double p = probabilitySearcher.getInitialProbability(); !probabilitySearcher.isFinished(); p =
                    probabilitySearcher.getNextProb()) {
                final int actualNumber =
                        configurationToNumberTranslator.translateFitnessAndMutationRateToNumber(fitness, p);
                Assertions.assertEquals(num, actualNumber);
                num++;
            }
        }
        Assertions.assertEquals(2550, num);
    }

    @RepeatedTest(1000)
    public void testTranslationToNumberWithShiftOfProbability() throws IOException,
            ConfigurationException {
        final OneExperimentConfiguration configuration = getExperimentConfiguration("basicConfig.json");
        final ConfigurationToNumberTranslator translator = new ConfigurationToNumberTranslator(configuration);
        final double eps = 0.00001;
        final Integer[] numRef = new Integer[]{50};
        final Random r = new Random();
        iterateFitnessAndProbability(configuration, (fitness, p) -> {
            final double randomShift = eps + (0.0005 - eps) * r.nextDouble();
            final boolean randomSign = r.nextBoolean();
            double mult = 1;
            if (!randomSign) {
                mult = -1;
            }
            final int actualNumber = translator.translateFitnessAndMutationRateToNumber(fitness,
                    p + mult * randomShift);
            Assertions.assertEquals(numRef[0], actualNumber);
            numRef[0]++;
        });
    }

    private void iterateFitnessAndProbability(@NotNull OneExperimentConfiguration configuration,
                                              @NotNull BiConsumer<Integer, Double> consumer) {
        for (int fitness = configuration.endFitness - 1; fitness >= configuration.beginFitness; fitness--) {
            final ProbabilitySearcher probabilitySearcher =
                    ProbabilitySearcher.createProbabilitySearcher(configuration.probabilityEnumeration);
            for (double p = probabilitySearcher.getInitialProbability(); !probabilitySearcher.isFinished(); p =
                    probabilitySearcher.getNextProb()) {
                consumer.accept(fitness, p);
            }
        }
    }

    @NotNull
    private OneExperimentConfiguration getExperimentConfiguration(@NotNull String fileName) throws IOException,
            ConfigurationException {
        final OneExperimentConfigurationLoader loader = new OneExperimentConfigurationLoader("configuration/cluster" +
                "/translationToNumber/" + fileName);
        final OneExperimentConfiguration configuration = loader.getConfigurationFromResources();
        return configuration;
    }

}