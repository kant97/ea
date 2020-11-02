package problem;

import optimal.configuration.problems.ProblemConfig;
import optimal.configuration.problems.RuggednessConfig;

public class ProblemsManager {
    public enum ProblemType {
        ONE_MAX {
            @Override
            public String toString() {
                return "ONE_MAX";
            }
        }, LEADING_ONES {
            @Override
            public String toString() {
                return "LEADING_ONES";
            }
        }, ONE_MAX_NEUTRALITY_3 {
            @Override
            public String toString() {
                return "ONE_MAX_NEUTRALITY_3";
            }
        }, ONE_MAX_RUGGEDNESS {
            @Override
            public String toString() {
                return "ONE_MAX_RUGGEDNESS";
            }
        }, ONE_MAX_PLATEAU {
            @Override
            public String toString() {
                return "ONE_MAX_PLATEAU";
            }
        }
    }

    public static Problem createProblemInstance(ProblemType type, int n) {
        if (type.equals(ProblemType.ONE_MAX)) {
            return new OneMax(n);
        } else if (type.equals(ProblemType.LEADING_ONES)) {
            return new LeadingOnes(n);
        }
        throw new IllegalArgumentException("Problem with type: " + type.toString() + " is not supported yet");
    }

    public static Problem createProblemInstanceWithFixedFitness(ProblemType type, int n, int fitness) {
        if (type.equals(ProblemType.ONE_MAX)) {
            return new OneMax(n, fitness);
        } else if (type.equals(ProblemType.LEADING_ONES)) {
            throw new IllegalStateException("Leading ones is not supported yet");
        } else if (type.equals(ProblemType.ONE_MAX_NEUTRALITY_3)) {
            return new OneMaxNeutral3(n, fitness);
        } else if (type.equals(ProblemType.ONE_MAX_RUGGEDNESS)) {
            return new Ruggedness(n, 2, fitness);
        } else if (type.equals(ProblemType.ONE_MAX_PLATEAU)) {
            return new Plateau(n, 2, fitness);
        }
        throw new IllegalArgumentException("Problem with type: " + type.toString() + " is not supported yet");
    }

    public static Problem createProblemInstanceWithFixedFitness(ProblemConfig problemConfig, int fitness) {
        final ProblemType type = problemConfig.getProblemType();
        final int size = problemConfig.getSize();
        if (type == ProblemType.ONE_MAX) {
            return new OneMax(size, fitness);
        } else if (type == ProblemType.LEADING_ONES) {
            throw new IllegalStateException("Leading ones is not supported yet");
        } else if (type == ProblemType.ONE_MAX_NEUTRALITY_3) {
            return new OneMaxNeutral3(size, fitness);
        } else if (type == ProblemType.ONE_MAX_RUGGEDNESS) {
            final RuggednessConfig config = (RuggednessConfig) problemConfig;
            return new Ruggedness(config.getSize(), config.getR(), fitness);
        } else if (type == ProblemType.ONE_MAX_PLATEAU) {
            return new Plateau(size, 2, fitness);
        }
        throw new IllegalArgumentException("Problem with type: " + type.toString() + " is not supported yet");
    }

}
