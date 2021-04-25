package optimal.optimal2;

import java.util.List;

interface ResultListener {
    void onResultsReady(int fitness, double r, List<Integer> curPiExistenceClasses, List<Double> t);
}
