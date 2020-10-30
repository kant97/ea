package pictures;

import org.ejml.simple.SimpleMatrix;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MatrixDataProcessor extends DataProcessor<SimpleMatrix> {
    private final int myMutationRateColumnIndex;
    private final int myFitnessColumnIndex;
    private final int myRunTimeColumnIndex;

    public MatrixDataProcessor(@NotNull String csvFileName, int mutationRateColumnIndex, int fitnessColumnIndex,
                               int runTimeColumnIndex) {
        super(csvFileName);
        this.myMutationRateColumnIndex = mutationRateColumnIndex;
        this.myFitnessColumnIndex = fitnessColumnIndex;
        this.myRunTimeColumnIndex = runTimeColumnIndex;
    }

    private int getAmountOfRows() {
        int ans = 0;
        int element = Integer.parseInt(myRecords.get(0).get(myFitnessColumnIndex));
        final int firstElement = element;
        while (firstElement == element) {
            ans += 1;
            element = Integer.parseInt(myRecords.get(ans).get(myFitnessColumnIndex));
        }
        return ans;
    }

    @Override
    public SimpleMatrix getProcessedData() {
        if (myRecords == null) {
            throw new IllegalStateException("Data was not loaded");
        }
        final int amountOfRows = getAmountOfRows();
        final SimpleMatrix dataMatrix = new SimpleMatrix(1, myRecords.size());
        int curColumn = 0;
        for (List<String> row : myRecords) {
            dataMatrix.set(0, curColumn++, Double.parseDouble(row.get(myRunTimeColumnIndex)));
        }
        dataMatrix.reshape(myRecords.size() / amountOfRows, amountOfRows);
        return dataMatrix.transpose();
    }

}
