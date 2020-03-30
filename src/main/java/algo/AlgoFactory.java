package algo;

import problem.Problem;
import problem.ProblemsManager;

public interface AlgoFactory {
    Algorithm getInstance (int lambda, double lowerBound, ProblemsManager.ProblemType type, int problemLength);
}
