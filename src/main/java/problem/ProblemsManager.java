package problem;

public class ProblemsManager {
    public enum ProblemType {
        ONE_MAX, LEADING_ONES, ONE_MAX_NEUTRALITY_3
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
        }
        throw new IllegalArgumentException("Problem with type: " + type.toString() + " is not supported yet");
    }
}
