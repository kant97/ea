package pictures.optimal2Pictures;

import javafx.util.Pair;
import optimal.utils.AbstractCsvProcessor;
import optimal.utils.CorruptedCsvException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AlgorithmTrace {
    private final List<Pair<Integer, Double>> trace;

    private AlgorithmTrace(@NotNull List<Pair<Integer, Double>> trace) {
        this.trace = trace;
    }

    @NotNull
    public static AlgorithmTrace createAlgorithmTraceByCsvData(@NotNull Path csvDataPath) throws CorruptedCsvException, IOException {
        return new AlgorithmTrace(new AbstractCsvProcessor<List<Pair<Integer, Double>>>() {
            @Override
            protected @NotNull List<Pair<Integer, Double>> processData(@NotNull List<List<String>> data) throws CorruptedCsvException {
                List<Pair<Integer, Double>> t = new ArrayList<>();
                for (List<String> row : data) {
                    try {
                        t.add(new Pair<>(Integer.parseInt(row.get(2)), Double.parseDouble(row.get(3))));
                    } catch (NumberFormatException e) {
                        throw new CorruptedCsvException("Unable to process csv", e);
                    }
                }
                return t;
            }
        }.loadAndGetProcessedData(csvDataPath.toAbsolutePath().toString()));
    }

    @NotNull
    public List<Pair<Integer, Double>> getTrace() {
        return trace;
    }
}
