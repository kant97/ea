package optimal.oneStepAlgorithms;

import algo.TwoRate;
import org.jetbrains.annotations.Nullable;
import problem.Problem;
import utils.BestCalculatedPatch;

public class OneStepTwoRate extends TwoRate implements OneStepAlgorithm {
    private int newFitnessOfOffspring;
    private int initialFitnessOfOffspring;
    private double initialR;

    public OneStepTwoRate(double probability, double lowerBound, int lambda, Problem problem) {
        super(probability * problem.getLength(), lowerBound, lambda, problem);
        newFitnessOfOffspring = problem.getFitness();
        initialFitnessOfOffspring = newFitnessOfOffspring;
        initialR = mutationRate;
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    @Override
    protected void updateProblemInstance(BestCalculatedPatch bpHalf) {
        newFitnessOfOffspring = bpHalf.fitness;
    }

    @Override
    public void resetState() {
        newFitnessOfOffspring = initialFitnessOfOffspring;
        mutationRate = initialR;
    }

    @Override
    public @Nullable BestCalculatedPatch getMutatedIndividual() {
        return null;
    }

    @Override
    public int getFitness() {
        return newFitnessOfOffspring;
    }
}
