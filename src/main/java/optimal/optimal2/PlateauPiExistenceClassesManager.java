package optimal.optimal2;

import org.jetbrains.annotations.NotNull;
import problem.Plateau;
import problem.Problem;
import utils.BestCalculatedPatch;

import java.util.ArrayList;
import java.util.List;

public class PlateauPiExistenceClassesManager extends AbstractPiExistenceClassesManager {
    private final int n;
    private final int k;

    public PlateauPiExistenceClassesManager(int n, int k) {
        this.n = n;
        this.k = k;
    }

    @Override
    public int getIdByIndividual(@NotNull Problem individual, @NotNull BestCalculatedPatch patch) {
        return ((Plateau) individual).getPatchOnesCount(patch.patch);
    }

    @Override
    public @NotNull Problem getAnyIndividualById(int piExistenceClassId) {
        final Plateau plateau = new Plateau(n, k);
        boolean[] individual = plateau.getIndividual();
        List<Integer> patch = new ArrayList<>();
        for (int i = 0; i < individual.length; i++) {
            if (individual[i]) {
                patch.add(i);
            }
        }
        plateau.applyPatch(patch, 1);
        patch.clear();
        for (int i = 0; i < piExistenceClassId; i++) {
            patch.add(i);
        }
        plateau.applyPatch(patch, getFitnessById(piExistenceClassId));
        return plateau;
    }

    @Override
    public int getFitnessById(int id) {
        if (id == Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return id / k + 1;
    }

    @Override
    public @NotNull List<Integer> getPiExistenceClassesWithFitness(int fitness) {
        final List<Integer> ids = new ArrayList<>();
        for (int i = (fitness - 1) * k; i < (fitness - 1) * k + k; i++) {
            ids.add(i);
        }
        return ids;
    }
}
