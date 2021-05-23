package pictures.optimal2Pictures;

import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class RegretPlot {

    private final HeatMap heatMap;
    private final ArrayList<Double> minInHeatMapColumn;
    private final AlgorithmTrace algorithmTrace;

    public RegretPlot(@NotNull HeatMap heatMap, @NotNull AlgorithmTrace algorithmTrace) {
        this.heatMap = heatMap;
        minInHeatMapColumn = heatMap.getMinInEveryColumn();
        this.algorithmTrace = algorithmTrace;
    }

    public void printToCsvFile(Path csvFilePath) {
        double inf = Double.NEGATIVE_INFINITY;
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath.toFile()))) {
            writer.write("iterationNumber,piClass,regret\n");
            int iterationNumber = 0;
            for (final Pair<Integer, Double> piClassAndProb : algorithmTrace.getTrace()) {
                writer.write(String.valueOf(iterationNumber));
                writer.write(',');
                writer.write(String.valueOf(piClassAndProb.getKey()));
                writer.write(',');
                double expectedTime = heatMap.getExpectedTime(piClassAndProb.getKey(), piClassAndProb.getValue());
                Integer columnIndex = heatMap.getColumnIndex(piClassAndProb.getKey());
                double bestTime = columnIndex == null ? 0. : minInHeatMapColumn.get(columnIndex);
                double regret = expectedTime - bestTime;
                inf = Math.max(inf, regret);
                writer.write(String.valueOf(regret));
                writer.write('\n');
                writer.flush();
                iterationNumber++;
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to write to file " + csvFilePath, e);
        }
        System.out.println("Infinity value: " + inf * 1.5);
    }
}
