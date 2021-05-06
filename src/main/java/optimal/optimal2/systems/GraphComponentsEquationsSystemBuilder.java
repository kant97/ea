package optimal.optimal2.systems;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class GraphComponentsEquationsSystemBuilder {
    private final @NotNull List<Integer> piExistenceClasses;
    private final @NotNull Map<Integer, Double> dp;
    private final double[][] matrixData;
    private final double[] constantsData;
    private final boolean[][] graph;
    private final ArrayList<Integer> reachableInfinity;

    public GraphComponentsEquationsSystemBuilder(@NotNull List<Integer> piExistenceClasses, @NotNull Map<Integer, Double> dp) {
        this.piExistenceClasses = piExistenceClasses;
        this.dp = dp;
        matrixData = new double[piExistenceClasses.size()][];
        graph = new boolean[piExistenceClasses.size() + 1][];
        for (int i = 0; i < matrixData.length; i++) {
            matrixData[i] = new double[piExistenceClasses.size()];
        }
        constantsData = new double[piExistenceClasses.size()];
        for (int i = 0; i < graph.length; i++) {
            graph[i] = new boolean[piExistenceClasses.size() + 1];
            Arrays.fill(graph[i], false);
        }
        reachableInfinity = new ArrayList<>();
    }

    public void addLine(int piExistenceClassIndex, @NotNull Map<Integer, Double> transitionProbabilities) {
        for (int i = 0; i < piExistenceClasses.size(); i++) {
            int toId = piExistenceClasses.get(i);
            if (transitionProbabilities.containsKey(toId) && transitionProbabilities.get(toId) != 0.) {
                graph[piExistenceClassIndex][i] = true;
            }
            matrixData[piExistenceClassIndex][i] = transitionProbabilities.getOrDefault(toId, 0.);
            transitionProbabilities.remove(toId);
        }
        matrixData[piExistenceClassIndex][piExistenceClassIndex] -= 1.;
        constantsData[piExistenceClassIndex] = -1.;
        for (Map.Entry<Integer, Double> entry: transitionProbabilities.entrySet()) {
            if (dp.get(entry.getKey()).equals(Double.POSITIVE_INFINITY)) {
                reachableInfinity.add(piExistenceClassIndex);
                break;
            }
            graph[piExistenceClassIndex][piExistenceClasses.size()] = true;
            constantsData[piExistenceClassIndex] -= entry.getValue() * dp.get(entry.getKey());
        }
    }

    public EquationsSystem buildSystem() {
        return new GraphComponentsEquationsSystem(matrixData, constantsData, graph, new HashSet<>(reachableInfinity));
    }
}
