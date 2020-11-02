package optimal.probability;

import optimal.configuration.OptimalMutationRateSearchingSingleExperimentConfiguration;
import optimal.configuration.vectorGeneration.PrecomputedVectorReadingConfiguration;
import optimal.execution.cluster.ConfigurationToNumberTranslator;
import optimal.utils.DataProcessor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
        final int fileId = myConfigurationToNumberTranslator.translateFitnessAndMutationRateToNumber(myFitness,
                myProbability);
        final ProbabilityVectorProcessor probabilityVectorProcessor =
                new ProbabilityVectorProcessor(getPrecomputedVectorsDir() + fileId + ".csv");
        probabilityVectorProcessor.loadData();
        return probabilityVectorProcessor.getProcessedData();
    }

    private String getPrecomputedVectorsDir() {
        return ((PrecomputedVectorReadingConfiguration) myConfiguration.getVectorGenerationConfig()).getPrecomputedVectorsDir();
    }

    private static final class ProbabilityVectorProcessor extends DataProcessor<ArrayList<Double>> {

        public ProbabilityVectorProcessor(@NotNull String csvFileName) {
            super(csvFileName);
        }

        @Override
        public ArrayList<Double> getProcessedData() {
            HashMap<Integer, Double> mp = new HashMap<>();
            for (List<String> record : myRecords) {
                final int fitnessIncrease = Integer.parseInt(record.get(0));
                final double probability = Double.parseDouble(record.get(1));
                mp.put(fitnessIncrease, probability);
            }
            final List<Integer> fitness = mp.keySet().stream().sorted().collect(Collectors.toList());
            final ArrayList<Double> ans = new ArrayList<>();
            for (int f : fitness) {
                ans.add(mp.get(f));
            }
            return ans;
        }
    }
}
