package problem;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Epistasis implements Problem {
    private boolean[] individual;
    private int fitness;
    private final int v;
    protected boolean[] buff;

    public Epistasis(int n, int v) {
        this.v = v;
        individual = new boolean[n];
        buff = new boolean[v];
        Random rand = ThreadLocalRandom.current();
        for (int i = 0; i < n; ++i) {
            individual[i] = rand.nextBoolean();
        }
        this.fitness = getEpistasisFitness(Collections.emptyList());
    }

    protected Epistasis(boolean[] individual, int v) {
        this.v = v;
        this.individual = individual;
        this.buff = new boolean[v];
        this.fitness = getEpistasisFitness(Collections.emptyList());
    }

    protected int getEpistasisFitness(List<Integer> patch) {
        int myFitness = 0;
        for (int groupBegin = 0; groupBegin < individual.length; groupBegin += v) {
            if (individual[groupBegin]) {
                individual[groupBegin] = false;
                getEpistasisOneGroupWithNonSetMSB(groupBegin, patch);
                for (boolean b : buff) {
                    if (!b) myFitness++;
                }
                individual[groupBegin] = true;
            } else {
                getEpistasisOneGroupWithNonSetMSB(groupBegin, patch);
                for (boolean b : buff) {
                    if (b) myFitness++;
                }
            }
        }
        return myFitness;
    }

    protected void getEpistasisOneGroupWithNonSetMSB(int groupBegin, List<Integer> patch) {
        int patchPosition = 0;
        for (int i = groupBegin; i < groupBegin + v; i++) {
            boolean cur = false;
            for (int j = groupBegin; j < groupBegin + v; j++) {
                if (j != (i + 1) % v) { // in paper they have minus, but with minus their examples dont work
                    boolean curBit = individual[j];
                    if (patchPosition < patch.size() && j == patch.get(patchPosition)) {
                        curBit = !curBit;
                        patchPosition++;
                    }
                    cur ^= curBit;
                }
            }
            buff[i] = cur;
        }
    }

    @Override
    public int calculatePatchFitness(List<Integer> patch) {
        return getEpistasisFitness(patch);
    }

    @Override
    public void applyPatch(List<Integer> patch, int fitness) {
        for (int i: patch) {
            individual[i] = !individual[i];
        }
        this.fitness = fitness;
    }

    @Override
    public int getFitness() {
        return fitness;
    }

    @Override
    public int getOnesCount(int fitness) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public int getLength() {
        return individual.length;
    }

    @Override
    public boolean isOptimized() {
        return fitness == individual.length;
    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public int getOptimum() {
        return individual.length;
    }
}
