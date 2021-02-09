package pictures;

import pictures.processing.AlgorithmData;
import pictures.processing.AlgorithmLogDataProcessor;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

public class IterationToDiffVectorProcessor {
    private final Path pathToAlgorithmsLog;
    private ArrayList<AlgorithmData> processedData;
    private final MatrixWrapper matrix;

    public IterationToDiffVectorProcessor(Path pathToAlgorithmsLog, MatrixWrapper matrixWrapper) {
        this.pathToAlgorithmsLog = pathToAlgorithmsLog;
        this.matrix = matrixWrapper;
    }

    public void loadVector() {
        final AlgorithmLogDataProcessor algorithmLogDataProcessor =
                new AlgorithmLogDataProcessor(pathToAlgorithmsLog.toAbsolutePath().toString());
        algorithmLogDataProcessor.loadData();
        processedData = algorithmLogDataProcessor.getProcessedData();
    }

    public int getDiffHeatmapWidth() {
        final Optional<AlgorithmData> max =
                processedData.stream().max(Comparator.comparingInt(AlgorithmData::getIterationNumber));
        if (!max.isPresent()) {
            throw new IllegalStateException("Data is empty");
        }
        return Math.min(1000, max.get().getIterationNumber());
    }

}
