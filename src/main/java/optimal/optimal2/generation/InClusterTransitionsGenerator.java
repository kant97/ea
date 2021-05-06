package optimal.optimal2.generation;

import optimal.configuration.PiExistenceTransitionClusterConfiguration;
import optimal.execution.cluster.generation.vectors.RecoveryFitnessLevelVectorGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static optimal.configuration.vectorGeneration.VectorGenerationConfiguration.VectorGenerationStrategy.RUN_TIME_VECTOR_GENERATION;

public class InClusterTransitionsGenerator {
    public void generateTransitions(@NotNull PiExistenceTransitionClusterConfiguration configuration, @NotNull PiExistenceTransitionsWriter transitionsWriter) {
        final AbstractTransitionsGenerator transitionsGenerator = AbstractTransitionsGenerator.create(configuration.getStopConditionConfig(), configuration.getProblemConfig(), configuration.getAlgorithmConfig(), RUN_TIME_VECTOR_GENERATION);
        final Map<Integer, Double> transitionsProbabilities = transitionsGenerator.getTransitionsProbabilities(configuration.getProbability(), configuration.getPiExistenceClassId());
        transitionsWriter.writeResults(transitionsProbabilities);
    }

    public void recoveryTransitions(@NotNull PiExistenceTransitionClusterConfiguration configuration, @NotNull ClusterPlacesManager placesManager) {
        RecoveryFitnessLevelVectorGenerator.RecoveryProbabilityVectorProcessorImpl recoveryProbabilityVectorProcessor = new RecoveryFitnessLevelVectorGenerator.RecoveryProbabilityVectorProcessorImpl(placesManager.getStringPathToOutputFile());
        recoveryProbabilityVectorProcessor.loadData();
        recoveryProbabilityVectorProcessor.getProcessedData();
        if (recoveryProbabilityVectorProcessor.needRecovering) {
            generateTransitions(configuration, new PiExistenceTransitionsWriter(placesManager));
        }
    }
}
