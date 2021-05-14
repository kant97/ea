package optimal.utils;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractCsvProcessor<T> {
    protected final boolean ignoreFirstLine;
    protected final String delimiter;

    public AbstractCsvProcessor(boolean ignoreFirstLine, @NotNull String delimiter) {
        this.ignoreFirstLine = ignoreFirstLine;
        this.delimiter = delimiter;
    }

    public AbstractCsvProcessor() {
        ignoreFirstLine = true;
        delimiter = ",";
    }

    public T loadAndGetProcessedData(@NotNull String csvFileName) throws IOException, CorruptedCsvException {
        final List<List<String>> records = new ArrayList<>();
        try (final BufferedReader csvReader = new BufferedReader(new FileReader(csvFileName))) {
            int rowNumber = 0;
            String line;
            while ((line = csvReader.readLine()) != null) {
                if (ignoreFirstLine && rowNumber < 1) {
                    rowNumber++;
                    continue;
                }
                String[] values = line.split(delimiter);
                records.add(Arrays.asList(values));
            }
        }
        return processData(records);
    }

    protected abstract T processData(@NotNull List<List<String>> data) throws CorruptedCsvException;
}
