package optimal.oneStepAlgorithms;

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
}
