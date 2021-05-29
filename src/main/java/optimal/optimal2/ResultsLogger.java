package optimal.optimal2;

import optimal.configuration.CsvExportConfigurationVisitor;
import optimal.configuration.MainConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ResultsLogger implements AutoCloseable {
    private final static String[] ENTRIES = {"piExistenceClassId", "probability", "optimizationTime"};
    private final int extraDataLen = 4;
    private final @NotNull Path outputPath;
    private final @NotNull BufferedWriter writer;

    public ResultsLogger(@NotNull String fileName) {
        final File file = new File(fileName);
        try {
            file.createNewFile();
            outputPath = file.toPath();
            this.writer = Files.newBufferedWriter(outputPath);
            for (int i = 0; i < ENTRIES.length - 1; i++) {
                writer.write(ENTRIES[i]);
                writer.write(',');
            }
            writer.write(ENTRIES[ENTRIES.length - 1]);
            writer.write('\n');
            writer.flush();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void close() throws Exception {
        writer.close();
    }

    public void logResults(ResultsContainer results) {
        try {
            for (int i = 0; i < ENTRIES.length; i++) {
                writer.write(results.getExtraDataValue(ENTRIES[i]));
                writer.write(i == ENTRIES.length - 1 ? '\n' : ',');
            }
            writer.flush();
        } catch (IOException e) {
            System.err.println("Failed to log message results: " + results);
            e.printStackTrace();
        }
    }

}