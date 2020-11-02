package pictures;

import org.jetbrains.annotations.NotNull;
import optimal.utils.DataProcessor;

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
        int prevFitness = Integer.parseInt(myRecords.get(0).get(0));
        double prevMutationRate = Double.parseDouble(myRecords.get(0).get(1));
        Map<Integer, Double> ans = new HashMap<>();
        for (List<String> record : myRecords) {
            final int fitness = Integer.parseInt(record.get(0));
            if (prevFitness != fitness) {
                ans.put(myOptimalValue - prevFitness, prevMutationRate);
            }
            prevFitness = fitness;
            prevMutationRate = Double.parseDouble(record.get(1));
        }
        return ans;
    }
}
