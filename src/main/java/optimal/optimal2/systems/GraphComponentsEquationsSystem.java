package optimal.optimal2.systems;

import org.apache.commons.math3.linear.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class GraphComponentsEquationsSystem extends EquationsSystem {
    private final double[][] matrixData;
    private final double[] constantsData;
    private final boolean[][] graph;
    private final boolean[][] innerGraphTransposition;
    private final NodeType[] nodeTypes;
    private final Set<Integer> reachableInfinity;

    public GraphComponentsEquationsSystem(double[][] matrixData, double[] constantsData, boolean[][] graph, Set<Integer> reachableInfinity) {
        this.matrixData = matrixData;
        this.constantsData = constantsData;
        this.graph = graph;
        this.innerGraphTransposition = new boolean[graph.length - 1][];
        for (int i = 0; i < graph.length - 1; i++) {
            innerGraphTransposition[i] = new boolean[graph.length - 1];
        }
        for (int i = 0; i < graph.length - 1; i++) {
            for (int j = 0; j < graph.length - 1; j++) {
                innerGraphTransposition[i][j] = graph[j][i];
            }
        }
        this.reachableInfinity = reachableInfinity;
        nodeTypes = new NodeType[innerGraphTransposition.length];
    }

    private enum NodeType {
        UNKNOWN, INF_TIME_REACHABLE, FINITE_REACHABLE
    }

    private void backPropagateNodeType(@NotNull Collection<Integer> startingNodes, NodeType type) {
        final Queue<Integer> queue = new LinkedList<>(startingNodes);
        for (int u : startingNodes) {
            nodeTypes[u] = type;
        }
        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (int i = 0; i < innerGraphTransposition.length; i++) {
                if (i != u && innerGraphTransposition[u][i] && nodeTypes[i] == NodeType.UNKNOWN) {
                    nodeTypes[u] = type;
                    queue.add(u);
                }
            }
        }
    }

    @Override
    public List<Double> solveSystem() {
        Arrays.fill(nodeTypes, NodeType.UNKNOWN);
        backPropagateNodeType(reachableInfinity, NodeType.INF_TIME_REACHABLE);
        final List<Integer> lastLayerFiniteTimeReachable = new ArrayList<>();
        for (int i = 0; i < graph.length - 1; i++) {
            if (graph[i][graph.length - 1] && !reachableInfinity.contains(i)) {
                lastLayerFiniteTimeReachable.add(i);
            }
        }
        backPropagateNodeType(lastLayerFiniteTimeReachable, NodeType.FINITE_REACHABLE);
        final List<Integer> allFiniteTimeReachable = new ArrayList<>();
        for (int i = 0; i < nodeTypes.length; i++) {
            if (nodeTypes[i] == NodeType.FINITE_REACHABLE) {
                allFiniteTimeReachable.add(i);
            }
        }
        final ArrayList<Double> solution = new ArrayList<>();
        for (int i = 0; i < matrixData.length; i++) {
            solution.add(Double.POSITIVE_INFINITY);
        }
        if (!allFiniteTimeReachable.isEmpty()) {
            final double[][] extractedMatrixData = new double[allFiniteTimeReachable.size()][];
            final double[] extractedConstantsData = new double[allFiniteTimeReachable.size()];
            for (int i = 0; i < extractedMatrixData.length; i++) {
                extractedMatrixData[i] = new double[extractedMatrixData.length];
                Arrays.fill(extractedMatrixData[i], 0.);
            }
            for (int i = 0; i < allFiniteTimeReachable.size(); i++) {
                final int iNodeId = allFiniteTimeReachable.get(i);
                for (int j = 0; j < allFiniteTimeReachable.size(); j++) {
                    extractedMatrixData[i][j] = matrixData[iNodeId][allFiniteTimeReachable.get(j)];
                }
                extractedConstantsData[i] = constantsData[iNodeId];
            }
            final RealMatrix m = MatrixUtils.createRealMatrix(extractedMatrixData);
            final RealVector constants = new ArrayRealVector(extractedConstantsData);
            final DecompositionSolver solver = new LUDecomposition(m).getSolver();
            final double[] finiteTimeSolution = solver.solve(constants).toArray();
            for (int i = 0; i < allFiniteTimeReachable.size(); i++) {
                solution.set(allFiniteTimeReachable.get(i), finiteTimeSolution[i]);
            }
        }
        return solution;
    }
}
