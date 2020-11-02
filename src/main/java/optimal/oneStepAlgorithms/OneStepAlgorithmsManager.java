package optimal.oneStepAlgorithms;

import optimal.configuration.algorithms.AlgorithmConfig;
import optimal.configuration.algorithms.TwoRateConfig;
import org.jetbrains.annotations.NotNull;
import problem.Problem;

public class OneStepAlgorithmsManager {
    public enum AlgorithmType {
        TWO_RATE {
            @Override
            public String toString() {
                return "TwoRate";
            }
        }, SIMPLE_ONE_PLUS_LAMBDA {
            @Override
            public String toString() {
                return "OnePlusLambda";
            }
        }
    }

    public static OneStepAlgorithm createAlgorithm(double currentProbability, double lowerBound,
                                                   int lambda, Problem problem, AlgorithmType type) {
        if (type == AlgorithmType.TWO_RATE) {
            return new OneStepTwoRate(currentProbability, lowerBound, lambda, problem);
        } else if (type == AlgorithmType.SIMPLE_ONE_PLUS_LAMBDA) {
            return new OneStepSimpleEA(currentProbability, lambda, problem);
        }
        throw new IllegalArgumentException("Algorithm type " + type.toString() + "is not supported yet");
    }

    public static OneStepAlgorithm createAlgorithm(double currentProbability, @NotNull AlgorithmConfig algorithmConfig, @NotNull Problem problem) {
        final AlgorithmType algorithmType = algorithmConfig.getAlgorithmType();
        if (algorithmType == AlgorithmType.TWO_RATE) {
            final TwoRateConfig config = (TwoRateConfig) algorithmConfig;
            return new OneStepTwoRate(currentProbability, config.getLowerBound(), config.getLambda(),
                    problem);
        } else if (algorithmType == AlgorithmType.SIMPLE_ONE_PLUS_LAMBDA) {
            return new OneStepSimpleEA(currentProbability, algorithmConfig.getLambda(), problem);
        }
        throw new IllegalArgumentException("Algorithm type " + algorithmType.toString() + " is not supported yet");
    }

}
