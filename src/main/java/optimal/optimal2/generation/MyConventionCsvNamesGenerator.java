package optimal.optimal2.generation;

import optimal.configuration.probability.ProbabilitySamplingConfiguration;
import optimal.probabilitySampling.IntegerToProbabilityBijectiveMapping;
import optimal.probabilitySampling.ProbabilitySpace;
import org.jetbrains.annotations.NotNull;

public class MyConventionCsvNamesGenerator extends CsvFileNamesGenerator {
    private final IntegerToProbabilityBijectiveMapping bijectionIntsProbs;

    public MyConventionCsvNamesGenerator(@NotNull ProbabilitySamplingConfiguration probabilitySamplingConfiguration) {
        bijectionIntsProbs = ProbabilitySpace.createProbabilitySpace(probabilitySamplingConfiguration).createBijectionToIntegers();
    }

    @Override
    public @NotNull String getCsvFileName(int piExistenceClass, double probability) {
        return "c" + piExistenceClass + "_r" + bijectionIntsProbs.probabilityToInteger(probability);
    }
}
