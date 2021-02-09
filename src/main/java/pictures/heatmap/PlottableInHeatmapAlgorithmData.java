package pictures.heatmap;

import optimal.configuration.probability.IterativeProbabilityConfiguration;
import optimal.probabilitySampling.ProbabilitySearcher;
import org.ejml.simple.SimpleMatrix;
import org.jetbrains.annotations.NotNull;
import pictures.MatrixCellCoordinate;
import pictures.PlottableInMatrixData;
import pictures.processing.AlgorithmData;

import java.awt.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PlottableInHeatmapAlgorithmData extends PlottableInMatrixData {

    private final int optimalValue;
    private final IterativeProbabilityConfiguration probabilitySamplingConfiguration;
    private final int minFitness;

    public PlottableInHeatmapAlgorithmData(Path resultsFilePath, Color color, int optimalValue,
                                           IterativeProbabilityConfiguration probabilitySamplingConfiguration,
                                           int minFitness) {
        super(resultsFilePath, color);
        this.optimalValue = optimalValue;
        this.probabilitySamplingConfiguration = probabilitySamplingConfiguration;
        this.minFitness = minFitness;
    }

    @Override
    public @NotNull List<MatrixCellCoordinate> getOrderedMatrixCoordinates(@NotNull SimpleMatrix matrix) {
        final ArrayList<AlgorithmData> algorithmLogData = getAlgorithmLogData();
        final MatrixLine matrixLine = new MatrixLine(matrix, ProbabilitySearcher.createProbabilitySearcher(
                probabilitySamplingConfiguration));
        ArrayList<MatrixCellCoordinate> coordinates = new ArrayList<>();
        for (AlgorithmData algorithmData : algorithmLogData) {
            final int fitnessDistance = optimalValue - algorithmData.getFitness();
            if (fitnessDistance > optimalValue - minFitness || fitnessDistance == 0) {
                continue;
            }
            final int matrixColumnIndOfFitnessDistance =
                    matrixLine.getMatrixColumnIndOfFitnessDistance(fitnessDistance);
            final int matrixRowIndOfMutationRate =
                    matrixLine.getMatrixRowIndOfMutationRate(algorithmData.getMutationRate());
            coordinates.add(new MatrixCellCoordinate(matrixRowIndOfMutationRate,
                    matrixColumnIndOfFitnessDistance));
        }
        return coordinates;
    }

}
