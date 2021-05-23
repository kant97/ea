package optimal.optimal2;

import optimal.configuration.MainConfiguration;
import optimal.configuration.algorithms.AlgorithmConfig;
import optimal.configuration.probability.ExponentialGridConfiguration;
import optimal.configuration.problems.PlateauConfig;
import optimal.configuration.runs.FixedRunsConfiguration;
import optimal.configuration.runs.FixedSuccessConfiguration;
import optimal.configuration.transitionsGeneration.PrecomputedTransitionsReadingConfiguration;
import optimal.oneStepAlgorithms.OneStepAlgorithmsManager;
import problem.ProblemsManager;

import java.util.Arrays;

import static optimal.configuration.transitionsGeneration.TransitionsGenerationConfiguration.TransitionsGenerationStrategy.READ_AND_RECOMPUTE_TRANSITIONS;

public class Main {
    public static void main(String[] args) {
        for (int lambda : Arrays.asList(2,4,8,16,32,64,128,256,512)) {
            final MainConfiguration oneExperimentConfiguration = new MainConfiguration(new PlateauConfig(ProblemsManager.ProblemType.ONE_MAX_PLATEAU, 100, 2), new AlgorithmConfig(OneStepAlgorithmsManager.AlgorithmType.SIMPLE_ONE_PLUS_LAMBDA, lambda), 1, 51, new PrecomputedTransitionsReadingConfiguration(READ_AND_RECOMPUTE_TRANSITIONS, new ExponentialGridConfiguration(2.718281828459045, -9.210340371976182, 0.0, 0.09210340371976182), new FixedRunsConfiguration(500000), "5-plateau_k2/transitions0", "5-plateau_k2/transitions0/global_configs.json"));
            final ExperimentEnvironment environment = ExperimentEnvironment.createEnvironmentForResults(oneExperimentConfiguration, "5-plateau_k2", false);
//        final MainConfiguration oneExperimentConfiguration = new MainConfiguration(new PlateauConfig(ProblemsManager.ProblemType.ONE_MAX_PLATEAU, 100, 2), new AlgorithmConfig(OneStepAlgorithmsManager.AlgorithmType.SIMPLE_ONE_PLUS_LAMBDA, 512), 1, 51, new PrecomputedTransitionsReadingConfiguration(PRECOMPUTED_TRANSITIONS_READING, new ExponentialGridConfiguration(2.718281828459045, -9.210340371976182, 0.0, 0.09210340371976182), new FixedSuccessConfiguration(2500,500000), "allVectorsPlateau/vectors0", "allVectorsPlateau/vectors0/global_configs.json"));
//        final ExperimentEnvironment environment = ExperimentEnvironment.createEnvironmentForResults(oneExperimentConfiguration, "allVectorsPlateau", false);
            final AllResultsListener allResultsListener = new AllResultsListener(environment.getResultsFilePath().toAbsolutePath().toString(), oneExperimentConfiguration);
            final BestMutationRateSearcher2 bestMutationRateSearcher2 = new BestMutationRateSearcher2();
            bestMutationRateSearcher2.addListener(allResultsListener);
            bestMutationRateSearcher2.runExperimentWithConfiguration(oneExperimentConfiguration);
            allResultsListener.close();
        }
    }
}
