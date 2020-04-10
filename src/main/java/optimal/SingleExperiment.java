package optimal;

import optimal.configuration.OneExperimentConfiguration;
import optimal.configuration.probability.IterativeProbabilityConfiguration;
import optimal.execution.OptimizationParametersSearchingListener;
import optimal.execution.ResultEntity;
import optimal.execution.ResultsConsumer;
import optimal.oneStepAlgorithms.OneStepAlgorithmsManager;
import optimal.probabilitySampling.ProbabilitySamplingStrategy;
import problem.ProblemsManager;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class SingleExperiment {

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        BestMutationRateSearcher searcher =
                new BestMutationRateSearcher(new OneExperimentConfiguration(ProblemsManager.ProblemType.ONE_MAX_NEUTRALITY_3, OneStepAlgorithmsManager.AlgorithmType.TWO_RATE, OneExperimentConfiguration.DEFAULT_NUMBER_OF_ONE_STEP_REPETITIONS, 100,
                        10, 17, 34, ProbabilitySamplingStrategy.ITERATIVE, null,
                        new IterativeProbabilityConfiguration(0.1, 0.5,
                                0.1)));
        OptimalParametersLogger logger = new OptimalParametersLogger("bestProbabilities.csv", "bestTimes.csv");
        searcher.addListener(logger);
        System.out.println(searcher.getBestMutationProbabilities());
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        logger.close();
        System.out.println("Time elapsed:" + timeElapsed);
    }

    private static final class OptimalParametersLogger implements OptimizationParametersSearchingListener,
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
        public void onNewResultEntity(ResultEntity resultEntity, ResultsConsumer.ResultType type) {
            if (type == ResultsConsumer.ResultType.OPTIMAL) {
                myProbabilityPrintWriter.println(resultEntity.fitness + ", " + resultEntity.optimizationTime);
                myTimePrintWriter.println(resultEntity.fitness + ", " + resultEntity.optimizationTime);
            }
        }
    }
}
