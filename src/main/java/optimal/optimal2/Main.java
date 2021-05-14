package optimal.optimal2;

import optimal.configuration.MainConfiguration;
import optimal.configuration.algorithms.AlgorithmConfig;
import optimal.configuration.probability.ExponentialGridConfiguration;
import optimal.configuration.problems.PlateauConfig;
import optimal.configuration.runs.FixedSuccessConfiguration;
import optimal.configuration.transitionsGeneration.PrecomputedTransitionsReadingConfiguration;
import optimal.oneStepAlgorithms.OneStepAlgorithmsManager;
import problem.ProblemsManager;

public class Main {
    public static void main(String[] args) {
        final MainConfiguration oneExperimentConfiguration = new MainConfiguration(new PlateauConfig(ProblemsManager.ProblemType.ONE_MAX_PLATEAU, 100, 2), new AlgorithmConfig(OneStepAlgorithmsManager.AlgorithmType.SIMPLE_ONE_PLUS_LAMBDA, 512), 1, 51, new PrecomputedTransitionsReadingConfiguration(new ExponentialGridConfiguration(2.718281828459045, -9.210340371976182, 0.0, 0.09210340371976182), new FixedSuccessConfiguration(2500, 500000), "allVectorsPlateau/vectors0", "allVectorsPlateau/vectors0/global_configs.json"));
        final ExperimentEnvironment environment = ExperimentEnvironment.createEnvironmentForResults(oneExperimentConfiguration, "allVectorsPlateau", false);
        final AllResultsListener allResultsListener = new AllResultsListener(environment.getResultsFilePath().toAbsolutePath().toString(), oneExperimentConfiguration);
        final BestMutationRateSearcher2 bestMutationRateSearcher2 = new BestMutationRateSearcher2();
        bestMutationRateSearcher2.addListener(allResultsListener);
        bestMutationRateSearcher2.runExperimentWithConfiguration(oneExperimentConfiguration);
        allResultsListener.close();
    }
}
