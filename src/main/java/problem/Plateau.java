package problem;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Plateau implements Problem {
    private boolean[] individual;
    private int fitness;
    private int onesCount;
    private final int k;
    private final int optimum;

    public Plateau(int n, int k) {
        individual = new boolean[n];
        Random rand = ThreadLocalRandom.current();
        onesCount = 0;
        this.k = k;
        for (int i = 0; i < n; ++i) {
            individual[i] = rand.nextBoolean();
            if (individual[i]) {
                onesCount++;
            }
        }
        fitness = (int) Math.floor(((double) onesCount) / k) + 1;
        optimum = (int) Math.floor(((double) n) / k) + 1;;
    }

    @Override
    public int calculatePatchFitness(List<Integer> patch) {
        int newFitness = onesCount;
        for (Integer i : patch) {
            if (individual[i]) {
                newFitness--;
            } else {
                newFitness++;
            }
        }
        int res = (int) Math.floor(((double) newFitness) / k) + 1;
        return res;
    }

    @Override
    public void applyPatch(List<Integer> patch, int fitness) {
        for (Integer i : patch) {
            if (individual[i]) {
                onesCount--;
            } else {
                onesCount++;
            }
            individual[i] = !individual[i];
        }
//        System.out.println(onesCount + " " + fitness);
        this.fitness = fitness;
    }

    @Override
    public int getFitness() {
        return fitness;
    }

    @Override
    public int getLength() {
        return individual.length;
    }

    @Override
    public boolean isOptimized() {
        return fitness == optimum;
    }

    @Override
    public String getInfo() {
        return "";
    }

    @Override
    public int getOptimum() {
        return 0;
    }
}

