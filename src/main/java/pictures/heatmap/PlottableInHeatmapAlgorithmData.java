package pictures.heatmap;

import optimal.configuration.probability.ProbabilitySamplingConfiguration;
import optimal.probabilitySampling.ProbabilitySpace;
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
    private final ProbabilitySamplingConfiguration probabilitySamplingConfiguration;
    private final int minFitness;

    public PlottableInHeatmapAlgorithmData(Path resultsFilePath, Color color, int optimalValue,
                                           ProbabilitySamplingConfiguration probabilitySamplingConfiguration,
                                           int minFitness) {
        super(resultsFilePath, color);
        this.optimalValue = optimalValue;
        this.probabilitySamplingConfiguration = probabilitySamplingConfiguration;
        this.minFitness = minFitness;
    }

    @Override
    public @NotNull List<MatrixCellCoordinate> getOrderedMatrixCoordinates(@NotNull SimpleMatrix matrix) {
        final ArrayList<AlgorithmData> algorithmLogData = getAlgorithmLogData();
        final MatrixLine matrixLine = new MatrixLine(matrix, ProbabilitySpace.createProbabilitySpace(
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
