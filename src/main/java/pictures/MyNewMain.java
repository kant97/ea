package pictures;

import optimal.configuration.probability.IterativeProbabilityConfiguration;
import org.ejml.simple.SimpleMatrix;
import pictures.processing.AlgorithmData;
import pictures.processing.AlgorithmLogDataProcessor;

import java.util.*;

public class MyNewMain {
    public static void main(String[] args) {
        final MatrixDataProcessor matrixDataProcessor = new MatrixDataProcessor("/home/kirill.antonov/itmo/Research" +
                "/IrasEngine/ea/all-vectors-plateau/optimal_for_lambda=512/allIntermediateResults.csv", 9, 8, 10);
        matrixDataProcessor.loadData();
        final SimpleMatrix matrix = matrixDataProcessor.getProcessedData();
        final MatrixDataProcessor matrixDataProcessor2 = new MatrixDataProcessor("/home/kirill.antonov/itmo/Research" +
                "/IrasEngine/ea/all-vectors-plateau/optimal_for_lambda=512/allIntermediateResults.csv", 9, 8, 10);
        matrixDataProcessor2.loadData();
        final AlgorithmLogDataProcessor dataProcessor = new AlgorithmLogDataProcessor("/home/kirill" +
                ".antonov/itmo/Research/IrasEngine/ea/twoRateRun0.csv");
        dataProcessor.loadData();
        final ArrayList<AlgorithmData> processedData1 = dataProcessor.getProcessedData();
        final MatrixWrapper matrixWrapper = new MatrixWrapper(matrix, new IterativeProbabilityConfiguration(0.01, 0.5
                , 0.01));
        final MatrixWrapper matrixWrapper2 = new MatrixWrapper(matrixDataProcessor2.getProcessedData(),
                new IterativeProbabilityConfiguration(0.0001, 0.01, 0.0001));
        final int optimalValue = 51;
        final int lambda = 512;
        Map<Integer, Double> fitnessToBestRunTime = new HashMap<>();
        for (int i = 0; i < matrix.numCols(); i++) {
            double bestRunTime = Double.MAX_VALUE;
            for (int j = 0; j < matrix.numRows(); j++) {
                bestRunTime = Math.min(matrix.get(j, i), bestRunTime);
            }
            fitnessToBestRunTime.put(i + 1, bestRunTime);
        }

        final ArrayList<Pair<Integer, Double>> diff = new ArrayList<>();
        final List<MatrixWrapper> wrappers = Arrays.asList(matrixWrapper, matrixWrapper2);
        for (AlgorithmData algorithmData : processedData1) {
            final int fitnessDist = optimalValue - algorithmData.getFitness();
            if (fitnessDist == 0) continue;
            if (algorithmData.getIterationNumber() > 1000) break;
            MatrixWrapper myWrapper = null;
            for (MatrixWrapper wrapper : wrappers) {
                final double minMutationRate = wrapper.getMinMutationRate();
                final int maxFitnessDist = wrapper.getMaxFitnessDist();
                if (minMutationRate <= algorithmData.getMutationRate() && maxFitnessDist >= fitnessDist) {
                    myWrapper = wrapper;
                    break;
                } else if (maxFitnessDist >= fitnessDist) {
                    myWrapper = wrapper;
                }
            }
            if (myWrapper == null) {
                System.err.println("Unable to find precise enough matrix for " + algorithmData);
                return;
            }
            final Double runTime = myWrapper.getExpectedTime(fitnessDist, algorithmData.getMutationRate());
            final Double bestRunTime = fitnessToBestRunTime.get(fitnessDist);
            double runTimeDiff = runTime - bestRunTime;
            if (Math.abs(Double.MAX_VALUE - runTime) < 1.) runTimeDiff = Double.MAX_VALUE;
            diff.add(new Pair<>(algorithmData.getIterationNumber(), runTimeDiff));
        }
        final Optional<Double> maxNonInfOptional =
                diff.stream().filter(element -> Math.abs(Double.MAX_VALUE - element.second) > 1.).map(element -> element.second).max(Double::compare);
        if (!maxNonInfOptional.isPresent()) {
            throw new IllegalStateException("No max non inf in diff");
        }
        final double maxNonInf = maxNonInfOptional.get();
        System.out.println("Max non inf: " + maxNonInf / lambda);
        final double newInf = 2 * maxNonInf;
        System.out.println("IterationNumber,FitnessDiff");
        for (Pair<Integer, Double> integerDoublePair : diff) {
            double element = integerDoublePair.second;
            if (Math.abs(Double.MAX_VALUE - integerDoublePair.second) < 1.) element = newInf;
            element = element / lambda;
            System.out.println(integerDoublePair.first + "," + element);
        }
    }

}
