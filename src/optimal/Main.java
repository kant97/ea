package optimal;

import problem.ProblemsManager;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Main {

    private static final class OptimalParametersLogger implements OptimizationParametersSearchingListener, AutoCloseable {

        private final PrintWriter myProbabilityPrintWriter;
        private final PrintWriter myTimePrintWriter;

        public OptimalParametersLogger(String filenameToLogProbability, String filenameToLogTime) throws FileNotFoundException {
            myProbabilityPrintWriter = new PrintWriter(filenameToLogProbability);
            myProbabilityPrintWriter.println("fitness, bestMutationProbability");
            myTimePrintWriter = new PrintWriter(filenameToLogTime);
            myTimePrintWriter.println("fitness, optimizationTime");
        }

        @Override
        public void onNewOptimizationTimeForFitness(int fitness, double time) {
            myTimePrintWriter.println(fitness + ", " + time);
        }

        @Override
        public void onNewMutationProbabilityForFitness(int fitness, double mutationRate) {
            myProbabilityPrintWriter.println(fitness + ", " + mutationRate);
        }

        @Override
        public void close() throws Exception {
            myTimePrintWriter.close();
            myProbabilityPrintWriter.close();
        }
    }

    public static void main(String[] args) throws Exception {
        BestMutationRateSearcher searcher = new BestMutationRateSearcher(ProblemsManager.ProblemType.ONE_MAX, 1000,
                10, 500, 1000, 0.001, 0.5, 0.01);
        OptimalParametersLogger logger = new OptimalParametersLogger("bestProbabilities.csv", "bestTimes.csv");
        searcher.addListener(logger);
        System.out.println(searcher.getBestMutationProbabilities());
        logger.close();
    }
}
