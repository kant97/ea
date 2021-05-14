package optimal.optimal2;

import optimal.configuration.MainConfiguration;
import optimal.configuration.loaders.MainConfigurationLoader;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class ExperimentEnvironment {
    private final Path envDirPath;
    private final Path resultsDirPath;
    private final Path resultsFilePath;
    private final Path savedConfigFilePath;

    public Path getEnvDirPath() {
        return envDirPath;
    }

    public Path getResultsDirPath() {
        return resultsDirPath;
    }

    public Path getResultsFilePath() {
        return resultsFilePath;
    }

    public Path getSavedConfigFilePath() {
        return savedConfigFilePath;
    }

    private ExperimentEnvironment(Path envDirPath, Path resultsDirPath, Path resultsFilePath, Path savedConfigFilePath) {
        this.envDirPath = envDirPath;
        this.resultsDirPath = resultsDirPath;
        this.resultsFilePath = resultsFilePath;
        this.savedConfigFilePath = savedConfigFilePath;
    }

    private static void createDirForResults(@NotNull String resultsDirPath) {
        final Path path = Paths.get(resultsDirPath);
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static @NotNull String generateResultsDirectoryName(int lambda) {
        return "processed_lambda=" + lambda;
    }

    private static int generateResultId(@NotNull String parentOfResultFilePath, boolean isGenerateFreshName) {
        final Path path = Paths.get(parentOfResultFilePath);
        int cnt = 0;
        for (File file : Objects.requireNonNull(path.toFile().listFiles(File::isFile))) {
            if (!file.getName().startsWith("result")) {
                continue;
            }
            cnt++;
        }
        int newResultId = cnt;
        if (cnt > 0) {
            newResultId -= isGenerateFreshName ? 0 : 1;
        }
        return newResultId;
    }

    private static void saveConfigToFile(@NotNull MainConfiguration configuration, @NotNull String filePath) throws IOException {
        final String serializedConfiguration = new MainConfigurationLoader("whatever").serializeConfiguration(configuration);
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(Paths.get(filePath).toFile()))) {
            writer.write(serializedConfiguration);
        }
    }

    public static ExperimentEnvironment createEnvironmentForResults(@NotNull MainConfiguration mainConfiguration, @NotNull String envDirPath, boolean isGenerateFreshResultsFile) {
        final String resultsDirPath = envDirPath + '/' + generateResultsDirectoryName(mainConfiguration.getAlgorithmConfig().getLambda());
        createDirForResults(resultsDirPath);
        final int id = generateResultId(resultsDirPath, isGenerateFreshResultsFile);
        final String storedConfigFilePath = resultsDirPath + '/' + "config" + id + ".json";
        try {
            saveConfigToFile(mainConfiguration, storedConfigFilePath);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to save configuration for the experiment", e);
        }
        final String resultsFilePath = resultsDirPath + '/' + "result" + id + ".csv";
        return new ExperimentEnvironment(Paths.get(envDirPath), Paths.get(resultsDirPath), Paths.get(resultsFilePath), Paths.get(storedConfigFilePath));
    }
}
