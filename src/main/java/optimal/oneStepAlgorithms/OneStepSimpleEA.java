package optimal.oneStepAlgorithms;

import algo.SimpleEA;
import problem.Problem;
import utils.BestCalculatedPatch;

public class OneStepSimpleEA extends SimpleEA implements OneStepAlgorithm {
    private int newFitnessOfOffspring;
    private int initialFitnessOfOffspring;
    private double initialR;

    public OneStepSimpleEA(double probability, int lambda, Problem problem) {
        super(probability * problem.getLength(), 1. / problem.getLength() / problem.getLength(), lambda, problem);
        newFitnessOfOffspring = problem.getFitness();
        initialFitnessOfOffspring = newFitnessOfOffspring;
        initialR = super.getMutationRate();
    }

    @Override
    protected void updateProblemInstance(BestCalculatedPatch best) {
        newFitnessOfOffspring = best.fitness;
    }

    @Override
    public void resetState() {
        newFitnessOfOffspring = initialFitnessOfOffspring;
        mutationRate = initialR;
    }

    @Override
    public int getFitness() {
        return newFitnessOfOffspring;
    }
}
