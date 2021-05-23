package optimal.optimal2.generation;

import optimal.configuration.MainConfiguration;
import optimal.configuration.algorithms.AlgorithmConfig;
import optimal.configuration.probability.ExponentialGridConfiguration;
import optimal.configuration.problems.PlateauConfig;
import optimal.configuration.problems.ProblemConfig;
import optimal.configuration.runs.FixedSuccessConfiguration;
import optimal.configuration.transitionsGeneration.PrecomputedTransitionsReadingConfiguration;
import optimal.oneStepAlgorithms.OneStepAlgorithmsManager;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import problem.ProblemsManager;

import java.util.HashMap;
import java.util.Map;

import static optimal.configuration.transitionsGeneration.TransitionsGenerationConfiguration.TransitionsGenerationStrategy.READ_AND_RECOMPUTE_TRANSITIONS;

class PrecomputedTransitionsReaderAndProcessorTest {

    @Test
    public void testMethod2ProbabilitiesRecomputationPlateauFromC99R0() {
        Map<Integer, Double> transitionsForLambda1 = new HashMap<Integer, Double>() {{
            put(-2147483648, 5.0E-5);
            put(98, 0.990012);
            put(100, 0.009938);
        }};
        Map<Integer, Double> computed = new PrecomputedTransitionsReaderAndProcessor(createMainConfig(new PlateauConfig(ProblemsManager.ProblemType.ONE_MAX_PLATEAU, 100, 2), 512)).method2(transitionsForLambda1);
        Map<Integer, Double> correct = new HashMap<Integer, Double>() {{
            put(-2147483648, 0.);
            put(98, 0.006013740681220231);
            put(100, 0.993986259318781);
        }};
        Assertions.assertEquals(correct, computed);
    }

    @Test
    public void testMethod2ProbabilitiesRecomputationPlateauFromC98R0() {
        Map<Integer, Double> transitionsForLambda1 = new HashMap<Integer, Double>() {{
            put(-2147483648, 0.980046);
            put(99, 0.019954);
        }};
        Map<Integer, Double> computed = new PrecomputedTransitionsReaderAndProcessor(createMainConfig(new PlateauConfig(ProblemsManager.ProblemType.ONE_MAX_PLATEAU, 100, 2), 512)).method2(transitionsForLambda1);
        Map<Integer, Double> correct = new HashMap<Integer, Double>() {{
            put(-2147483648, 3.297522429137525E-5);
            put(99, 0.9999670247757062);
        }};
        Assertions.assertEquals(correct, computed);
    }

    @NotNull
    private MainConfiguration createMainConfig(@NotNull ProblemConfig problemConfig, int lambda) {
        return new MainConfiguration(problemConfig, new AlgorithmConfig(OneStepAlgorithmsManager.AlgorithmType.SIMPLE_ONE_PLUS_LAMBDA, lambda), 1, 51, new PrecomputedTransitionsReadingConfiguration(READ_AND_RECOMPUTE_TRANSITIONS, new ExponentialGridConfiguration(2.718281828459045, -9.210340371976182, 0.0, 0.09210340371976182), new FixedSuccessConfiguration(2500, 500000), "2-plateau_k2/transitions0", "2-plateau_k2/transitions0/global_configs.json"));
    }

}