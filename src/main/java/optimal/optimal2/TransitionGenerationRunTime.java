package optimal.optimal2;

import algo.Algorithm;
import optimal.configuration.algorithms.AlgorithmConfig;
import optimal.configuration.problems.ProblemConfig;
import optimal.configuration.runs.StopConditionConfiguration;
import optimal.oneStepAlgorithms.OneStepAlgorithm;
import optimal.oneStepAlgorithms.OneStepAlgorithmsManager;
import optimal.optimal2.generation.AbstractTransitionsGenerator;
import org.jetbrains.annotations.NotNull;
import problem.Problem;
import utils.BestCalculatedPatch;

import java.util.HashMap;
import java.util.Map;

public abstract class TransitionGenerationRunTime extends AbstractTransitionsGenerator {
    private final StopConditionConfiguration myStopConditionConfiguration;
    private final AlgorithmConfig myAlgorithmConfiguration;
    private final AbstractPiExistenceClassesManager myPiExistenceClassesManager;

    protected TransitionGenerationRunTime(@NotNull StopConditionConfiguration stopConditionConfiguration, @NotNull ProblemConfig problemConfig, @NotNull AlgorithmConfig algorithmConfig) {
        myStopConditionConfiguration = stopConditionConfiguration;
        myAlgorithmConfiguration = algorithmConfig;
        myPiExistenceClassesManager = AbstractPiExistenceClassesManager.create(problemConfig);
    }

    @Override
    public @NotNull Map<Integer, Double> getTransitionsProbabilities(double r, int piExistenceClassId) {
        final Problem individual = myPiExistenceClassesManager.getAnyIndividualById(piExistenceClassId);
        final OneStepAlgorithm algorithm = OneStepAlgorithmsManager.createAlgorithm(r, myAlgorithmConfiguration, individual);
        final int beginFitness = individual.getFitness();
        final Map<Integer, Integer> cntOfId = new HashMap<>();
        int runNumber = 0;
        for (; !isStopConditionHit(runNumber); runNumber++) {
            algorithm.makeIteration();
            final BestCalculatedPatch patch = algorithm.getMutatedIndividual();
            final int newFitness = patch == null ? beginFitness : patch.fitness;
            final int newClassId = patch == null ? piExistenceClassId : myPiExistenceClassesManager.getIdByIndividual(individual, patch);
            update(cntOfId, newClassId);
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
