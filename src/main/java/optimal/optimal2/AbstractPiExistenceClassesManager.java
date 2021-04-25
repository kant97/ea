package optimal.optimal2;

import optimal.configuration.problems.PlateauConfig;
import optimal.configuration.problems.ProblemConfig;
import org.jetbrains.annotations.NotNull;
import problem.Problem;
import problem.ProblemsManager;
import utils.BestCalculatedPatch;

import java.util.List;

public abstract class AbstractPiExistenceClassesManager {
    @NotNull
    public static AbstractPiExistenceClassesManager create(@NotNull ProblemConfig config) {
        if (config.getProblemType() == ProblemsManager.ProblemType.ONE_MAX_PLATEAU) {
            PlateauConfig plateauConfig = (PlateauConfig) config;
            return new PlateauPiExistenceClassesManager(plateauConfig.getSize(), plateauConfig.getK());
        }
        throw new IllegalStateException(config.getProblemType().name() + " is not supported");
    }

    public abstract int getIdByIndividual(@NotNull Problem individual, @NotNull BestCalculatedPatch patch);

    public abstract @NotNull Problem getAnyIndividualById(int piExistenceClassId);

    public abstract int getFitnessById(int id);

    public abstract @NotNull List<Integer> getPiExistenceClassesWithFitness(int fitness);
}
