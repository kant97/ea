package optimal.execution.cluster;

import optimal.configuration.AbstractSingleExperimentConfiguration;
import optimal.probabilitySampling.ProbabilitySpace;
import org.jetbrains.annotations.NotNull;

public class ConfigurationToNumberTranslator {
    private final double myEpsilon;
    private final AbstractSingleExperimentConfiguration myCurrentConfiguration;
    private final int myProbabilityDistance;

    public ConfigurationToNumberTranslator(AbstractSingleExperimentConfiguration currentConfiguration) {
        this.myCurrentConfiguration = currentConfiguration;
        int probabilityDistance = 0;
        final ProbabilitySpace probabilitySpace = getProbabilitySearcher(currentConfiguration);
        double prevP = 0.;
        double epsilon = Double.MAX_VALUE;
        for (double p = probabilitySpace.getInitialProbability(); !probabilitySpace.isFinished(); p =
                probabilitySpace.getNextProb()) {
            epsilon = Math.min(epsilon, Math.abs(prevP - p) / 4.);
            prevP = p;
            probabilityDistance++;
        }
        this.myProbabilityDistance = probabilityDistance;
        this.myEpsilon = epsilon;
    }

    @NotNull
    private ProbabilitySpace getProbabilitySearcher(AbstractSingleExperimentConfiguration currentConfiguration) {
        return ProbabilitySpace.createProbabilitySpace(currentConfiguration.getProbabilityEnumerationConfiguration());
    }

    public int translateFitnessAndMutationRateToNumber(int fitness, double mutationRate) {
        if (fitness > myCurrentConfiguration.endFitness) {
            throw new IllegalArgumentException("Fitness value is too big");
        }
        if (fitness < myCurrentConfiguration.beginFitness) {
            throw new IllegalArgumentException("Fitness value is too small");
        }
        int fitnessDist = (myCurrentConfiguration.endFitness - fitness) * myProbabilityDistance;
        int mutationRateDist = getMutationRateDistance(mutationRate);
        return fitnessDist + mutationRateDist;
    }

    private boolean isAInAreaOfB(double a, double b) {
        return Math.abs(a - b) < myEpsilon;
    }

    private boolean isALessOrInAreaOfB(double a, double b) {
        if (isAInAreaOfB(a, b)) {
            return true;
        }
        return a < b;
    }

    public int getMutationRateDistance(double mutationRate) {
        final ProbabilitySpace probabilitySpace = getProbabilitySearcher(myCurrentConfiguration);
        int left = 0;
        int right = myProbabilityDistance + 1;
        while (right - left > 1) {
            int m = (left + right) / 2;
            if (isALessOrInAreaOfB(probabilitySpace.getProbabilityOnStepN(m), mutationRate)) {
                left = m;
            } else {
                right = m;
            }
        }
        return left;
    }
}
