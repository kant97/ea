package pictures;

import optimal.optimal2.AbstractPiExistenceClassesManager;
import pictures.processing.BestResultsProcessor;
import problem.Problem;
import problem.Ruggedness;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class MathExpectationsCalc {
    protected final int n;
    protected final ArrayList<BigInteger> Cn;
    protected final BigDecimal all;
    protected final Problem myProblem;

    public MathExpectationsCalc(int n, Problem problem) {
        this.n = n;
        this.myProblem = problem;
        all = new BigDecimal("2").pow(n);
        ArrayList<BigInteger> tmpSwap;
        ArrayList<BigInteger> tmp1 = new ArrayList<>();
        ArrayList<BigInteger> tmp2 = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            tmp1.add(BigInteger.ZERO);
            tmp2.add(BigInteger.ZERO);
        }
        tmp1.set(0, BigInteger.ONE);
        for (int i = 1; i <= n; i++) {
            tmp2.set(0, BigInteger.ONE);
            for (int j = 0; j < n; j++) {
                tmp2.set(j + 1, tmp1.get(j).add(tmp1.get(j + 1)));
            }
            tmpSwap = tmp1;
            tmp1 = tmp2;
            tmp2 = tmpSwap;
        }
        Cn = tmp1;
    }

    // mathExps[i] = T means that expected time from i to n is T
    public double getMathExpect(ArrayList<Double> mathExps) {
        double T = 0.;
        for (int fitnessValue = 0; fitnessValue < n; fitnessValue++) {
            int onesCount = myProblem.getOnesCount(fitnessValue);
            BigDecimal binomial = new BigDecimal(Cn.get(onesCount));
            double p = Double.parseDouble(binomial.divide(all, 30, RoundingMode.HALF_UP).toString());
            T += mathExps.get(fitnessValue) * p;
        }
        return T;
    }

    public double getPiExistMathExpect(ArrayList<Double> mathExps, AbstractPiExistenceClassesManager piExistenceClassesManager) {
        double T = 0.;
        for (int id = 0; id < mathExps.size(); id++) {
            int onesCount = id; // Plateau case
            BigDecimal binomial = new BigDecimal(Cn.get(onesCount));
            double p = Double.parseDouble(binomial.divide(all, 30, RoundingMode.HALF_UP).toString());
            T += mathExps.get(id) * p;
        }
        return T;
    }

    // returns fitness mapped to the best runtime for this fitness
    private static Map<Integer, Double> getProcessedDataForLambdaFromOneSetOfResults(Path oneSetOfResultsPath, int lambda, int optimalValue) {
        final String targetSuffix = "lambda=" + lambda;
        for (File directory : oneSetOfResultsPath.toFile().listFiles(File::isDirectory)) {
            if (!directory.getName().endsWith(targetSuffix) || !directory.getName().startsWith("optimal")) {
                continue;
            }
            final Optional<File> optimalFile =
                    Arrays.stream(directory.listFiles()).filter(f -> f.getName().equals("results.csv")).findFirst();
            if (!optimalFile.isPresent()) {
                throw new IllegalStateException("File with optimal results is not found.");
            }
            final BestResultsProcessor bestResultsProcessor =
                    new BestResultsProcessor(optimalFile.get().toPath().toAbsolutePath().toString(), 10, 8, 3,
                            optimalValue);
            bestResultsProcessor.loadData();
            bestResultsProcessor.getFitnessToRuntimeMap();
            if (bestResultsProcessor.lambda != lambda) {
                throw new IllegalStateException("Results for wrong lambda = " + bestResultsProcessor.lambda + " are written to the folder for lambda = " + lambda);
            }
            return bestResultsProcessor.getFitnessToRuntimeMap();
        }
        throw new IllegalStateException("Results for lambda = " + lambda + " can not be found in directory " + oneSetOfResultsPath);
    }

    private static ArrayList<Double> mergeResultsForLambda(List<Path> oneResultsDirectories, int lambda, int optimalValue) {
        final ArrayList<Double> ans = new ArrayList<>();
        for (int i = 0; i < optimalValue; i++) {
            ans.add(Double.MAX_VALUE);
        }
        for (Path path : oneResultsDirectories) {
            Map<Integer, Double> oneLambdaData = getProcessedDataForLambdaFromOneSetOfResults(path, lambda, optimalValue);
            for (Map.Entry<Integer, Double> entry : oneLambdaData.entrySet()) {
                ans.set(entry.getKey(), Math.min(entry.getValue(), ans.get(entry.getKey())));
            }
        }
        return ans;
    }

    // returns lambda mapped to the expected runtime
    private static Map<Integer, Double> getExpectedRuntimesForAllLambdas(List<Path> oneResultDirectories, int optimalValue, MathExpectationsCalc calc) {
        Path directoryForLambdaReading = oneResultDirectories.get(0);
        Map<Integer, Double> ans = new HashMap<>();
        for (File directory : directoryForLambdaReading.toFile().listFiles(File::isDirectory)) {
            if (!directory.getName().startsWith("optimal")) {
                continue;
            }
            final Optional<File> optimalFile =
                    Arrays.stream(directory.listFiles()).filter(f -> f.getName().equals("results.csv")).findFirst();
            if (!optimalFile.isPresent()) {
                throw new IllegalStateException("File with optimal results is not found.");
            }
            final BestResultsProcessor bestResultsProcessor =
                    new BestResultsProcessor(optimalFile.get().toPath().toAbsolutePath().toString(), 10, 8, 3,
                            optimalValue);
            bestResultsProcessor.loadData();
            bestResultsProcessor.getFitnessToRuntimeMap();
            int lambda = bestResultsProcessor.lambda;
            ArrayList<Double> doubles = mergeResultsForLambda(oneResultDirectories, lambda, optimalValue);
            final double mathExpect = calc.getMathExpect(doubles);
            ans.put(bestResultsProcessor.lambda, mathExpect);
        }
        return ans;
    }

    private static Map<Integer, Double> getLambdaToRuntime(String directoryWithOptimalResults, int optimalValue,
                                                           MathExpectationsCalc calc) {
        final Path path = Paths.get(directoryWithOptimalResults);
        Map<Integer, Double> ans = new HashMap<>();
        for (File directory : path.toFile().listFiles(File::isDirectory)) {
            if (!directory.getName().startsWith("optimal")) {
                continue;
            }
            final Optional<File> optimalFile =
                    Arrays.stream(directory.listFiles()).filter(f -> f.getName().equals("results.csv")).findFirst();
            if (!optimalFile.isPresent()) {
                throw new IllegalStateException("File with optimal results is not found.");
            }
            final BestResultsProcessor bestResultsProcessor =
                    new BestResultsProcessor(optimalFile.get().toPath().toAbsolutePath().toString(), 10, 8, 3,
                            optimalValue);
            bestResultsProcessor.loadData();
            final ArrayList<Double> processedData = bestResultsProcessor.getProcessedData();
            final double mathExpect = calc.getMathExpect(processedData);
            ans.put(bestResultsProcessor.lambda, mathExpect);
        }
        return ans;
    }

    public static void main(String[] args) {
        final int size = 100;
        final Problem problem = new Ruggedness(100, 2);
        final int optimal = size;

        final MathExpectationsCalc mathExpectationsCalc = new MathExpectationsCalc(size, problem);
//        final Map<Integer, Double> lambdaToRuntime = getLambdaToRuntime("all7-vectors-ruggedness", optimal,
//                mathExpectationsCalc);
        final Map<Integer, Double> lambdaToRuntime = getExpectedRuntimesForAllLambdas(Arrays.asList(Paths.get("all8-vectors-ruggedness"), Paths.get("all7-vectors-ruggedness")), optimal, mathExpectationsCalc);
        System.out.println("lambda, time");
        final List<Integer> sortedLambdas = lambdaToRuntime.keySet().stream().sorted().collect(Collectors.toList());
        for (int lambda : sortedLambdas) {
            System.out.println(lambda + ", " + lambdaToRuntime.get(lambda));
        }
    }
}
