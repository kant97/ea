package optimal.optimal2;

import javafx.util.Pair;
import optimal.configuration.OneExperimentConfiguration;
import optimal.configuration.vectorGeneration.VectorGenerationConfiguration;
import optimal.probabilitySampling.ProbabilitySearcher;
import org.apache.commons.math3.linear.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BestMutationRateSearcher2 {
    private final List<ResultListener> resultListeners = new ArrayList<>();

    public void addListener(@NotNull ResultListener listener) {
        resultListeners.add(listener);
    }

    public boolean deleteListener(@NotNull ResultListener listener) {
        return resultListeners.remove(listener);
    }

    public void runExperimentWithConfiguration(@NotNull OneExperimentConfiguration oneExperimentConfiguration, @NotNull VectorGenerationConfiguration.VectorGenerationStrategy strategy) {
        final Map<Integer, Double> dp = new HashMap<>();
        final AbstractPiExistenceClassesManager piExistenceClassesManager = AbstractPiExistenceClassesManager.create(oneExperimentConfiguration.problemConfig);
        for (int id : piExistenceClassesManager.getPiExistenceClassesWithFitness(oneExperimentConfiguration.endFitness)) {
            dp.put(id, 0.);
        }
        for (int fitness = oneExperimentConfiguration.endFitness - 1; fitness >= oneExperimentConfiguration.beginFitness; fitness--) {
            final ProbabilitySearcher ps = ProbabilitySearcher.createProbabilitySearcher(oneExperimentConfiguration.probabilityEnumeration);
            for (double r = ps.getInitialProbability(); !ps.isFinished(); r = ps.getNextProb()) {
                final List<Integer> curPiExistenceClasses = piExistenceClassesManager.getPiExistenceClassesWithFitness(fitness);
                final List<Double> C = new ArrayList<>();
                final List<List<Double>> P = new ArrayList<>();
                for (int piExistenceClassId : curPiExistenceClasses) {
                    final Map<Integer, Double> pk = AbstractTransitionsGenerator.create(oneExperimentConfiguration, strategy).getTransitionsProbabilities(r, piExistenceClassId);
                    final Pair<List<Double>, Double> lineK = buildLineK(pk, curPiExistenceClasses, P.size(), dp);
                    P.add(lineK.getKey());
                    C.add(lineK.getValue());
                }
                final List<Double> T = solveSystem(P, C);
                onResultReady(fitness, r, curPiExistenceClasses, T, dp);
            }
        }
    }

    private void onResultReady(int fitness, double r, @NotNull List<Integer> curPiExistenceClasses, @NotNull List<Double> T, @NotNull Map<Integer, Double> dp) {
        updateOptimalExpectations(curPiExistenceClasses, T, dp);
        for (ResultListener listener : resultListeners) {
            listener.onResultsReady(fitness, r, curPiExistenceClasses, T);
        }
    }

    private void updateOptimalExpectations(List<Integer> curPiExistenceClasses, List<Double> t, Map<Integer, Double> dp) {
        for (int i = 0; i < curPiExistenceClasses.size(); i++) {
            final int piExistenceClassId = curPiExistenceClasses.get(i);
            final double ti = t.get(i);
            dp.computeIfPresent(piExistenceClassId, (id, oldTi) -> Math.min(oldTi, ti));
            dp.putIfAbsent(piExistenceClassId, ti);
        }
    }

    protected List<Double> solveSystem(List<List<Double>> p, List<Double> c) {
        final double[][] matrixData = new double[p.size()][];
        int i = 0;
        for (List<Double> nestedList : p) {
            matrixData[i] = new double[nestedList.size()];
            int j = 0;
            for (double pij : nestedList) {
                matrixData[i][j++] = pij;
            }
            i++;
        }
        final RealMatrix m = MatrixUtils.createRealMatrix(matrixData);
        final DecompositionSolver solver = new LUDecomposition(m).getSolver();
        final double[] constantsData = new double[c.size()];
        for (int i1 = 0; i1 < c.size(); i1++) {
            constantsData[i1] = c.get(i1);
        }
        final RealVector constants = new ArrayRealVector(constantsData);
        double[] solution;
        try {
            solution = solver.solve(constants).toArray();
        } catch (SingularMatrixException e) {
            solution = new double[p.size()];
            Arrays.fill(solution, Double.MAX_VALUE);
        }
        final List<Double> ans = new ArrayList<>();
        for (double t : solution) {
            ans.add(t);
        }
        return ans;
    }

    protected @NotNull Pair<List<Double>, Double> buildLineK(@NotNull Map<Integer, Double> pk, @NotNull List<Integer> curPiExistenceClasses, int lineNumber, @NotNull Map<Integer, Double> dp) {
        final List<Double> pi = new ArrayList<>();
        for (int i = 0; i < curPiExistenceClasses.size(); i++) {
            pi.add(Double.MAX_VALUE);
        }
        for (int i = 0; i < curPiExistenceClasses.size(); i++) {
            final int id = curPiExistenceClasses.get(i);
            pi.set(i, pk.getOrDefault(id, 0.));
            pk.remove(id);
        }
        pi.set(lineNumber, pi.get(lineNumber) - 1.);
        final double[] C = new double[1];
        C[0] = -1.;
        pk.forEach((j, pkj) -> C[0] += pkj * dp.getOrDefault(j, 0.));
        return new Pair<>(pi, C[0]);
    }
}
