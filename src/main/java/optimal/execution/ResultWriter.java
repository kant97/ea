package optimal.execution;

import optimal.configuration.OneExperimentConfiguration;
import optimal.execution.events.EventType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ResultWriter implements AutoCloseable {
    private final static String[] entries = {"problemType", "algorithmType", "numberOfStepRepetitions", "problemSize"
            , "lambda", "beginFitness", "endFitness", "probabilityEnumerationStrategy", "fitness", "bestProbability",
            "optimizationTime"};
    private final Path outputPath;
    private final BufferedWriter writer;
    private final EventType myResultsType;

    public ResultWriter(String fileName, EventType resultsType) throws IOException {
        myResultsType = resultsType;
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

    public void writeResultsForMyType(ResultEntity results, EventType type) {
        if (myResultsType != type) {
            return;
        }
        OneExperimentConfiguration configuration = results.configuration;
        try {
            writer.write(configuration.problemType.toString());
            writer.write(',');
            writer.write(configuration.algorithmType.toString());
            writer.write(',');
            writer.write(configuration.getNumberOfStepRepetitions().toString());
            writer.write(',');
            writer.write(Integer.toString(configuration.problemSize));
            writer.write(',');
            writer.write(Integer.toString(configuration.lambda));
            writer.write(',');
            writer.write(Integer.toString(configuration.beginFitness));
            writer.write(',');
            writer.write(Integer.toString(configuration.endFitness));
            writer.write(',');
            writer.write(configuration.probabilityEnumerationStrategy.toString());
            writer.write(',');
            writer.write(Integer.toString(results.fitness));
            writer.write(',');
            writer.write(Double.toString(results.bestProbability));
            writer.write(',');
            writer.write(Double.toString(results.optimizationTime));
            writer.write('\n');
            writer.flush();
        } catch (IOException e) {
            System.err.println("Failed to log message with type: " + type.toString() + " and results: " + results.toString());
            e.printStackTrace();
        }

    }

    @Override
    public void close() throws Exception {
        writer.close();
    }
}
