package pictures;

import optimal.utils.DataProcessor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChartDataProcessor extends DataProcessor<Map<Integer, Double>> {
    private final int myOptimalValue;

    public ChartDataProcessor(@NotNull String csvFileName, int myOptimalValue) {
        super(csvFileName);
        this.myOptimalValue = myOptimalValue;
    }

    @Override
    public Map<Integer, Double> getProcessedData() {
        Map<Integer, Double> ans = new HashMap<>();
        for (List<String> record : myRecords) {
            final int fitness = myOptimalValue - Integer.parseInt(record.get(1));
            final double mutationRate = Double.parseDouble(record.get(2));
            if (fitness > 0) {
                ans.put(fitness, mutationRate);
            }
        }
        return ans;
    }
}
