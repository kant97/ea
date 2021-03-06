package optimal;

import optimal.configuration.AbstractSingleExperimentConfiguration;
import optimal.configuration.OneExperimentConfiguration;
import optimal.configuration.OptimalMutationRateSearchingSingleExperimentConfiguration;
import optimal.configuration.probability.ProbabilitySamplingConfiguration;
import optimal.configuration.runs.StopConditionConfiguration;
import optimal.configuration.vectorGeneration.VectorGenerationConfiguration;
import optimal.execution.ResultEntity;
import optimal.execution.events.EventType;
import optimal.execution.events.EventsManager;
import optimal.execution.events.ResultEntityObtainedEvent;
import optimal.heuristics.ExperimentState;
import optimal.heuristics.Heuristics;
import optimal.probability.ProbabilityVectorGenerator;
import optimal.probability.ProbabilityVectorGeneratorManager;
import optimal.probabilitySampling.ProbabilitySearcher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import problem.Problem;
import problem.ProblemsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class BestMutationRateSearcher {
    private final ProblemsManager.ProblemType myProblemType;
    private final double INFINITY = Double.MAX_VALUE;
    private final int myProblemSize;
    private final int myLambda;
    private final int myBeginFitness;
    private final int myEndFitness;
    private final ProbabilitySamplingConfiguration myProbabilityEnumerationConfiguration;
    private static final Double EPS = 0.0000000001;
    private final StopConditionConfiguration myStopConditionConfiguration;
    private final AbstractSingleExperimentConfiguration myConfiguration;
    private final EventsManager myEventsManager;
    protected final @Nullable Heuristics myHeuristics;
    private final VectorGenerationConfiguration.VectorGenerationStrategy myVectorGenerationStrategy;

    private BestMutationRateSearcher(@NotNull AbstractSingleExperimentConfiguration configuration,
                                     StopConditionConfiguration stopConditionConfiguration,
                                     VectorGenerationConfiguration.VectorGenerationStrategy vectorGenerationStrategy) {
        myProblemType = configuration.problemConfig.getProblemType();
        myProblemSize = configuration.problemConfig.getSize();
        myLambda = configuration.algorithmConfig.getLambda();
        myBeginFitness = configuration.beginFitness;
        myEndFitness = configuration.endFitness;
        myProbabilityEnumerationConfiguration = configuration.getProbabilityEnumerationConfiguration();
        myConfiguration = configuration;
        myEventsManager = new EventsManager();
        myHeuristics = Heuristics.createHeuristics(configuration.problemConfig.getProblemType());
        addListener(event -> {
            if (myHeuristics != null) {
                myHeuristics.acceptResult(((ResultEntityObtainedEvent) event).getResultEntity());
            }
        }, EventType.INTERMEDIATE_RESULT_READY);
        this.myStopConditionConfiguration = stopConditionConfiguration;
        this.myVectorGenerationStrategy = vectorGenerationStrategy;
    }

    public BestMutationRateSearcher(@NotNull OneExperimentConfiguration oneExperimentConfiguration) {
        this(oneExperimentConfiguration, oneExperimentConfiguration.stopConditionConfig,
                VectorGenerationConfiguration.VectorGenerationStrategy.RUN_TIME_VECTOR_GENERATION);
    }

    public BestMutationRateSearcher(@NotNull OptimalMutationRateSearchingSingleExperimentConfiguration configuration) {
        this(configuration, configuration.getStopConditionConfiguration(),
                configuration.getVectorGenerationConfig().getStrategy());
    }

    protected ProbabilityVectorGenerator getProbabilityVectorGenerator(double currentProbability,
                                                                       @NotNull Problem problem, int fitness) {
        if (myVectorGenerationStrategy == VectorGenerationConfiguration.VectorGenerationStrategy.RUN_TIME_VECTOR_GENERATION) {
            return ProbabilityVectorGeneratorManager.getProbabilityVectorGeneratorInRuntime(currentProbability,
                    problem, myConfiguration.algorithmConfig, myStopConditionConfiguration);
        } else if (myVectorGenerationStrategy == VectorGenerationConfiguration.VectorGenerationStrategy.PRECOMPUTED_VECTOR_READING) {
            return ProbabilityVectorGeneratorManager.getProbabilityVectorReaderFromPrecomputedFiles((
                            (OptimalMutationRateSearchingSingleExperimentConfiguration) myConfiguration),
                    currentProbability,
                    fitness);
        }
        throw new IllegalStateException("Probability vector generation strategy " + myVectorGenerationStrategy + " is" +
                " not supported");
    }

    protected ProbabilitySearcher getProbabilitySearcher() {
        return ProbabilitySearcher.createProbabilitySearcher(myProbabilityEnumerationConfiguration);
    }

    public void addListener(Consumer<EventsManager.Event> listener, @NotNull EventType eventType) {
        myEventsManager.subscribe(eventType, listener);
    }

    public void deleteListener(Consumer<EventsManager.Event> listener, @NotNull EventType eventType) {
        myEventsManager.unsubscribe(eventType, listener);
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
            for (double p = ps.getInitialProbability(); !ps.isFinished(); p = ps.getNextProb()) {
                if (myHeuristics != null) {
                    myHeuristics.acceptNewExperimentState(new ExperimentState(myProblemType, fitness, p));
                    if (myHeuristics.isSupposedToBeInfOnThisExperiment()) {
                        update(T, pOpt, INFINITY, p, fitness);
                        notifyResultsListeners(new ResultEntity(myConfiguration, fitness, p, INFINITY),
                                EventType.INTERMEDIATE_RESULT_READY);
                        continue;
                    }
                }
                ArrayList<Double> v = getProbabilityVectorGenerator(p, problem, fitness).getProbabilityVector();
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
                notifyResultsListeners(new ResultEntity(myConfiguration, fitness, p, tFP),
                        EventType.INTERMEDIATE_RESULT_READY);
            }
            notifyResultsListeners(new ResultEntity(myConfiguration, fitness, pOpt.get(fitness - myBeginFitness),
                    T.get(fitness - myBeginFitness)), EventType.OPTIMAL_RESULT_READY);
            notifyProgressListeners();
        }
        ArrayList<Double> pOptList = new ArrayList<>(pOpt.size());
        for (int i = 0; i < pOpt.size(); i++) {
            pOptList.add(-1.);
        }
        pOpt.forEach(pOptList::set);
        return pOptList;
    }

    private void notifyProgressListeners() {
        myEventsManager.notify(EventType.PROGRESS_UPDATE, null);
    }

    public void notifyResultsListeners(ResultEntity resultEntity, EventType type) {
        myEventsManager.notify(type, new ResultEntityObtainedEvent(type, resultEntity));
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
