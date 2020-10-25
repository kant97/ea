package optimal.heuristics;

import problem.ProblemsManager;

public class ExperimentState {
    private final ProblemsManager.ProblemType problemType;
    private final int fitness;
    private final double probability;

    public ExperimentState(ProblemsManager.ProblemType problemType, int fitness, double probability) {
        this.problemType = problemType;
        this.fitness = fitness;
        this.probability = probability;
    }

    public ProblemsManager.ProblemType getProblemType() {
        return problemType;
    }

    public int getFitness() {
        return fitness;
    }

    public double getProbability() {
        return probability;
    }
}
