package problem;

import java.util.List;
import java.util.Random;

public abstract class AbstractProblem implements Problem {
    protected boolean[] myOffspring;
    protected int myFitness;

    protected AbstractProblem(int lengthOfOffspring) {
        myOffspring = new boolean[lengthOfOffspring];
        Random rand = new Random();
        for (int i = 0; i < lengthOfOffspring; ++i) {
            myOffspring[i] = rand.nextBoolean();
        }
        myFitness = getHonestFitness();
    }

    protected abstract int getHonestFitness();

    @Override
    public void applyPatch(List<Integer> patch, int fitness) {
        for (Integer i : patch) {
            myOffspring[i] = !myOffspring[i];
        }
        this.myFitness = fitness;
    }

    @Override
    public int getFitness() {
        return myFitness;
    }

    @Override
    public int getLength() {
        return myOffspring.length;
    }

    @Override
    public boolean isOptimized() {
        return myFitness == getLength();
    }
}
