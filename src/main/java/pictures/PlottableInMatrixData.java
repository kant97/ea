package pictures;

import org.ejml.simple.SimpleMatrix;
import org.jetbrains.annotations.NotNull;
import pictures.processing.AlgorithmData;
import pictures.processing.AlgorithmLogDataProcessor;

import java.awt.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public abstract class PlottableInMatrixData {
    protected final Color color;
    private final Path resultsFilePath;

    public PlottableInMatrixData(Path resultsFilePath, Color color) {
        this.resultsFilePath = resultsFilePath;
        this.color = color;
    }

    protected ArrayList<AlgorithmData> getAlgorithmLogData() {
        final AlgorithmLogDataProcessor algorithmLogDataProcessor =
                new AlgorithmLogDataProcessor(resultsFilePath.toAbsolutePath().toString());
        algorithmLogDataProcessor.loadData();
        return algorithmLogDataProcessor.getProcessedData();
    }

    public abstract @NotNull List<MatrixCellCoordinate> getOrderedMatrixCoordinates(@NotNull SimpleMatrix matrix);

    public @NotNull Color getDataInPlotColor() {
        return color;
    }
}
