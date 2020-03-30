package optimal;

public interface OptimizationParametersSearchingListener {
    void onNewOptimizationTimeForFitness(int fitness, double time);

    void onNewMutationProbabilityForFitness(int fitness, double mutationRate);
}
