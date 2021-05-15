package optimal.utils;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PiExistenceTransitionsProcessor extends AbstractCsvProcessor<Map<Integer, Double>> {
    @Override
    protected @NotNull Map<Integer, Double> processData(@NotNull List<List<String>> data) throws CorruptedCsvException {
        final HashMap<Integer, Double> mp = new HashMap<>();
        for (List<String> record : data) {
            final int piClass;
            final double probability;
            try {
                piClass = Integer.parseInt(record.get(0));
                probability = Double.parseDouble(record.get(1));
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                throw new CorruptedCsvException("Csv is corrupted", e);
            }
            mp.put(piClass, probability);
        }
        return mp;
    }
}
