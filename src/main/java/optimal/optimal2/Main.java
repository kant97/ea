package optimal.optimal2;

import optimal.configuration.OneExperimentConfiguration;
import optimal.configuration.algorithms.AlgorithmConfig;
import optimal.configuration.probability.IterativeProbabilityConfiguration;
import optimal.configuration.problems.PlateauConfig;
import optimal.configuration.runs.FixedRunsConfiguration;
import optimal.configuration.vectorGeneration.VectorGenerationConfiguration;
import optimal.oneStepAlgorithms.OneStepAlgorithmsManager;
import problem.ProblemsManager;

public class Main {
    public static void main(String[] args) {
        final BestMutationRateSearcher2 bestMutationRateSearcher2 = new BestMutationRateSearcher2();
        OneExperimentConfiguration oneExperimentConfiguration = new OneExperimentConfiguration(new PlateauConfig(ProblemsManager.ProblemType.ONE_MAX_PLATEAU, 100, 2), new AlgorithmConfig(OneStepAlgorithmsManager.AlgorithmType.SIMPLE_ONE_PLUS_LAMBDA, 1), new FixedRunsConfiguration(1000), 1, 51, new IterativeProbabilityConfiguration(0.001, 0.5, 0.001));
        final AllResultsListener allResultsListener = new AllResultsListener("piExistenceResults.csv", oneExperimentConfiguration);
        bestMutationRateSearcher2.addListener(allResultsListener);
        bestMutationRateSearcher2.runExperimentWithConfiguration(oneExperimentConfiguration, VectorGenerationConfiguration.VectorGenerationStrategy.RUN_TIME_VECTOR_GENERATION);
    }
}
