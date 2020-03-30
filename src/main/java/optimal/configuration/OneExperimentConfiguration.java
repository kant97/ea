package optimal.configuration;

import problem.ProblemsManager;

public class OneExperimentConfiguration {
    public final ProblemsManager.ProblemType problemType;
    public final int problemSize;
    public final int lambda;
    public final int beginFitness;
    public final int endFitness;
    public final double minMutationProbability;
    public final double maxMutationProbability;
    public final double precisionForProbability;

    public OneExperimentConfiguration(ProblemsManager.ProblemType problemType, int problemSize, int lambda, int beginFitness, int endFitness, double minMutationProbability, double maxMutationProbability, double precisionForProbability) {
        this.problemType = problemType;
        this.problemSize = problemSize;
        this.lambda = lambda;
        this.beginFitness = beginFitness;
        this.endFitness = endFitness;
        this.minMutationProbability = minMutationProbability;
        this.maxMutationProbability = maxMutationProbability;
        this.precisionForProbability = precisionForProbability;
    }
}
