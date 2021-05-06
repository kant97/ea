package optimal.optimal2;

import optimal.configuration.OneExperimentConfiguration;
import optimal.configuration.vectorGeneration.VectorGenerationConfiguration;
import optimal.optimal2.generation.AbstractTransitionsGenerator;
import optimal.optimal2.systems.GraphComponentsEquationsSystemBuilder;
import optimal.probabilitySampling.ProbabilitySearcher;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                final GraphComponentsEquationsSystemBuilder systemBuilder = new GraphComponentsEquationsSystemBuilder(curPiExistenceClasses, dp);
                for (int i = 0; i < curPiExistenceClasses.size(); i++) {
                    int piExistenceClassId = curPiExistenceClasses.get(i);
                    final Map<Integer, Double> pk = AbstractTransitionsGenerator.create(oneExperimentConfiguration.stopConditionConfig, oneExperimentConfiguration.problemConfig, oneExperimentConfiguration.algorithmConfig, strategy).getTransitionsProbabilities(r, piExistenceClassId);
                    systemBuilder.addLine(i, pk);
                }
                final List<Double> T = systemBuilder.buildSystem().solveSystem();
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
}
