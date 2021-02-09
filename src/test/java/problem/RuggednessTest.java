package problem;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RuggednessTest {

    @Test
    public void testRuggednessGenerationWithZeroFitness() {
        final Ruggedness ruggedness = new Ruggedness(100, 2, 0);
        Assertions.assertEquals(0, ruggedness.getFitness());
    }

}