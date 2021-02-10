package problem;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class EpistasisTest {

    @Test
    public void testEpistatisBuffTransformation1() {
        doTestEpistasisBuff(
                new boolean[]{false, false, false, false},
                new boolean[]{false, false, false, false});
    }

    @Test
    public void testEpistatisBuffTransformation2() {
        doTestEpistasisBuff(
                new boolean[]{false, false, false, true},
                new boolean[]{true, true, false, true});
    }

    @Test
    public void testEpistatisBuffTransformation3() {
        doTestEpistasisBuff(
                new boolean[]{false, false, true, false},
                new boolean[]{true, false, true, true});
    }

    @Test
    public void testEpistatisBuffTransformation4() {
        doTestEpistasisBuff(
                new boolean[]{false, true, false, false},
                new boolean[]{false, true, true, true});
    }

    @Test
    public void testEpistatisBuffTransformation5() {
        doTestEpistasisBuff(
                new boolean[]{false, true, true, true},
                new boolean[]{false, false, false, true});
    }

    @Test
    public void testEpistatisBuffTransformation6() {
        doTestEpistasisBuff(
                new boolean[]{false, false, true, true},
                new boolean[]{false, true, true, false});
    }

    @Test
    public void testEpistatisBuffTransformation7() {
        doTestEpistasisBuff(
                new boolean[]{false, true, false, true},
                new boolean[]{true, false, true, false});
    }

    @Test
    public void testEpistatisBuffTransformation8() {
        doTestEpistasisBuff(
                new boolean[]{false, true, true, false},
                new boolean[]{true, true, false, false});
    }

    @Test
    public void testEpistasisFitness1() {
        doTestEpistasisFitness(new boolean[]{false, false, false, false}, 0);
    }

    @Test
    public void testEpistasisFitness2() {
        doTestEpistasisFitness(
                new boolean[]{false, false, false, true},
                3);
    }

    @Test
    public void testEpistasisFitness3() {
        doTestEpistasisFitness(
                new boolean[]{false, false, true, false},
                3);
    }

    @Test
    public void testEpistasisFitness4() {
        doTestEpistasisFitness(
                new boolean[]{false, true, false, false},
                3);
    }

    @Test
    public void testEpistasisFitness5() {
        doTestEpistasisFitness(
                new boolean[]{false, true, true, true},
                1);
    }

    @Test
    public void testEpistasisFitness6() {
        doTestEpistasisFitness(
                new boolean[]{false, false, true, true},
                2);
    }

    @Test
    public void testEpistasisFitness7() {
        doTestEpistasisFitness(
                new boolean[]{false, true, false, true},
                2);
    }

    @Test
    public void testEpistasisFitness8() {
        doTestEpistasisFitness(
                new boolean[]{false, true, true, false},
                2);
    }

    @Test
    public void testEpistasisFitness9() {
        doTestEpistasisFitness(
                new boolean[]{true, false, false, false},
                4);
    }

    @Test
    public void testEpistasisFitness10() {
        doTestEpistasisFitness(
                new boolean[]{true, true, true, true},
                3);
    }

    @Test
    public void testEpistasisFitness11() {
        doTestEpistasisFitness(
                new boolean[]{true, false, false, true},
                1);
    }

//    @Test
//    public void testEpistatisBuffTransformationPatch1() {
//        doTestEpistasisWithPatch(
//                new boolean[]{true, false, false, true},
//                Arrays.asList(0, 3),
//                new boolean[]{false, false, false, false});
//    }
//
//    @Test
//    public void testEpistatisBuffTransformationPatch2() {
//        doTestEpistasisWithPatch(
//                new boolean[]{false, true, true, true},
//                Arrays.asList(1, 2),
//                new boolean[]{true, true, false, true});
//    }
//
//    @Test
//    public void testEpistatisBuffTransformationPatch3() {
//        doTestEpistasisWithPatch(
//                new boolean[]{false, false, true, true},
//                Arrays.asList(3),
//                new boolean[]{true, false, true, true});
//    }
//
//    @Test
//    public void testEpistatisBuffTransformationPatch4() {
//        doTestEpistasisWithPatch(
//                new boolean[]{true, false, false, false},
//                Arrays.asList(0, 1),
//                new boolean[]{false, true, true, true});
//    }
//
//    @Test
//    public void testEpistatisBuffTransformationPatch5() {
//        doTestEpistasisWithPatch(
//                new boolean[]{false, false, true, true},
//                Arrays.asList(2),
//                new boolean[]{false, false, false, true});
//    }
//
//    @Test
//    public void testEpistatisBuffTransformationPatch6() {
//        doTestEpistasisWithPatch(
//                new boolean[]{false, true, true, true},
//                Arrays.asList(1),
//                new boolean[]{false, true, true, false});
//    }
//
//    @Test
//    public void testEpistatisBuffTransformationPatch7() {
//        doTestEpistasisWithPatch(
//                new boolean[]{true, false, true, false},
//                Arrays.asList(0,1,2,3),
//                new boolean[]{true, false, true, false});
//    }
//
//    @Test
//    public void testEpistatisBuffTransformationPatch8() {
//        doTestEpistasisWithPatch(
//                new boolean[]{false, true, false, true},
//                Arrays.asList(2,3),
//                new boolean[]{true, true, false, false});
//    }

    private void doTestEpistasisWithPatch(boolean[] individual, List<Integer> patch, boolean[] expectedAfter) {
        Epistasis epistasis = new Epistasis(individual, 4);
        epistasis.getEpistasisOneGroupWithNonSetMSB(0, patch);
        Assertions.assertArrayEquals(expectedAfter, epistasis.buff);
    }

    private void doTestEpistasisFitness(boolean[] individual, int expectedFitness) {
        Epistasis epistasis = new Epistasis(individual, 4);
        Assertions.assertEquals(expectedFitness, epistasis.getFitness());
    }

    private void doTestEpistasisBuff(boolean[] individual, boolean[] expectedAfter) {
        Epistasis epistasis = new Epistasis(individual, 4);
        epistasis.getEpistasisOneGroupWithNonSetMSB(0, Collections.emptyList());
        Assertions.assertArrayEquals(expectedAfter, epistasis.buff);
    }

}