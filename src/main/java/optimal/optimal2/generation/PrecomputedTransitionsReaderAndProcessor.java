package optimal.optimal2.generation;

import optimal.configuration.MainConfiguration;
import optimal.configuration.OneExperimentConfiguration;
import optimal.optimal2.AbstractPiExistenceClassesManager;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class PrecomputedTransitionsReaderAndProcessor extends PrecomputedTransitionsReader {
    public PrecomputedTransitionsReaderAndProcessor(@NotNull MainConfiguration mainConfiguration) {
        super(mainConfiguration);
    }

    private static double fastPow(double a, int power) {
        double ans = 1.;
        while (power > 0) {
            if (power % 2 > 0) {
                ans = ans * a;
            }
            a = a * a;
            power = power / 2;
        }
        return ans;
    }

    private Map<Integer, Double> method1(@NotNull Map<Integer, Double> transitions) {
        return null;
    }

    protected Map<Integer, Double> method2(@NotNull Map<Integer, Double> transitions) {
        final AbstractPiExistenceClassesManager piManager = AbstractPiExistenceClassesManager.create(mainConfiguration.getProblemConfig());
        final int lambda = mainConfiguration.getAlgorithmConfig().getLambda();
        final List<Integer> piClassesSortedByFitness = transitions.keySet().stream().sorted(Comparator.comparingInt(piManager::getFitnessById)).collect(Collectors.toList());
        double prefSum = 0.;
        final int firstClassByFitness = piClassesSortedByFitness.get(0);
        int prevClassFitness = firstClassByFitness == Integer.MIN_VALUE ? Integer.MIN_VALUE : piManager.getFitnessById(firstClassByFitness);
        final Map<Integer, Double> transitionsLambda = new HashMap<>();
        for (int i = 0; i < piClassesSortedByFitness.size(); ) {
            final ArrayList<Integer> curFitnessClasses = new ArrayList<>();
            double curProbsSum = 0.;
            for (; i < piClassesSortedByFitness.size(); i++) {
                int piClass = piClassesSortedByFitness.get(i);
                int fitness = piManager.getFitnessById(piClass);
                if (fitness != prevClassFitness) {
                    prevClassFitness = fitness;
                    break;
                }
                curFitnessClasses.add(piClass);
                curProbsSum += transitions.get(piClass);
            }
            for (int curClassId : curFitnessClasses) {
                double curClassProbLambda = 0.;
                if (curClassId == Integer.MIN_VALUE) {
                    curClassProbLambda = fastPow(curProbsSum, lambda);
                } else {
                    double curClassProb = transitions.get(curClassId);
                    for (int j = 0; j < lambda; j++) {
                        curClassProbLambda += fastPow(prefSum + curProbsSum, j) * curClassProb * fastPow(prefSum, lambda - j - 1);
                    }
                }
                transitionsLambda.put(curClassId, curClassProbLambda);
            }
            prefSum += curProbsSum;
        }
        final Double probLambdaSum = transitionsLambda.values().stream().reduce(0., Double::sum);
        if (probLambdaSum > 1.) {
            transitionsLambda.forEach((id, prob) -> prob /= probLambdaSum);
        }
        return transitionsLambda;
    }

    @Override
    public @NotNull Map<Integer, Double> getTransitionsProbabilities(double r, int piExistenceClassId) {
        final Map<Integer, Double> transitionsProbabilities = super.getTransitionsProbabilitiesIncludingWorse(r, piExistenceClassId);
        final Map<Integer, Double> integerDoubleMap = method2(transitionsProbabilities);
        final Double probWorse = integerDoubleMap.remove(Integer.MIN_VALUE);
        if (probWorse == null) {
            return integerDoubleMap;
        }
        final Double curProb = integerDoubleMap.getOrDefault(piExistenceClassId, 0.);
        integerDoubleMap.put(piExistenceClassId, probWorse + curProb);
        return integerDoubleMap;
    }

    @Override
    public @NotNull Map<Integer, Double> getTransitionsProbabilitiesIncludingWorse(double r, int piExistenceClassId) {
        final Map<Integer, Double> transitionsProbabilities = super.getTransitionsProbabilities(r, piExistenceClassId);
        return method2(transitionsProbabilities);
    }

    @Override
    protected boolean isCompatible(@NotNull OneExperimentConfiguration storedConfiguration) {
        return storedConfiguration.probabilityEnumeration.equals(mainConfiguration.getTransitionsGeneration().getProbabilityEnumeration()) &&
                storedConfiguration.problemConfig.equals(mainConfiguration.getProblemConfig()) &&
                storedConfiguration.algorithmConfig.getAlgorithmType().equals(mainConfiguration.getAlgorithmConfig().getAlgorithmType()) &&
                storedConfiguration.algorithmConfig.getLambda() == 1 &&
                storedConfiguration.stopConditionConfig.equals(mainConfiguration.getTransitionsGeneration().getStopConditionConfig()) &&
                storedConfiguration.endFitness == mainConfiguration.getEndFitness();
    }
}
