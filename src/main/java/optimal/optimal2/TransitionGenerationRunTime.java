package optimal.optimal2;

import algo.Algorithm;
import optimal.configuration.OneExperimentConfiguration;
import optimal.oneStepAlgorithms.OneStepAlgorithm;
import optimal.oneStepAlgorithms.OneStepAlgorithmsManager;
import org.jetbrains.annotations.NotNull;
import problem.Problem;
import utils.BestCalculatedPatch;

import java.util.HashMap;
import java.util.Map;

public abstract class TransitionGenerationRunTime extends AbstractTransitionsGenerator {
    private final OneExperimentConfiguration myConfiguration;
    private final AbstractPiExistenceClassesManager myPiExistenceClassesManager;

    protected TransitionGenerationRunTime(@NotNull OneExperimentConfiguration configuration) {
        myConfiguration = configuration;
        myPiExistenceClassesManager = AbstractPiExistenceClassesManager.create(configuration.problemConfig);
    }

    @Override
    public @NotNull Map<Integer, Double> getTransitionsProbabilities(double r, int piExistenceClassId) {
        final Problem individual = myPiExistenceClassesManager.getAnyIndividualById(piExistenceClassId);
        final OneStepAlgorithm algorithm = OneStepAlgorithmsManager.createAlgorithm(r, myConfiguration.algorithmConfig, individual);
        final int beginFitness = individual.getFitness();
        final Map<Integer, Integer> cntOfId = new HashMap<>();
        int runNumber = 0;
        for (; !isStopConditionHit(runNumber); runNumber++) {
            algorithm.makeIteration();
            final BestCalculatedPatch patch = algorithm.getMutatedIndividual();
            final int newFitness = patch == null ? beginFitness : patch.fitness;
            if (newFitness > beginFitness) {
                update(cntOfId, myPiExistenceClassesManager.getIdByIndividual(individual, patch));
            } else {
                update(cntOfId, piExistenceClassId);
            }
            onIterationFinished(beginFitness, newFitness, algorithm);
            algorithm.resetState();
        }
        final Map<Integer, Double> transitions = new HashMap<>();
        final int finalRunNumber = runNumber;
        cntOfId.forEach((id, cnt) -> transitions.put(id, (double) cnt / (double) finalRunNumber));
        return transitions;
    }

    private void update(Map<Integer, Integer> cntOfId, int id) {
        cntOfId.computeIfPresent(id, (i, oldCnt) -> oldCnt + 1);
        cntOfId.putIfAbsent(id, 1);
    }

    protected abstract boolean isStopConditionHit(int runNumber);

    protected abstract void onIterationFinished(int beginFitness, int newFitness, @NotNull Algorithm algorithm);
}
