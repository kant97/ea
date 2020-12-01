package pictures;

import optimal.utils.DataProcessor;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ResultsCollector {
    public static void main(String[] args) {
        new ResultsCollector().printLambdaToRuntime("all2-vectors-ruggedness");
    }

    public static class Table {
        final ArrayList<String> columnNames;
        final int lambda;
        ArrayList<List<String>> data = new ArrayList<>();

        public Table(int lambda) {
            this.lambda = lambda;
            this.columnNames = new ArrayList<>(Arrays.asList("fitness", "mutationRate", "runTime"));
        }

        public void addRow(List<String> dataRow) {
            data.add(dataRow);
        }

        public ArrayList<String> getColumnNames() {
            return columnNames;
        }

        public ArrayList<List<String>> getData() {
            return data;
        }

        public void sortTable() {
            data = data.stream().sorted((r1, r2) -> Integer.compare(Integer.parseInt(r1.get(0)),
                    Integer.parseInt(r2.get(0)))).collect(Collectors.toCollection(ArrayList::new));
        }
    }

    private void printLambdaToRuntime(String directoryWithOptimalResults) {
        final Collection<Table> tables = collectResults(directoryWithOptimalResults);
        final Path directoryPath = Paths.get("/home/kirill.antonov/itmo/Research/data");
        final String texStringTemplate = "\t\\addplot table [x=fitness, y=mutationRate, col sep=comma] " +
                "{data/l$LAMBDA_VALUE$.csv};\n" +
                "\t\\addlegendentry{$ \\lambda = $LAMBDA_VALUE$ $}\n";
        for (Table table : tables) {
            table.sortTable();
            StringBuilder allCsvBulder = new StringBuilder();
//            System.out.println("lambda=" + table.lambda);
            {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < table.getColumnNames().size(); i++) {
                    if (i - 1 >= 0) {
                        sb.append(',');
                    }
                    sb.append(table.getColumnNames().get(i));
                }
                allCsvBulder.append(sb.toString()).append("\n");
            }
            for (int i = 0; i < table.data.size(); i++) {
                List<String> row = table.getData().get(i);
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < row.size(); j++) {
                    if (j - 1 >= 0) {
                        sb.append(',');
                    }
                    sb.append(row.get(j));
                }
                allCsvBulder.append(sb.toString()).append("\n");
            }
//            allCsvBulder.append("\n--------------------------------------------------\n");
            String fileName = "l" + table.lambda + ".csv";
            try (final BufferedWriter writer =
                         new BufferedWriter(new FileWriter(directoryPath.toAbsolutePath().toString() + "/" + fileName))) {
                writer.write(allCsvBulder.toString());
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
            final String texString = texStringTemplate.replace("$LAMBDA_VALUE$", String.valueOf(table.lambda));
            System.out.println(texString);
        }
    }

    private Collection<Table> collectResults(String directoryWithOptimalResults) {
        return getResultsFromDir(directoryWithOptimalResults,
                "results.csv",
                optimalFilePath -> new DataProcessor<Table>(optimalFilePath.toString()) {
                    final int myLambdaIndex = 3;
                    final int myFitnessIndex = 8;
                    final int myProbabilityIndex = 9;
                    final int myOptimizationTimeIndex = 10;

                    @Override
                    public Table getProcessedData() {
                        if (myRecords == null) {
                            throw new IllegalStateException("Data was not loaded");
                        }
                        int lambda = Integer.parseInt(myRecords.get(0).get(myLambdaIndex));
                        Table table = new Table(lambda);
                        for (List<String> record : myRecords) {
                            table.addRow(Arrays.asList(record.get(myFitnessIndex), record.get(myProbabilityIndex),
                                    record.get(myOptimizationTimeIndex)));
                        }
                        return table;
                    }
                }).stream().sorted(Comparator.comparingInt(t -> t.lambda)).collect(Collectors.toCollection(ArrayList::new));
    }

    private Collection<Table> getResultsFromDir(@NotNull String directoryWithOptimalResults, String csvFileName,
                                                @NotNull Function<Path, DataProcessor<Table>> factor) {
        final Path path = Paths.get(directoryWithOptimalResults);
        Collection<Table> ans = new ArrayList<>();
        for (File directory : path.toFile().listFiles(File::isDirectory)) {
            if (!directory.getName().startsWith("optimal")) {
                continue;
            }
            final Optional<File> optimalFile =
                    Arrays.stream(directory.listFiles()).filter(f -> f.getName().equals(csvFileName)).findFirst();
            if (!optimalFile.isPresent()) {
                throw new IllegalStateException("File with optimal results is not found.");
            }
            final DataProcessor<Table> optimalDataProcessor = factor.apply(optimalFile.get().toPath().toAbsolutePath());
//                    new ColorfulPicturePainter.OptimalDataProcessor(optimalFile.get().toPath().toAbsolutePath()
//                    .toString(),
//                            3, 10, 8);
            optimalDataProcessor.loadData();
            ans.add(optimalDataProcessor.getProcessedData());
        }
        return ans;
    }


}
