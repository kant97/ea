package optimal.oneStepAlgorithms;

import algo.SimpleEA;
import org.jetbrains.annotations.Nullable;
import problem.Problem;
import utils.BestCalculatedPatch;

public class OneStepSimpleEA extends SimpleEA implements OneStepAlgorithm {
    private int newFitnessOfOffspring;
    private int initialFitnessOfOffspring;
    private double initialR;
    private BestCalculatedPatch patch = null;

    public OneStepSimpleEA(double probability, int lambda, Problem problem) {
        super(probability * problem.getLength(), 1. / problem.getLength() / problem.getLength(), lambda, problem);
        newFitnessOfOffspring = problem.getFitness();
        initialFitnessOfOffspring = newFitnessOfOffspring;
        initialR = super.getMutationRate();
    }

    @Override
    protected void updateProblemInstance(BestCalculatedPatch best) {
        patch = best;
        newFitnessOfOffspring = best.fitness;
    }

    @Override
    public void resetState() {
        newFitnessOfOffspring = initialFitnessOfOffspring;
        mutationRate = initialR;
        patch = null;
    }

    @Override
    public @Nullable BestCalculatedPatch getMutatedIndividual() {
        return patch;
    }

    @Override
    public int getFitness() {
        return newFitnessOfOffspring;
    }
}
