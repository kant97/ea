package pictures.processing;

import org.jetbrains.annotations.NotNull;

public final class AlgorithmData implements Comparable<AlgorithmData> {
    private final int iterationNumber, fitness;
    private final double mutationRate;

    public AlgorithmData(int iterationNumber, int fitness, double mutationRate) {
        this.iterationNumber = iterationNumber;
        this.fitness = fitness;
        this.mutationRate = mutationRate;
    }

    @Override
    public int compareTo(@NotNull AlgorithmData o) {
        if (fitness == o.fitness) {
            return Integer.compare(iterationNumber, o.iterationNumber);
        }
        return Integer.compare(fitness, o.fitness);
    }

    public int getIterationNumber() {
        return iterationNumber;
    }

    public int getFitness() {
        return fitness;
    }

    public double getMutationRate() {
        return mutationRate;
    }

    @Override
    public String toString() {
        return "AlgorithmData{" +
                "iterationNumber=" + iterationNumber +
                ", fitness=" + fitness +
                ", mutationRate=" + mutationRate +
                '}';
    }
}
