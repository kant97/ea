package pictures;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class DataProcessor<T> {
    private static final String COMMA_DELIMITER = ",";
    protected final @NotNull String csvFileName;
    protected List<List<String>> myRecords = null;

    public DataProcessor(@NotNull String csvFileName) {
        this.csvFileName = csvFileName;
    }

    public void loadData() {
        myRecords = new ArrayList<>();
        int colNumber = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(csvFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (colNumber < 1) {
                    colNumber++;
                    continue;
                }
                String[] values = line.split(COMMA_DELIMITER);
                myRecords.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract T getProcessedData();
}
