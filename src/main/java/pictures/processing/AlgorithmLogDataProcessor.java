package pictures.processing;

import optimal.utils.DataProcessor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public final class AlgorithmLogDataProcessor extends DataProcessor<ArrayList<AlgorithmData>> {
    public AlgorithmLogDataProcessor(@NotNull String csvFileName) {
        super(csvFileName);
    }

    @Override
    public ArrayList<AlgorithmData> getProcessedData() {
        final ArrayList<AlgorithmData> algorithmData = new ArrayList<>();
        for (java.util.List<String> records : myRecords) {
            final int iterationNumber = Integer.parseInt(records.get(0));
            final int fitness = Integer.parseInt(records.get(1));
            final double v = Double.parseDouble(records.get(2));
            algorithmData.add(new AlgorithmData(iterationNumber, fitness, v));
        }
        return algorithmData;
    }
}
