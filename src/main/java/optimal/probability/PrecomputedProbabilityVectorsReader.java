package optimal.probability;

import optimal.configuration.OptimalMutationRateSearchingSingleExperimentConfiguration;
import optimal.configuration.vectorGeneration.PrecomputedVectorReadingConfiguration;
import optimal.execution.cluster.ConfigurationToNumberTranslator;
import optimal.utils.ProbabilityVectorProcessor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PrecomputedProbabilityVectorsReader implements ProbabilityVectorGenerator {
    private final OptimalMutationRateSearchingSingleExperimentConfiguration myConfiguration;
    private final ConfigurationToNumberTranslator myConfigurationToNumberTranslator;
    private final double myProbability;
    private final int myFitness;

    protected PrecomputedProbabilityVectorsReader(@NotNull OptimalMutationRateSearchingSingleExperimentConfiguration configuration,
                                                  @NotNull ConfigurationToNumberTranslator configurationToNumberTranslator,
                                                  double probability, int fitness) {
        this.myConfiguration = configuration;
        this.myConfigurationToNumberTranslator = configurationToNumberTranslator;
        this.myProbability = probability;
        this.myFitness = fitness;
    }

    @Override
    public @NotNull ArrayList<Double> getProbabilityVector() {
        int fileId = myConfigurationToNumberTranslator.translateFitnessAndMutationRateToNumber(myFitness,
                myProbability);
        final ProbabilityVectorProcessor probabilityVectorProcessor =
                new ProbabilityVectorProcessorImpl(getPrecomputedVectorsDir() + "/" + fileId + ".csv");
        probabilityVectorProcessor.loadData();
        return probabilityVectorProcessor.getProcessedData();
    }

    private String getPrecomputedVectorsDir() {
        return ((PrecomputedVectorReadingConfiguration) myConfiguration.getVectorGenerationConfig()).getPrecomputedVectorsDir();
    }

    private static final class ProbabilityVectorProcessorImpl extends ProbabilityVectorProcessor {

        public ProbabilityVectorProcessorImpl(@NotNull String csvFileName) {
            super(csvFileName);
        }

        @Override
        protected void onParseNumberError(Exception e, @NotNull List<String> record) {
            System.err.println("Failed to parse " + record + " for file " + csvFileName);
        }

        @Override
        protected boolean shouldContinue() {
            return true;
        }

        @Override
        protected void onEmptyRecords() {
            throw new IllegalStateException("Records are empty for file " + csvFileName);
        }
    }

}
