package optimal.probabilitySampling;

import optimal.configuration.probability.IterativeProbabilityConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IterativeProbabilitySpaceTest {
    @Test
    public void testMaxProbabilityIncluding() {
        final IterativeProbabilityConfiguration configuration = new IterativeProbabilityConfiguration(0.5, 1, 0.5);
        final List<Double> expected = Arrays.asList(0.5, 1.);
        compare(expected, configuration);
    }

    @Test
    public void testMaxProbabilityIncluding2() {
        final IterativeProbabilityConfiguration configuration = new IterativeProbabilityConfiguration(0.6, 1, 0.5);
        final List<Double> expected = Arrays.asList(0.6, 1.);
        compare(expected, configuration);
    }

    @Test
    public void testMaxProbabilityIncluding3() {
        final IterativeProbabilityConfiguration configuration = new IterativeProbabilityConfiguration(0.5, 1, 0.2);
        final List<Double> expected = Arrays.asList(0.5, 0.7, 0.9, 1.);
        compare(expected, configuration);
    }

    private void compare(List<Double> expected, IterativeProbabilityConfiguration configuration) {
        final ProbabilitySpace probabilitySpace = ProbabilitySpace.createProbabilitySpace(configuration);
        final ArrayList<Double> have = new ArrayList<>();
        for (double r = probabilitySpace.getInitialProbability(); !probabilitySpace.isFinished(); r = probabilitySpace.getNextProb()) {
            have.add(r);
        }
        double epsilon = 1.;
        for (int i = 1; i < expected.size(); i++) {
            epsilon = Math.min((expected.get(i) - expected.get(i - 1)) / 2., epsilon);
        }
        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertTrue(Math.abs(expected.get(i) - have.get(i)) < epsilon,
                    expected.get(i) + " != " + have.get(i) + "\nExpected: " + expected + "\nActual: " + have);
        }
    }
}