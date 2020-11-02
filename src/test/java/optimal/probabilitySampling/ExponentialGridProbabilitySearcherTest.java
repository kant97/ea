package optimal.probabilitySampling;

import optimal.configuration.probability.ExponentialGridConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExponentialGridProbabilitySearcherTest {

    @Test
    void getNextProb() {
        ExponentialGridProbabilitySearcher searcher = new ExponentialGridProbabilitySearcher(
                new ExponentialGridConfiguration(2,1,10,1)
        );
        Assertions.assertFalse(searcher.isFinished());
        Assertions.assertEquals(2, searcher.getInitialProbability(), 0.0000001);
        Assertions.assertEquals(4, searcher.getNextProb(), 0.0000001);
        Assertions.assertEquals(8, searcher.getNextProb(), 0.0000001);
        Assertions.assertEquals(16, searcher.getNextProb(), 0.0000001);
        Assertions.assertEquals(32, searcher.getNextProb(), 0.0000001);
        Assertions.assertEquals(64, searcher.getNextProb(), 0.0000001);
        Assertions.assertFalse(searcher.isFinished());
        Assertions.assertEquals(128, searcher.getNextProb(), 0.0000001);
        Assertions.assertEquals(256, searcher.getNextProb(), 0.0000001);
        Assertions.assertEquals(512, searcher.getNextProb(), 0.0000001);
        Assertions.assertFalse(searcher.isFinished());
        Assertions.assertEquals(1024, searcher.getNextProb(), 0.0000001);
        Assertions.assertFalse(searcher.isFinished());
        searcher.getNextProb();
        Assertions.assertTrue(searcher.isFinished());
    }

    @Test
    void getNextProb2() {
        ExponentialGridProbabilitySearcher searcher = new ExponentialGridProbabilitySearcher(
                new ExponentialGridConfiguration(10,-2,-1,0.1)
        );
        Assertions.assertFalse(searcher.isFinished());
        Assertions.assertEquals(0.01, searcher.getInitialProbability(), 0.0000001);
        Assertions.assertEquals(0.0125, searcher.getNextProb(), 0.0001);
        Assertions.assertEquals(0.0158, searcher.getNextProb(), 0.0001);
        Assertions.assertEquals(0.0199, searcher.getNextProb(), 0.0001);
        Assertions.assertEquals(0.0251, searcher.getNextProb(), 0.0001);
        Assertions.assertEquals(0.0316, searcher.getNextProb(), 0.0001);
        Assertions.assertFalse(searcher.isFinished());
        Assertions.assertEquals(0.0398, searcher.getNextProb(), 0.0001);
        Assertions.assertEquals(0.0501, searcher.getNextProb(), 0.0001);
        Assertions.assertEquals(0.0630, searcher.getNextProb(), 0.0001);
        Assertions.assertFalse(searcher.isFinished());
        Assertions.assertEquals(0.0794, searcher.getNextProb(), 0.0001);
        Assertions.assertFalse(searcher.isFinished());
        Assertions.assertEquals(0.100, searcher.getNextProb(), 0.0001);
        Assertions.assertFalse(searcher.isFinished());
        searcher.getNextProb();
        Assertions.assertTrue(searcher.isFinished());
    }
}