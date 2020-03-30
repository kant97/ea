package problem;

import java.util.List;

public class LeadingOnes extends AbstractProblem {

    public LeadingOnes(int lengthOfOffspring) {
        super(lengthOfOffspring);
    }

    // used for tests only
    LeadingOnes(boolean[] offspring) {
        super(offspring.length);
        myOffspring = offspring;
        myFitness = getHonestFitness();
    }

    @Override
    protected int getHonestFitness() {
        int fitness = 0;
        for (boolean bit : myOffspring) {
            if (!bit) {
                return fitness;
            }
            fitness++;
        }
        return fitness;
    }

    @Override
    public int calculatePatchFitness(List<Integer> patch) {
        int patchFitness = myFitness;
        int prevInvertedIndex = -1;
        for (Integer invertedBitIndex : patch) {
            if (invertedBitIndex <= prevInvertedIndex) {
                throw new IllegalArgumentException("Patch should be sorted here, but it does not");
            }
            prevInvertedIndex = invertedBitIndex;
        }
        for (Integer invertedBitIndex : patch) {
            if (invertedBitIndex < patchFitness) {
                // deteriorate current offspring
                assert myOffspring[invertedBitIndex];
                patchFitness = invertedBitIndex;
                return patchFitness;
            }
            if (invertedBitIndex == patchFitness) {
                // improve current offspring
                assert !myOffspring[myFitness];
                patchFitness++;
                for (int i = myFitness + 1; i < myOffspring.length; i++) {
                    if (myOffspring[i]) {
                        patchFitness++;
                    } else {
                        break;
                    }
                }
            } else {
                // not change current offspring
                return patchFitness;
            }
        }
        return patchFitness;
    }

    @Override
    public String getInfo() {
        return null;
    }

}
