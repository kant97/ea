package optimal.execution.cluster.generation;

import optimal.execution.cluster.Utils;

public class VectorsDirectoryNameGenerator {
    private int resultsDirectoryId = 0;

    public String generateNewResultsDirectoryName() {
        final String s = Utils.RESULTS_DIRECTORY_NAME + resultsDirectoryId;
        resultsDirectoryId++;
        return s;
    }
}
