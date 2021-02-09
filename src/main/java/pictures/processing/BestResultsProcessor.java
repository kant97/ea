package pictures.processing;

import optimal.utils.DataProcessor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BestResultsProcessor extends DataProcessor<ArrayList<Double>> {

    private final int timeColumn;
    private final int fitnessColumn;
    private final int lambdaColumn;
    private final int optimalValue;

    public int lambda = -1;

    public BestResultsProcessor(@NotNull String csvFileName, int timeColumn, int fitnessColumn, int lambdaColumn,
                                int optimalValue) {
        super(csvFileName);
        this.timeColumn = timeColumn;
        this.fitnessColumn = fitnessColumn;
        this.lambdaColumn = lambdaColumn;
        this.optimalValue = optimalValue;
    }

    @Override
    public ArrayList<Double> getProcessedData() {
        final HashMap<Integer, Double> doubles = getFitnessToRuntimeMap();
        ArrayList<Double> ans = new ArrayList<>();
        for (int i = 0; i < optimalValue; i++) {
            ans.add(0.);
        }
        for (Map.Entry<Integer, Double> entry : doubles.entrySet()) {
            ans.set(entry.getKey(), entry.getValue());
        }
        if (doubles.size() < optimalValue) {
            throw new IllegalStateException("All fitness values up to optimalValue (not including) should be in log, " +
                    "but we have " + doubles.size() + " out of " + optimalValue);
        }
        return ans;
    }

    @NotNull
    public HashMap<Integer, Double> getFitnessToRuntimeMap() {
        final HashMap<Integer, Double> doubles = new HashMap<>();
        for (List<String> record : myRecords) {
            int fitness = Integer.parseInt(record.get(fitnessColumn));
            double time = Double.parseDouble(record.get(timeColumn));
            int curLambda = Integer.parseInt(record.get(lambdaColumn));
            if (lambda == -1) {
                lambda = curLambda;
            } else if (lambda != curLambda) {
                throw new IllegalStateException("Different lambda found " + curLambda + " other lambda is " + lambda);
            }
            if (doubles.containsKey(fitness)) {
                throw new IllegalStateException("More than one best run time for one fitness, but should be one.");
            }
            if (fitness >= optimalValue) {
                throw new IllegalStateException("All fitness in log should be less than the optimal fitness. Found " + fitness + " Optimal " + optimalValue);
            }
            doubles.put(fitness, time);
        }
        return doubles;
    }
}
