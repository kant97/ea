package pictures.optimal2Pictures;

import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.List;

public class AlgorithmTrace {
    private final List<Pair<Integer, Double>> trace;

    private AlgorithmTrace(@NotNull List<Pair<Integer, Double>> trace) {
        this.trace = trace;
    }

    @NotNull
    public static AlgorithmTrace createAlgorithmTraceByCsvData(@NotNull Path csvDataPath) {
        return null;
    }
}
