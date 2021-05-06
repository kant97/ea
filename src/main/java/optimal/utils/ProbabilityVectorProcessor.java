package optimal.utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ProbabilityVectorProcessor extends DataProcessor<ArrayList<Double>> {

    public ProbabilityVectorProcessor(@NotNull String csvFileName) {
        super(csvFileName);
    }

    protected abstract void onParseNumberError(Exception e, @NotNull List<String> record);

    protected abstract boolean shouldContinue();

    protected abstract void onEmptyRecords();

    @Override
    @NotNull
    public ArrayList<Double> getProcessedData() {
        if (myRecords.isEmpty()) {
            onEmptyRecords();
        }
        if (!shouldContinue()) {
            return new ArrayList<>();
        }
        HashMap<Integer, Double> mp = new HashMap<>();
        for (List<String> record : myRecords) {
            final int fitnessIncrease;
            final double probability;
            try {
                fitnessIncrease = Integer.parseInt(record.get(0));
                probability = Double.parseDouble(record.get(1));
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                onParseNumberError(e, record);
                if (shouldContinue()) {
                    continue;
                } else {
                    return new ArrayList<>();
                }
            }
            mp.put(fitnessIncrease, probability);
        }
        final List<Integer> fitness = mp.keySet().stream().sorted().collect(Collectors.toList());
        final ArrayList<Double> ans = new ArrayList<>();
        for (int f : fitness) {
            ans.add(mp.get(f));
        }
        return ans;
    }
}
