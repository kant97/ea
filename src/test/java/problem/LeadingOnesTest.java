package problem;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class LeadingOnesTest {

    @Test
    void calculatePatchFitnessTestZeroIndividual() {
        boolean[] offspring = new boolean[10];
        LeadingOnes leadingOnes = new LeadingOnes(offspring);
        Assertions.assertEquals(0, leadingOnes.getFitness());
        List<Integer> patch1 = Arrays.asList(1, 2, 3);
        int patchFitness = leadingOnes.calculatePatchFitness(patch1);
        Assertions.assertEquals(0, patchFitness);
    }

    @Test
    void calculatePatchFitnessTestDeteriorateCase() {
        boolean[] offspring = new boolean[10];
        {
            offspring[0] = true;
            LeadingOnes leadingOnes = new LeadingOnes(offspring);
            Assertions.assertEquals(1, leadingOnes.getFitness());
            List<Integer> patch = Collections.singletonList(0);
            int patchFitness = leadingOnes.calculatePatchFitness(patch);
            Assertions.assertEquals(0, patchFitness);
        }
        {
            Arrays.fill(offspring, true);
            LeadingOnes leadingOnes = new LeadingOnes(offspring);
            Assertions.assertEquals(leadingOnes.getLength(), leadingOnes.getFitness());
            Assertions.assertTrue(leadingOnes.isOptimized());
            int patchFitness = leadingOnes.calculatePatchFitness(Arrays.asList(4, 5));
            Assertions.assertEquals(4, patchFitness);
        }
    }

    @Test
    void calculatePatchFitnessTestImproveCase() {
        {
            boolean[] offspring = new boolean[10];
            LeadingOnes leadingOnes = new LeadingOnes(offspring);
            Assertions.assertEquals(0, leadingOnes.getFitness());
            int patchFitness = leadingOnes.calculatePatchFitness(Collections.singletonList(0));
            Assertions.assertEquals(1, patchFitness);
            patchFitness = leadingOnes.calculatePatchFitness(Arrays.asList(0, 1, 2, 3, 4, 5, 10));
            Assertions.assertEquals(6, patchFitness);
        }
        {
            boolean[] offspring = new boolean[10];
            offspring[0] = true;
            offspring[1] = true;
            offspring[3] = true;
            offspring[9] = true;
            LeadingOnes leadingOnes = new LeadingOnes(offspring);
            Assertions.assertEquals(2, leadingOnes.getFitness());
            int patchFitness = leadingOnes.calculatePatchFitness(Collections.singletonList(2));
            Assertions.assertEquals(patchFitness, 4);
        }
        {
            boolean[] offspring = new boolean[10];
            Arrays.fill(offspring, true);
            offspring[4] = false;
            LeadingOnes leadingOnes = new LeadingOnes(offspring);
            int patchFitness = leadingOnes.calculatePatchFitness(Collections.singletonList(4));
            Assertions.assertEquals(leadingOnes.getLength(), patchFitness);
        }
        {
            boolean[] offspring = new boolean[10];
            Arrays.fill(offspring, true);
            offspring[4] = false;
            LeadingOnes leadingOnes = new LeadingOnes(offspring);
            int patchFitness = leadingOnes.calculatePatchFitness(Arrays.asList(4, 5));
            Assertions.assertEquals(5, patchFitness);
        }
    }

    @Test
    void calculatePatchFitnessTestNotChangeCase() {
        boolean[] offspring = new boolean[10];
        Arrays.fill(offspring, true);
        offspring[4] = false;
        LeadingOnes leadingOnes = new LeadingOnes(offspring);
        Assertions.assertEquals(4, leadingOnes.getFitness());
        int patchFitness = leadingOnes.calculatePatchFitness(Arrays.asList(5, 6));
        Assertions.assertEquals(4, patchFitness);
    }

    @Test
    void calculatePatchFitnessTestBadPatchCase() {
        boolean[] offspring = new boolean[10];
        Arrays.fill(offspring, true);
        offspring[9] = false;
        LeadingOnes leadingOnes = new LeadingOnes(offspring);
        Assertions.assertEquals(9, leadingOnes.getFitness());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> leadingOnes.calculatePatchFitness(Arrays.asList(6, 5)));
    }
}