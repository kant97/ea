package optimal;

import optimal.configuration.OneExperimentConfiguration;
import optimal.configuration.algorithms.AlgorithmConfig;
import optimal.configuration.probability.IterativeProbabilityConfiguration;
import optimal.configuration.problems.ProblemConfig;
import optimal.configuration.runs.FixedRunsConfiguration;
import optimal.execution.events.EventType;
import optimal.execution.ResultEntity;
import optimal.execution.events.EventsManager;
import optimal.execution.events.ResultEntityObtainedEvent;
import optimal.oneStepAlgorithms.OneStepAlgorithmsManager;
import problem.ProblemsManager;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.function.Consumer;

public class SingleExperiment {

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        BestMutationRateSearcher searcher =
                new BestMutationRateSearcher(new OneExperimentConfiguration(new ProblemConfig(ProblemsManager.ProblemType.ONE_MAX_NEUTRALITY_3, 100),
                        new AlgorithmConfig(OneStepAlgorithmsManager.AlgorithmType.TWO_RATE, 10),
                        new FixedRunsConfiguration(OneExperimentConfiguration.DEFAULT_NUMBER_OF_ONE_STEP_REPETITIONS),
                        17, 34, new IterativeProbabilityConfiguration(0.1, 0.5,
                        0.1)));
        OptimalParametersLogger logger = new OptimalParametersLogger("bestProbabilities.csv", "bestTimes.csv");
        searcher.addListener(logger, EventType.OPTIMAL_RESULT_READY);
        System.out.println(searcher.getBestMutationProbabilities());
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        logger.close();
        System.out.println("Time elapsed:" + timeElapsed);
    }

    private static final class OptimalParametersLogger implements Consumer<EventsManager.Event>,
            AutoCloseable {

        private final PrintWriter myProbabilityPrintWriter;
        private final PrintWriter myTimePrintWriter;

        public OptimalParametersLogger(String filenameToLogProbability, String filenameToLogTime) throws FileNotFoundException {
            myProbabilityPrintWriter = new PrintWriter(filenameToLogProbability);
            myProbabilityPrintWriter.println("fitness, bestMutationProbability");
            myTimePrintWriter = new PrintWriter(filenameToLogTime);
            myTimePrintWriter.println("fitness, optimizationTime");
        }

        @Override
        public void close() throws Exception {
            myTimePrintWriter.close();
            myProbabilityPrintWriter.close();
        }

        @Override
        public void accept(EventsManager.Event event) {
            if (event instanceof ResultEntityObtainedEvent) {
                final ResultEntityObtainedEvent resultEntityObtainedEvent = (ResultEntityObtainedEvent) event;
                final ResultEntity resultEntity = resultEntityObtainedEvent.getResultEntity();
                myProbabilityPrintWriter.println(resultEntity.fitness + ", " + resultEntity.optimizationTime);
                myTimePrintWriter.println(resultEntity.fitness + ", " + resultEntity.optimizationTime);
            }
        }
    }
}
