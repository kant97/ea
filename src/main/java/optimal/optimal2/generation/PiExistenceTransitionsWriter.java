package optimal.optimal2.generation;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Path;
import java.util.Map;

public class PiExistenceTransitionsWriter {
    private static final String CSV_FILE_HEADER = "piExistenceClass,probability\n";
    private final Path filePath;

    public PiExistenceTransitionsWriter(@NotNull ClusterPlacesManager placesManager) {
        this.filePath = placesManager.getPathToOutputFile();
    }

    public void writeResults(@NotNull Map<Integer, Double> transitions) {
        try (final BufferedWriter writer =
                     new BufferedWriter(new FileWriter(filePath.toFile()))) {
            writeResultsWithWriter(transitions, writer);
        } catch (IOException e) {
            try {
                System.out.println("Failed to write results to file, so writing them to stdout");
                writeResultsWithWriter(transitions, new PrintWriter(new OutputStreamWriter(System.out)));
            } catch (IOException ioException) {
                throw new IllegalStateException(ioException);
            }
        }
    }

    private void writeResultsWithWriter(@NotNull Map<Integer, Double> transitions, @NotNull Writer writer) throws IOException {
        writer.write(CSV_FILE_HEADER);
        transitions.forEach((id, p) -> {
            try {
                writer.write(id + "," + p + "\n");
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        });
    }

}
