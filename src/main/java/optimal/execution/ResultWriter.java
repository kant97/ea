package optimal.execution;

import optimal.configuration.AbstractSingleExperimentConfiguration;
import optimal.configuration.CsvExportConfigurationVisitor;
import optimal.execution.events.EventType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class ResultWriter implements AutoCloseable {
    private final static String[] ENTRIES = {"problem", "algorithm", "problemSize"
            , "lambda", "beginFitness", "endFitness", "probabilityEnumeration", "vectorGeneration", "fitness",
            "probability", "optimizationTime"};
    private final Path outputPath;
    private final BufferedWriter writer;
    private final EventType myResultsType;

    public ResultWriter(String fileName, EventType resultsType, StandardOpenOption... options) throws IOException {
        myResultsType = resultsType;
        File file = new File(fileName);
        boolean needToWriteHeader = file.createNewFile();
        outputPath = file.toPath();
        writer = Files.newBufferedWriter(outputPath, options);
        if (Arrays.stream(options).noneMatch(option -> option == StandardOpenOption.APPEND)) {
            needToWriteHeader = true;
        }
        if (needToWriteHeader) {
            for (int i = 0; i < ENTRIES.length - 1; i++) {
                writer.write(ENTRIES[i]);
                writer.write(',');
            }
            writer.write(ENTRIES[ENTRIES.length - 1]);
            writer.write('\n');
            writer.flush();
        }
    }

    public void writeResultsForMyType(ResultEntity results, EventType type) {
        if (myResultsType != type) {
            return;
        }
        AbstractSingleExperimentConfiguration configuration = results.configuration;
        try {
            writer.write(configuration.accept(new CsvExportConfigurationVisitor()));
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
