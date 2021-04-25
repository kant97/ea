package optimal;

import algo.ABalgo;
import algo.TwoRate;
import org.jetbrains.annotations.NotNull;
import problem.Plateau;
import problem.Ruggedness;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class RunTimeMeasurer implements AutoCloseable {
    private final @NotNull BufferedWriter myWriter;

    public RunTimeMeasurer(@NotNull String fileName) {
        try {
            myWriter = new BufferedWriter(new FileWriter(fileName));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        logHeader();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            final RunTimeMeasurer twoRateRunTimeMeasurer = new RunTimeMeasurer("abRun" + i + ".csv");
            twoRateRunTimeMeasurer.runAb();
            twoRateRunTimeMeasurer.close();
        }
        for (int i = 0; i < 3; i++) {
            final RunTimeMeasurer twoRateRunTimeMeasurer = new RunTimeMeasurer("twoRateRun" + i + ".csv");
            twoRateRunTimeMeasurer.runTwoRate();
            twoRateRunTimeMeasurer.close();
        }
    }

    public void runTwoRate() {
        final TwoRate twoRate = new TwoRate(1., 1. / 100., 512, new Ruggedness(100, 2, 0));
        int iterationNumber = 0;
        log(iterationNumber, twoRate.getFitness(), twoRate.getMutationRate());
        while (!twoRate.isFinished()) {
            twoRate.makeIteration();
            iterationNumber++;
            log(iterationNumber, twoRate.getFitness(), twoRate.getMutationRateUsedInBestMutation());
        }
    }

    public void runAb() {
        final ABalgo aBalgo = new ABalgo(1. / 100., 2, 0.5, 1. / 10000., 512, true, new Ruggedness(100, 2, 0));
        int iterationNumber = 0;
        log(iterationNumber, aBalgo.getFitness(), aBalgo.getMutationRate());
        while (!aBalgo.isFinished()) {
            aBalgo.makeIteration();
            iterationNumber++;
            log(iterationNumber, aBalgo.getFitness(), aBalgo.getMutationRateUsedInBestMutation());
        }
    }

    private void logHeader() {
        try {
            myWriter.write("iterationNumber,fitness,mutationRate\n");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void log(int iterationNumber, int fitness, double usedMutationRate) {
        try {
            myWriter.write(iterationNumber + "," + fitness + "," + usedMutationRate + "\n");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void close() {
        try {
            myWriter.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
