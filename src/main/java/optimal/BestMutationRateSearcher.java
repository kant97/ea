package optimal;

import optimal.configuration.OneExperimentConfiguration;
import optimal.execution.OptimizationParametersSearchingListener;
import optimal.execution.ResultEntity;
import optimal.probabilitySampling.ProbabilitySamplingStrategy;
import optimal.probabilitySampling.ProbabilitySearcher;
import org.jetbrains.annotations.NotNull;
import problem.Problem;
import problem.ProblemsManager;

import java.util.ArrayList;
import java.util.HashMap;

public class BestMutationRateSearcher {
    private final ProblemsManager.ProblemType myProblemType;
    private final double INFINITY = Double.MAX_VALUE;
    private final int myProblemSize;
    private final int myLambda;
    private final int myBeginFitness;
    private final int myEndFitness;
    private final double myMinMutationProbability;
    private final double myMaxMutationProbability;
    private final double myPrecisionForProbability;
    private static final Double EPS = 0.0000000001;
    private final ArrayList<OptimizationParametersSearchingListener> myListeners;
    private final OneExperimentConfiguration myConfiguration;

    public BestMutationRateSearcher(@NotNull OneExperimentConfiguration configuration) {
        myProblemType = configuration.problemType;
        myProblemSize = configuration.problemSize;
        myLambda = configuration.lambda;
        myBeginFitness = configuration.beginFitness;
        myEndFitness = configuration.endFitness;
        myMinMutationProbability = configuration.minMutationProbability;
        myMaxMutationProbability = configuration.maxMutationProbability;
        myPrecisionForProbability = configuration.precisionForProbability;
        myConfiguration = configuration;
        myListeners = new ArrayList<>();
    }

    protected ProbabilityVectorGenerator getProbabilityVectorGenerator(double currentProbability, @NotNull Problem problem) {
        return new ProbabilityVectorGenerator(currentProbability, myProblemSize, myLambda,
                2.0 / (double) myProblemSize, problem);
    }

    protected ProbabilitySearcher getProbabilitySearcher() {
        return ProbabilitySearcher.createProbabilitySearcher(myMinMutationProbability, myMaxMutationProbability,
                myPrecisionForProbability, ProbabilitySamplingStrategy.ITERATIVE);
    }

    public void addListener(OptimizationParametersSearchingListener listener) {
        myListeners.add(listener);
    }

    public void deleteAllListeners() {
        myListeners.clear();
    }

    public ArrayList<Double> getBestMutationProbabilities() {
        ArrayList<Double> T = new ArrayList<>(myEndFitness - myBeginFitness + 1); // optimization time for any fitness
        for (int f = myBeginFitness; f <= myEndFitness; f++) {
            T.add(INFINITY);
        }
        T.set(myEndFitness - myBeginFitness, 0.);
        HashMap<Integer, Double> pOpt = new HashMap<>(); // optimal mutation probability for any fitness
        for (int fitness = myEndFitness - 1; fitness >= myBeginFitness; fitness--) {
            Problem problem = ProblemsManager.createProblemInstanceWithFixedFitness(myProblemType, myProblemSize, fitness);
            ProbabilitySearcher ps = getProbabilitySearcher();
            int feedback = -1;
            for (double p = ps.getInitialProbability(); !ps.isFinished(); p = ps.getNextProb(feedback)) {
                ArrayList<Double> v = getProbabilityVectorGenerator(p, problem).getProbabilityVector();
                Double p0Tilda = v.get(0);
                if (Math.abs(p0Tilda - 1.) < EPS) {
                    update(T, pOpt, INFINITY, p, fitness);
                } else {
                    double tFP = 1. / (1. - p0Tilda);
                    int fitnessOffset = fitness - myBeginFitness;
                    for (int i = 1; i < Math.min(v.size(), T.size() - fitnessOffset); i++) {
                        tFP += T.get(fitnessOffset + i) * v.get(i) / (1. - p0Tilda);
                    }
                    update(T, pOpt, tFP, p, fitness);
                }
            }
            for (OptimizationParametersSearchingListener listener : myListeners) {
                listener.onNewResultEntity(new ResultEntity(myConfiguration, fitness, pOpt.get(fitness - myBeginFitness),
                        T.get(fitness - myBeginFitness)));
            }
        }
        ArrayList<Double> pOptList = new ArrayList<>(pOpt.size());
        for (int i = 0; i < pOpt.size(); i++) {
            pOptList.add(-1.);
        }
        pOpt.forEach(pOptList::set);
        return pOptList;
    }

    private void update(ArrayList<Double> T, HashMap<Integer, Double> pOpt,
                        Double optimizationTime, Double currentProbability, int currentFitness) {
        int fitnessOffset = currentFitness - myBeginFitness;
        if ((Math.abs(INFINITY - optimizationTime) < EPS) && !pOpt.containsKey(fitnessOffset)) {
            pOpt.put(fitnessOffset, currentProbability);
        }

        if (optimizationTime < T.get(fitnessOffset)) {
            T.set(fitnessOffset, optimizationTime);
            pOpt.put(fitnessOffset, currentProbability);
        }
    }
}
