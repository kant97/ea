package optimal;

import algo.SimpleEA;
import problem.Problem;
import problem.ProblemsManager;

public class RunTimeMeasurer {
    public static void main(String[] args) {
        final int RUNS = 1000;
        long sumGenerations = 0;
        for (int i = 0; i < RUNS; i++) {
            final Problem OneMaxInstanceWithFixedFitness =
                    ProblemsManager.createProblemInstanceWithFixedFitness(ProblemsManager.ProblemType.ONE_MAX_RUGGEDNESS,
                            100, 50);
            final SimpleEA oPLEA = new SimpleEA(1., 16, OneMaxInstanceWithFixedFitness);
            while (!oPLEA.isFinished()) {
                oPLEA.makeIteration();
            }
            final long iterCount = oPLEA.getIterCount();
            System.out.println("Generations amount of run " + i + " is: " + iterCount);
            sumGenerations += iterCount;
        }
        System.out.println("-------------------------------------- Finished");
        System.out.println("Average generations amount is: " + sumGenerations / RUNS);

    }
}
