package optimal.optimal2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class BestMutationRateSearcher2Test {

    @Test
    public void testSolveSystem() {
        List<List<Double>> p = new ArrayList<>();
        p.add(Arrays.asList(1., 1.));
        p.add(Arrays.asList(1., -1.));
        List<Double> c = Arrays.asList(2., 0.);
        BestMutationRateSearcher2 bestMutationRateSearcher2 = new BestMutationRateSearcher2();
        List<Double> doubles = bestMutationRateSearcher2.solveSystem(p, c);
        Assertions.assertEquals(Arrays.asList(1., 1.), doubles);
    }

}