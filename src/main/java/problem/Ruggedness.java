package problem;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Ruggedness implements Problem {
    private final int realOptimumBacket;
    private boolean[] individual;
    private final int length;
    private int fitness;
    private int onesCount;
    private final int r;
    private final int optimum;
    private final HashMap<Integer, Integer> lastBucketMapping = new HashMap<>();

    public Ruggedness(int n, int r) {
        individual = new boolean[n];
        length = n;
        Random rand = ThreadLocalRandom.current();
        this.r = r;
        int om = 0;
        realOptimumBacket = n / r;
        remapTheLastBacket(n, r);
        optimum = countFitness(n);
        for (int i = 0; i < n; ++i) {
            individual[i] = rand.nextBoolean();
            if (individual[i]) {
                om++;
            }
        }
        onesCount = om;
        fitness = countFitness(om);
    }

    public Ruggedness(int n, int r, int fitness) {
        individual = new boolean[n];
        length = n;
        this.r = r;
        realOptimumBacket = n / r;
        remapTheLastBacket(n, r);
        optimum = countFitness(n);
        final int realFitness = countFitness(fitness);
        individual = OneMax.generateOneMaxOffspringWithFitness(individual, 0, realFitness);
        this.onesCount = realFitness;
        this.fitness = fitness;
    }

    public void remapTheLastBacket(int n, int r) {
        final int firstNumberInLastBucket = realOptimumBacket * r;
        for (int i = firstNumberInLastBucket; i <= n; i++) {
            lastBucketMapping.put(i, n - (i - firstNumberInLastBucket));
        }
    }

    private int countFitness(int om) {
        if (r == 1) {
//            if (om == length) {
//                return om / 2 + 2;
//            }
            return om / 2 + 1 + om % 2;
//            }
        } else {
//            if (om == length) {
//                return length;
//            }

            final int omBucket = om / r;
            if (realOptimumBacket == omBucket) {
                return lastBucketMapping.get(om);
            }
            return r * omBucket + r - 1 - om % r;
        }
    }

    @Override
    public int calculatePatchFitness(List<Integer> patch) {
        int newOnesCount = onesCount;
        for (Integer i : patch) {
            if (individual[i]) {
                newOnesCount--;
            } else {
                newOnesCount++;
            }
        }
        return countFitness(newOnesCount);
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
        this.fitness = fitness;
    }

    @Override
    public int getFitness() {
        return fitness;
    }

    @Override
    public int getLength() {
        return length;
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
        return optimum;
    }
}
