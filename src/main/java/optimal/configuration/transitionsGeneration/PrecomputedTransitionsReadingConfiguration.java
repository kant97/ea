package optimal.configuration.transitionsGeneration;

import com.google.gson.annotations.SerializedName;
import optimal.configuration.probability.ProbabilitySamplingConfiguration;
import optimal.configuration.runs.StopConditionConfiguration;
import org.jetbrains.annotations.NotNull;

public class PrecomputedTransitionsReadingConfiguration extends TransitionsGenerationConfiguration {
    @SerializedName(value = "csvDirPath")
    private final String directoryWithPrecomputedFilesPath;

    @SerializedName(value = "validationConfigPath")
    private final String configForWhichTheFilesWereComputedPath;

    public PrecomputedTransitionsReadingConfiguration(@NotNull TransitionsGenerationStrategy strategy,
                                                      @NotNull ProbabilitySamplingConfiguration probabilityEnumeration,
                                                      @NotNull StopConditionConfiguration stopConditionConfig,
                                                      @NotNull String directoryWithPrecomputedFilesPath,
                                                      @NotNull String configForWhichTheFilesWereComputedPath) {
        super(strategy, probabilityEnumeration, stopConditionConfig);
        this.directoryWithPrecomputedFilesPath = directoryWithPrecomputedFilesPath;
        this.configForWhichTheFilesWereComputedPath = configForWhichTheFilesWereComputedPath;
    }

    public String getDirectoryWithPrecomputedFilesPath() {
        return directoryWithPrecomputedFilesPath;
    }

    public String getConfigForWhichTheFilesWereComputedPath() {
        return configForWhichTheFilesWereComputedPath;
    }
}
