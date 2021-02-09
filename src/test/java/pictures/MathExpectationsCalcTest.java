package pictures;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class MathExpectationsCalcTest {

    @Test
    public void testCnCalculation() {
        doTestCnkCalc(3, Arrays.asList(1, 3, 3, 1));
        doTestCnkCalc(4, Arrays.asList(1, 4, 6, 4, 1));
    }

    private void doTestCnkCalc(int n, List<Integer> ints) {
        final MathExpectationsCalc mathExpectationsCalc = new MathExpectationsCalc(n, null);
        Assertions.assertArrayEquals(createBigIntegerList(ints).toArray(), mathExpectationsCalc.Cn.toArray());
    }

    private ArrayList<BigInteger> createBigIntegerList(List<Integer> ints) {
        final ArrayList<BigInteger> ans = new ArrayList<>();
        for (int i : ints) {
            ans.add(BigInteger.valueOf(i));
        }
        return ans;
    }

}