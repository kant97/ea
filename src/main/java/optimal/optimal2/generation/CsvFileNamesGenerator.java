package optimal.optimal2.generation;

import org.jetbrains.annotations.NotNull;

public abstract class CsvFileNamesGenerator {
    public abstract @NotNull String getCsvFileName(int piExistenceClass, double probability);
}
