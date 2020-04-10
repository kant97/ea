package optimal;

import optimal.configuration.OneExperimentConfiguration;
import optimal.configuration.probability.ProbabilitySamplingConfiguration;
import optimal.execution.OptimizationParametersSearchingListener;
import optimal.execution.ResultEntity;
import optimal.execution.ResultsConsumer;
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
    ProbabilitySamplingConfiguration myProbabilityEnumerationConfiguration;
    private static final Double EPS = 0.0000000001;
    private final ArrayList<OptimizationParametersSearchingListener> myListeners;
    private final OneExperimentConfiguration myConfiguration;

    public BestMutationRateSearcher(@NotNull OneExperimentConfiguration configuration) {
        myProblemType = configuration.problemType;
        myProblemSize = configuration.problemSize;
        myLambda = configuration.lambda;
        myBeginFitness = configuration.beginFitness;
        myEndFitness = configuration.endFitness;
        myProbabilityEnumerationConfiguration = configuration.getProbabilityEnumerationConfiguration();
        myConfiguration = configuration;
        myListeners = new ArrayList<>();
    }

    protected ProbabilityVectorGenerator getProbabilityVectorGenerator(double currentProbability, @NotNull Problem problem) {
        return new ProbabilityVectorGenerator(currentProbability, myProblemSize, myLambda,
                2.0 / (double) myProblemSize, myConfiguration.getNumberOfStepRepetitions(), problem,
                myConfiguration.algorithmType);
    }

    protected ProbabilitySearcher getProbabilitySearcher() {
        return ProbabilitySearcher.createProbabilitySearcher(myProbabilityEnumerationConfiguration);
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
                double tFP = INFINITY;
                if (Math.abs(p0Tilda - 1.) < EPS) {
                    update(T, pOpt, tFP, p, fitness);
                } else {
                    tFP = 1. / (1. - p0Tilda);
                    int fitnessOffset = fitness - myBeginFitness;
                    for (int i = 1; i < Math.min(v.size(), T.size() - fitnessOffset); i++) {
                        tFP += T.get(fitnessOffset + i) * v.get(i) / (1. - p0Tilda);
                    }
                    update(T, pOpt, tFP, p, fitness);
                }
                notifyListeners(new ResultEntity(myConfiguration, fitness, p, tFP),
                        ResultsConsumer.ResultType.INTERMEDIATE);
            }
            notifyListeners(new ResultEntity(myConfiguration, fitness, pOpt.get(fitness - myBeginFitness),
                    T.get(fitness - myBeginFitness)), ResultsConsumer.ResultType.OPTIMAL);
        }
        ArrayList<Double> pOptList = new ArrayList<>(pOpt.size());
        for (int i = 0; i < pOpt.size(); i++) {
            pOptList.add(-1.);
        }
        pOpt.forEach(pOptList::set);
        return pOptList;
    }

    public void notifyListeners(ResultEntity resultEntity, ResultsConsumer.ResultType type) {
        for (OptimizationParametersSearchingListener listener : myListeners) {
            listener.onNewResultEntity(resultEntity, type);
        }
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
