package optimal;

import optimal.configuration.OneExperimentConfiguration;
import optimal.configuration.probability.ExponentialGridConfiguration;
import optimal.configuration.probability.IterativeProbabilityConfiguration;
import optimal.execution.events.EventType;
import optimal.execution.events.EventsManager;
import optimal.execution.events.ResultEntityObtainedEvent;
import optimal.oneStepAlgorithms.OneStepAlgorithmsManager;
import optimal.probabilitySampling.ProbabilitySamplingStrategy;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static optimal.configuration.OneExperimentConfiguration.DEFAULT_NUMBER_OF_ONE_STEP_REPETITIONS;
import static problem.ProblemsManager.ProblemType.ONE_MAX;
import static problem.ProblemsManager.ProblemType.ONE_MAX_NEUTRALITY_3;

class BestMutationRateSearcherTest {

    private final Consumer<EventsManager.Event> consumer = event -> {
        if (event instanceof ResultEntityObtainedEvent) {
            System.out.println(((ResultEntityObtainedEvent) event).getResultEntity().toString());
        }
    };


    @Test
    void getBestMutationProbabilities() {
        BestMutationRateSearcher searcher = new BestMutationRateSearcher(new OneExperimentConfiguration(
                ONE_MAX_NEUTRALITY_3, OneStepAlgorithmsManager.AlgorithmType.TWO_RATE,
                DEFAULT_NUMBER_OF_ONE_STEP_REPETITIONS, 200, 10, 33, 67,
                ProbabilitySamplingStrategy.ITERATIVE, null,
                new IterativeProbabilityConfiguration(0.1, 0.5, 0.1)
        ));
        searcher.addListener(consumer, EventType.OPTIMAL_RESULT_READY);
        searcher.getBestMutationProbabilities();
    }

    @Test
    void getBestMutationProbabilitiesTest2() {
        BestMutationRateSearcher searcher = new BestMutationRateSearcher(new OneExperimentConfiguration(
                ONE_MAX_NEUTRALITY_3, OneStepAlgorithmsManager.AlgorithmType.TWO_RATE, DEFAULT_NUMBER_OF_ONE_STEP_REPETITIONS, 100, 10, 17, 34,
                ProbabilitySamplingStrategy.ITERATIVE, null,
                new IterativeProbabilityConfiguration(0.1, 0.5, 0.1)
        ));
        searcher.addListener(consumer, EventType.INTERMEDIATE_RESULT_READY);
        searcher.getBestMutationProbabilities();
    }

    @Test
    void getBestMutationProbabilitiesTest3() {
        BestMutationRateSearcher searcher = new BestMutationRateSearcher(new OneExperimentConfiguration(
                ONE_MAX_NEUTRALITY_3, OneStepAlgorithmsManager.AlgorithmType.TWO_RATE, DEFAULT_NUMBER_OF_ONE_STEP_REPETITIONS, 100, 10, 17, 34,
                ProbabilitySamplingStrategy.EXPONENTIAL_GRID,
                new ExponentialGridConfiguration(10, -2, -1, 0.1),
                null));
        searcher.addListener(consumer, EventType.INTERMEDIATE_RESULT_READY);
        searcher.getBestMutationProbabilities();
    }

    @Test
    void getBestMutationProbabilitiesTest4() {
        BestMutationRateSearcher searcher = new BestMutationRateSearcher(new OneExperimentConfiguration(
                ONE_MAX, OneStepAlgorithmsManager.AlgorithmType.SIMPLE_ONE_PLUS_LAMBDA, 40000, 100, 10, 50, 100,
                ProbabilitySamplingStrategy.ITERATIVE,
                null,
                new IterativeProbabilityConfiguration(0.1, 0.5, 0.1)));
        searcher.addListener(consumer, EventType.OPTIMAL_RESULT_READY);
        searcher.getBestMutationProbabilities();
    }
}