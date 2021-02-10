package optimal;

import optimal.configuration.OneExperimentConfiguration;
import optimal.configuration.algorithms.AlgorithmConfig;
import optimal.configuration.algorithms.TwoRateConfig;
import optimal.configuration.probability.ExponentialGridConfiguration;
import optimal.configuration.probability.IterativeProbabilityConfiguration;
import optimal.configuration.problems.ProblemConfig;
import optimal.configuration.runs.FixedRunsConfiguration;
import optimal.execution.ResultEntity;
import optimal.execution.events.EventType;
import optimal.execution.events.EventsManager;
import optimal.execution.events.ResultEntityObtainedEvent;
import optimal.heuristics.ExperimentState;
import optimal.heuristics.OneMaxHeuristics;
import optimal.oneStepAlgorithms.OneStepAlgorithmsManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import problem.ProblemsManager;

import java.util.function.Consumer;

import static optimal.configuration.OneExperimentConfiguration.DEFAULT_NUMBER_OF_ONE_STEP_REPETITIONS;

class BestMutationRateSearcherTest {

    private final Consumer<EventsManager.Event> consumer = event -> {
        if (event instanceof ResultEntityObtainedEvent) {
            System.out.println(((ResultEntityObtainedEvent) event).getResultEntity().toString());
        }
    };

//    @Test
//    void getBestMutationProbabilities() {
//        BestMutationRateSearcher searcher = new BestMutationRateSearcher(new OneExperimentConfiguration(
//                new ProblemConfig(ProblemsManager.ProblemType.ONE_MAX_NEUTRALITY_3, 200),
//                new TwoRateConfig(OneStepAlgorithmsManager.AlgorithmType.TWO_RATE, 10, 0.01),
//                new FixedRunsConfiguration(DEFAULT_NUMBER_OF_ONE_STEP_REPETITIONS), 33, 67,
//                new IterativeProbabilityConfiguration(0.1, 0.5, 0.1)
//        ));
//        searcher.addListener(consumer, EventType.OPTIMAL_RESULT_READY);
//        searcher.getBestMutationProbabilities();
//    }
//
//    @Test
//    void getBestMutationProbabilitiesTest2() {
//        BestMutationRateSearcher searcher = new BestMutationRateSearcher(new OneExperimentConfiguration(
//                new ProblemConfig(ProblemsManager.ProblemType.ONE_MAX_NEUTRALITY_3, 100),
//                new TwoRateConfig(OneStepAlgorithmsManager.AlgorithmType.TWO_RATE, 10, 0.01),
//                new FixedRunsConfiguration(DEFAULT_NUMBER_OF_ONE_STEP_REPETITIONS), 17, 34,
//                new IterativeProbabilityConfiguration(0.1, 0.5, 0.1)
//        ));
//        searcher.addListener(consumer, EventType.INTERMEDIATE_RESULT_READY);
//        searcher.getBestMutationProbabilities();
//    }
//
//    @Test
//    void getBestMutationProbabilitiesTest3() {
//        BestMutationRateSearcher searcher = new BestMutationRateSearcher(new OneExperimentConfiguration(
//                new ProblemConfig(ProblemsManager.ProblemType.ONE_MAX_NEUTRALITY_3, 100),
//                new TwoRateConfig(OneStepAlgorithmsManager.AlgorithmType.TWO_RATE, 10, 0.01),
//                new FixedRunsConfiguration(DEFAULT_NUMBER_OF_ONE_STEP_REPETITIONS), 17, 34,
//                new ExponentialGridConfiguration(10, -2, -1, 0.1)));
//        searcher.addListener(consumer, EventType.INTERMEDIATE_RESULT_READY);
//        searcher.getBestMutationProbabilities();
//    }
//
//    @Test
//    void getBestMutationProbabilitiesTest4() {
//        BestMutationRateSearcher searcher = new BestMutationRateSearcher(new OneExperimentConfiguration(
//                new ProblemConfig(ProblemsManager.ProblemType.ONE_MAX, 100),
//                new AlgorithmConfig(OneStepAlgorithmsManager.AlgorithmType.SIMPLE_ONE_PLUS_LAMBDA, 10),
//                new FixedRunsConfiguration(40000), 50, 100,
//                new IterativeProbabilityConfiguration(0.1, 0.5, 0.1)));
//        searcher.addListener(consumer, EventType.OPTIMAL_RESULT_READY);
//        searcher.getBestMutationProbabilities();
//    }
}