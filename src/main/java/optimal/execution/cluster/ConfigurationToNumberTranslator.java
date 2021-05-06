package optimal.execution.cluster;

import optimal.configuration.AbstractSingleExperimentConfiguration;
import optimal.probabilitySampling.ProbabilitySearcher;
import org.jetbrains.annotations.NotNull;

public class ConfigurationToNumberTranslator {
    private final double myEpsilon;
    private final AbstractSingleExperimentConfiguration myCurrentConfiguration;
    private final int myProbabilityDistance;

    public ConfigurationToNumberTranslator(AbstractSingleExperimentConfiguration currentConfiguration) {
        this.myCurrentConfiguration = currentConfiguration;
        int probabilityDistance = 0;
        final ProbabilitySearcher probabilitySearcher = getProbabilitySearcher(currentConfiguration);
        double prevP = 0.;
        double epsilon = Double.MAX_VALUE;
        for (double p = probabilitySearcher.getInitialProbability(); !probabilitySearcher.isFinished(); p =
                probabilitySearcher.getNextProb()) {
            epsilon = Math.min(epsilon, Math.abs(prevP - p) / 4.);
            prevP = p;
            probabilityDistance++;
        }
        this.myProbabilityDistance = probabilityDistance;
        this.myEpsilon = epsilon;
    }

    @NotNull
    private ProbabilitySearcher getProbabilitySearcher(AbstractSingleExperimentConfiguration currentConfiguration) {
        return ProbabilitySearcher.createProbabilitySearcher(currentConfiguration.getProbabilityEnumerationConfiguration());
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
        final ProbabilitySearcher probabilitySearcher = getProbabilitySearcher(myCurrentConfiguration);
        int left = 0;
        int right = myProbabilityDistance + 1;
        while (right - left > 1) {
            int m = (left + right) / 2;
            if (isALessOrInAreaOfB(probabilitySearcher.getProbabilityOnStepN(m), mutationRate)) {
                left = m;
            } else {
                right = m;
            }
        }
        return left;
    }
}
