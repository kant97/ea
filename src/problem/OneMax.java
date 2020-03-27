package problem;

import java.util.*;

public class OneMax extends AbstractProblem {

    public OneMax(int n) {
        super(n);
    }

    public OneMax(int n, int fitness) {
        super(n);

        boolean zerosToFlip = (fitness - myFitness) > 0;
        ArrayList<Integer> toFlip = new ArrayList<>(myOffspring.length / 2);

        for (int i = 0; i < myOffspring.length; i++) {
            if (zerosToFlip) {
                if (!myOffspring[i]) {
                    toFlip.add(i);
                }
            } else {
                if (myOffspring[i]) {
                    toFlip.add(i);
                }
            }
        }

        int toFlipAmount = Math.abs(fitness - myFitness);
        Random random = new Random();
        for (int i = 0; i < toFlipAmount; i++) {
            int r = random.nextInt(toFlip.size());
            int removed = toFlip.remove(r);
            myOffspring[removed] = !myOffspring[removed];
        }
        myFitness = calcOneMaxFitness(myOffspring);
        assert fitness == myFitness;
    }

    public static int calcOneMaxFitness(boolean[] myOffspring) {
        int fit = 0;
        for (boolean b : myOffspring) {
            if (b) {
                fit++;
            }
        }
        return fit;
    }

    @Override
    protected int getHonestFitness() {
        return calcOneMaxFitness(myOffspring);
    }

    @Override
    public int calculatePatchFitness(List<Integer> patch) {
        int newFitness = myFitness;
        for (Integer i : patch) {
            if (myOffspring[i]) {
                newFitness--;
            } else {
                newFitness++;
            }
        }
        return newFitness;
    }

    @Override
    public String getInfo() {
        return "";
    }
}
