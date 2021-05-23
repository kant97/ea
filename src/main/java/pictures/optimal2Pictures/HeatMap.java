package pictures.optimal2Pictures;

import javafx.util.Pair;
import optimal.probabilitySampling.IntegerToProbabilityBijectiveMapping;
import optimal.utils.AbstractCsvProcessor;
import optimal.utils.CorruptedCsvException;
import org.ejml.simple.SimpleMatrix;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pictures.coloring.AbstractColouring;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class HeatMap {
    private final SimpleMatrix data;
    private final IntegerToProbabilityBijectiveMapping integerProbabilityBijection;
    private final Map<Integer, Integer> piExistenceClassToNumber;
    private final ArrayList<Integer> numberToPiExistenceClass;

    private HeatMap(@NotNull SimpleMatrix data, @NotNull IntegerToProbabilityBijectiveMapping integerProbabilityBijection, @NotNull Map<Integer, Integer> piExistenceClassToNumber, @NotNull ArrayList<Integer> numberToPiExistenceClass) {
        this.data = data;
        this.integerProbabilityBijection = integerProbabilityBijection;
        this.piExistenceClassToNumber = piExistenceClassToNumber;
        this.numberToPiExistenceClass = numberToPiExistenceClass;
    }

    @NotNull
    public static HeatMap createHeatMapByCsvData(@NotNull Path csvFilePath, @NotNull IntegerToProbabilityBijectiveMapping integerProbabilityBijection) throws CorruptedCsvException, IOException {
        final ArrayList<Integer> numberToPiClass = new ArrayList<>();
        Map<Integer, Integer> piClassToNumber = new HashMap<>();
        final SimpleMatrix data = new AbstractCsvProcessor<SimpleMatrix>() {
            @Override
            protected @NotNull SimpleMatrix processData(@NotNull List<List<String>> data) throws CorruptedCsvException {
                int maxMutationRateNumber = Integer.MIN_VALUE;
                int minMutationRateNumber = Integer.MAX_VALUE;
                final TreeSet<Integer> piClasses = new TreeSet<>();
                for (List<String> row : data) {
                    try {
                        int classId = Integer.parseInt(row.get(0));
                        double mutationRate = Double.parseDouble(row.get(1));
                        piClasses.add(classId);
                        int mutationRateNumber = integerProbabilityBijection.probabilityToInteger(mutationRate);
                        maxMutationRateNumber = Math.max(mutationRateNumber, maxMutationRateNumber);
                        minMutationRateNumber = Math.min(mutationRateNumber, minMutationRateNumber);
                    } catch (NumberFormatException e) {
                        throw new CorruptedCsvException("Unable to parse csv", e);
                    }
                }
                int h = maxMutationRateNumber - minMutationRateNumber + 1;
                int w = piClasses.size();
                numberToPiClass.addAll(piClasses);
                for (int i = 0; i < numberToPiClass.size(); i++) {
                    piClassToNumber.put(numberToPiClass.get(i), i);
                }
                final SimpleMatrix matrix = new SimpleMatrix(h, w);
                for (List<String> row : data) {
                    int classId;
                    double mutationRate;
                    double expectedTime;
                    try {
                        classId = Integer.parseInt(row.get(0));
                        mutationRate = Double.parseDouble(row.get(1));
                        expectedTime = Double.parseDouble(row.get(2));
                    } catch (NumberFormatException e) {
                        throw new CorruptedCsvException("Unable to parse csv", e);
                    }
                    int mutationRateNumber = integerProbabilityBijection.probabilityToInteger(mutationRate);
                    int piClassNumber = piClassToNumber.get(classId);
                    matrix.set(mutationRateNumber - minMutationRateNumber, piClassNumber, expectedTime);
                }
                return matrix;
            }
        }.loadAndGetProcessedData(csvFilePath.toAbsolutePath().toString());
        return new HeatMap(data, integerProbabilityBijection, piClassToNumber, numberToPiClass);
    }

    @NotNull
    public AbstractColouring createColouring(@NotNull AbstractColouring.ColoringStrategy strategy) {
        return AbstractColouring.createColoring(data, strategy);
    }

    public double getExpectedTime(int piExistenceClass, double mutationProbability) {
        Integer rowIndex = getRowIndex(mutationProbability);
        Integer columnIndex = getColumnIndex(piExistenceClass);
        if (rowIndex == null || columnIndex == null) {
            return 0.;
        }
        return data.get(rowIndex, columnIndex);
    }

    @NotNull
    public Pair<Integer, Integer> getCellRowCol(int piExistenceClass, double mutationProbability) {
        return new Pair<>(getRowIndex(mutationProbability), getColumnIndex(piExistenceClass));
    }

    public double getValueInCell(int rowNumber, int colNumber) {
        return data.get(rowNumber, colNumber);
    }

    public int getRowsNumber() {
        return data.numRows();
    }

    public int getColumnsNumber() {
        return data.numCols();
    }

    public ArrayList<Double> getMinInEveryColumn() {
        final ArrayList<Double> minInHeatMapColumn = new ArrayList<>(this.getColumnsNumber());
        for (int i = 0; i < this.getColumnsNumber(); i++) {
            double minValue = Double.POSITIVE_INFINITY;
            for (int j = 0; j < this.getRowsNumber(); j++) {
                minValue = Math.min(this.getValueInCell(j, i), minValue);
            }
            minInHeatMapColumn.add(minValue);
        }
        return minInHeatMapColumn;
    }

    @Nullable
    public Integer getColumnIndex(int piExistenceClass) {
        return piExistenceClassToNumber.get(piExistenceClass);
    }

    @Nullable
    public Integer getRowIndex(double mutationProbability) {
        return integerProbabilityBijection.probabilityToInteger(mutationProbability);
    }

    public void saveToFile(@NotNull HeatMapPainter painter, @NotNull Path filePath) throws IOException {
        final BufferedImage heatMapImage = painter.getHeatMapImageWithAlgorithmsTracesAbove(this);
        final File file = new File(filePath.toAbsolutePath().toString());
        ImageIO.write(heatMapImage, "png", file);
    }
}
