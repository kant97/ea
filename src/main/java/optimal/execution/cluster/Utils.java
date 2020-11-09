package optimal.execution.cluster;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Utils {
    public final static String RESULTS_DIRECTORY_NAME = "vectors";
//    public static final String RESULTS_DIRECTORY = "./" + RESULTS_DIRECTORY_NAME + "/";
    private static final String CONFIGURATIONS_DIRECTORY_NAME = "jsonConfigs";
    public static final String CONFIGURATIONS_DIRECTORY = "./" + CONFIGURATIONS_DIRECTORY_NAME + "/";
    public static final String GLOBAL_CONFIGS_FILE_NAME_JSON = "global_configs.json";


    public static Path createResultsDirectoryInFsIfNotExists(String resultsDirectory) {
        return createDirectory(resultsDirectory);
    }

    @NotNull
    public static Path createDirectory(String dir) {
        final Path path = Paths.get(dir);
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return path;
    }

    public static Path createConfigsDirectoryInFsIfNotExists() {
        return createDirectory(CONFIGURATIONS_DIRECTORY);
    }

    public static boolean doesDirectoryExist(String directory) {
        final Path path = Paths.get(directory);
        return Files.exists(path);
    }
}
