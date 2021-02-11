package optimal;

import optimal.configuration.OptimalMutationRateSearchingSingleExperimentConfiguration;
import optimal.configuration.algorithms.AlgorithmConfig;
import optimal.configuration.probability.IterativeProbabilityConfiguration;
import optimal.configuration.problems.ProblemConfig;
import optimal.configuration.vectorGeneration.PrecomputedVectorReadingConfiguration;
import optimal.execution.ResultEntity;
import optimal.execution.events.EventType;
import optimal.execution.events.EventsManager;
import optimal.execution.events.ResultEntityObtainedEvent;
import optimal.oneStepAlgorithms.OneStepAlgorithmsManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import problem.ProblemsManager;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

class BestMutationRateSearcherTest {

    private final Consumer<EventsManager.Event> consumer = event -> {
        if (event instanceof ResultEntityObtainedEvent) {
            System.out.println(((ResultEntityObtainedEvent) event).getResultEntity().toString());
        }
    };

    private void doTestMutationProbabilities(OptimalMutationRateSearchingSingleExperimentConfiguration configuration, String filename) throws IOException {
        URL resource = getClass().getClassLoader().getResource("optimalData/" + filename);
        assert resource != null;
        String resourcePath = resource.getPath();
        List<String> expectedStrings = Files.readAllLines(Paths.get(resourcePath));
        ArrayList<Double> firstElements = new ArrayList<>();
        ArrayList<Double> secondElements = new ArrayList<>();
        for (String line : expectedStrings) {
            String[] s = line.split(" ");
            firstElements.add(Double.parseDouble(s[0]));
            secondElements.add(Double.parseDouble(s[1]));
        }
        AtomicInteger cnt = new AtomicInteger(0);
        final double EPS = 0.01;
        final Consumer<EventsManager.Event> consumer = event -> {
            if (event instanceof ResultEntityObtainedEvent) {
                ResultEntity resultEntity = ((ResultEntityObtainedEvent) event).getResultEntity();
                Assertions.assertEquals(firstElements.get(cnt.get()), resultEntity.bestProbability);
                Assertions.assertTrue(Math.abs(secondElements.get(cnt.get()) - resultEntity.optimizationTime) < EPS);
                cnt.incrementAndGet();
            }
        };
        final BestMutationRateSearcher searcher = new BestMutationRateSearcher(configuration);
        searcher.addListener(consumer, EventType.OPTIMAL_RESULT_READY);
        searcher.getBestMutationProbabilities();
    }

    @Test
    void getBestMutationProbabilities() throws IOException {
        URL resource = getClass().getClassLoader().getResource("optimalData/vectors0");
        assert resource != null;
        String resourcePath = resource.getPath();
        doTestMutationProbabilities(new OptimalMutationRateSearchingSingleExperimentConfiguration(
                new ProblemConfig(ProblemsManager.ProblemType.ONE_MAX_RUGGEDNESS, 100),
                new AlgorithmConfig(OneStepAlgorithmsManager.AlgorithmType.SIMPLE_ONE_PLUS_LAMBDA, 2),
                80, 100,
                new IterativeProbabilityConfiguration(0.001, 0.05, 0.001),
                new PrecomputedVectorReadingConfiguration("", resourcePath)), "bestMutationProbCorrect.csv");
    }

}