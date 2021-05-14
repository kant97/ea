package optimal.optimal2;

import optimal.configuration.MainConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

public class ResultsContainer {
    private final MainConfiguration configuration;
    private final Map<String, String> extraData = new HashMap<>();

    public ResultsContainer(MainConfiguration configuration) {
        this.configuration = configuration;
    }

    public void addExtraData(@NotNull String key, @NotNull String value) {
        extraData.put(key, value);
    }

    @NotNull
    public String getExtraDataValue(@NotNull String key) {
        return Optional.ofNullable(extraData.get(key)).orElseThrow(() -> new NoSuchElementException("Key " + key + " is not found in extra data"));
    }

    @Override
    public String toString() {
        return "ResultEntity{" +
                "configuration=" + configuration.toString() +
                extraData +
                '}';
    }

    public MainConfiguration getConfiguration() {
        return configuration;
    }
}
