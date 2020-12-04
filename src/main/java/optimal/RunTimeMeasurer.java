package optimal;

import algo.ABalgo;
import algo.TwoRate;
import org.jetbrains.annotations.NotNull;
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
        {
            final RunTimeMeasurer twoRateRunTimeMeasurer = new RunTimeMeasurer("abRun.csv");
            twoRateRunTimeMeasurer.runAb();
            twoRateRunTimeMeasurer.close();
        }
        {
            final RunTimeMeasurer twoRateRunTimeMeasurer = new RunTimeMeasurer("twoRateRun.csv");
            twoRateRunTimeMeasurer.runTwoRate();
            twoRateRunTimeMeasurer.close();
        }
    }

    public void runTwoRate() {
        final TwoRate twoRate = new TwoRate(1., 1. / 10_000., 32, new Ruggedness(100, 2, 50));
        int iterationNumber = 0;
        log(iterationNumber, twoRate.getFitness(), twoRate.getMutationRate());
        while (!twoRate.isFinished()) {
            twoRate.makeIteration();
            iterationNumber++;
            log(iterationNumber, twoRate.getFitness(), twoRate.getMutationRateUsedInBestMutation());
        }
    }

    public void runAb() {
        final ABalgo aBalgo = new ABalgo(1. / 100., 2, 0.5, 1. / 100., 32, true, new Ruggedness(100, 2, 50));
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
