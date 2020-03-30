package algo;

import problem.Problem;

public class OneStepTwoRate extends TwoRate {
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
    protected void updateProblemInstance(TwoRate.BestCalculatedPatch bpHalf) {
        newFitnessOfOffspring = bpHalf.fitness;
    }

    public void resetState() {
        newFitnessOfOffspring = initialFitnessOfOffspring;
        mutationRate = initialR;
    }

    @Override
    public int getFitness() {
        return newFitnessOfOffspring;
    }
}
