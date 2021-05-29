package optimal.optimal2;

import optimal.configuration.MainConfiguration;
import optimal.configuration.OneExperimentConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AllResultsListener implements ResultListener {
    private final ResultsLogger myResultsLogger;
    private final MainConfiguration myOneExperimentConfiguration;

    public AllResultsListener(@NotNull String allResultsFileName, @NotNull MainConfiguration configuration) {
        myResultsLogger = new ResultsLogger(allResultsFileName);
        myOneExperimentConfiguration = configuration;
    }

    @Override
    public void onResultsReady(int fitness, double r, List<Integer> curPiExistenceClasses, List<Double> t) {
        final ResultsContainer resultsContainer = new ResultsContainer(myOneExperimentConfiguration);
        resultsContainer.addExtraData("fitness", String.valueOf(fitness));
        resultsContainer.addExtraData("probability", String.valueOf(r));
        for (int i = 0; i < curPiExistenceClasses.size(); i++) {
            resultsContainer.addExtraData("piExistenceClassId", String.valueOf(curPiExistenceClasses.get(i)));
            resultsContainer.addExtraData("optimizationTime", String.valueOf(t.get(i)));
            myResultsLogger.logResults(resultsContainer);
        }
    }

    public void close() {
        try {
            myResultsLogger.close();
        } catch (Exception e) {
            throw new IllegalStateException("Exception occurred while closing logger", e);
        }
    }
}