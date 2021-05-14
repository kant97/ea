package optimal.optimal2.generation;

import optimal.configuration.PiExistenceTransitionClusterConfiguration;
import optimal.utils.CorruptedCsvException;
import optimal.utils.PiExistenceTransitionsProcessor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;

public class InClusterTransitionsGenerator {
    public void generateTransitions(@NotNull PiExistenceTransitionClusterConfiguration configuration, @NotNull PiExistenceTransitionsWriter transitionsWriter) {
        final AbstractTransitionsGenerator transitionsGenerator = AbstractTransitionsGenerator.createRunTimeTransitionsGenerator(configuration.getStopConditionConfig(), configuration.getProblemConfig(), configuration.getAlgorithmConfig());
        final Map<Integer, Double> transitionsProbabilities = transitionsGenerator.getTransitionsProbabilities(configuration.getProbability(), configuration.getPiExistenceClassId());
        transitionsWriter.writeResults(transitionsProbabilities);
    }

    public void recoveryTransitions(@NotNull PiExistenceTransitionClusterConfiguration configuration, @NotNull ClusterPlacesManager placesManager) {
        final PiExistenceTransitionsProcessor piExistenceTransitionsProcessor = new PiExistenceTransitionsProcessor();
        boolean needRecovery = false;
        try {
            piExistenceTransitionsProcessor.loadAndGetProcessedData(placesManager.getStringPathToOutputFile());
        } catch (IOException | CorruptedCsvException e) {
            needRecovery = true;
        }
        if (needRecovery) {
            generateTransitions(configuration, new PiExistenceTransitionsWriter(placesManager));
        }
    }
}
