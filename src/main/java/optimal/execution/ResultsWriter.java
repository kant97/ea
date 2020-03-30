package optimal.execution;

import optimal.configuration.OneExperimentConfiguration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.LinkedBlockingQueue;

public class ResultsWriter implements Runnable {
    protected final LinkedBlockingQueue<ResultEntity> queue = new LinkedBlockingQueue<>();
    private final Path outputPath;
    private final static String[] entries = {"problemType", "problemSize", "lambda", "beginFitness", "endFitness", "minMutationProbability", "maxMutationProbability", "precisionForProbability", "fitness", "bestProbability", "optimizationTime"};
    private final BufferedWriter writer;

    public ResultsWriter() throws URISyntaxException, IOException {
        this("results.csv");
    }

    ResultsWriter(String fileName) throws IOException {
        File file = new File(fileName);
        boolean isNewFile = file.createNewFile();
        outputPath = file.toPath();
        writer = Files.newBufferedWriter(outputPath, StandardOpenOption.APPEND);
        if (isNewFile) {
            for (int i = 0; i < entries.length - 1; i++) {
                writer.write(entries[i]);
                writer.write(',');
            }
            writer.write(entries[entries.length - 1]);
            writer.write('\n');
            writer.flush();
        }
    }

    public void waitAndLogResults() throws InterruptedException, IOException {
        ResultEntity entity = queue.take();

        OneExperimentConfiguration configuration = entity.configuration;
        writer.write(configuration.problemType.toString());
        writer.write(',');
        writer.write(Integer.toString(configuration.problemSize));
        writer.write(',');
        writer.write(Integer.toString(configuration.lambda));
        writer.write(',');
        writer.write(Integer.toString(configuration.beginFitness));
        writer.write(',');
        writer.write(Integer.toString(configuration.endFitness));
        writer.write(',');
        writer.write(Double.toString(configuration.minMutationProbability));
        writer.write(',');
        writer.write(Double.toString(configuration.maxMutationProbability));
        writer.write(',');
        writer.write(Double.toString(configuration.precisionForProbability));
        writer.write(',');
        writer.write(Integer.toString(entity.fitness));
        writer.write(',');
        writer.write(Double.toString(entity.bestProbability));
        writer.write(',');
        writer.write(Double.toString(entity.optimizationTime));
        writer.write('\n');
        writer.flush();
    }


    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                waitAndLogResults();
            } catch (InterruptedException e) {
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void addToQueue(ResultEntity e) {
        queue.add(e);
    }
}
