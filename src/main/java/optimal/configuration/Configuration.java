package optimal.configuration;


import java.util.List;

public class Configuration {
    public final int amountOfThreads;
    public final List<OneExperimentConfiguration> experimentConfigurations;

    public Configuration(int amountOfThreads, List<OneExperimentConfiguration> experimentConfigurations) {
        this.amountOfThreads = amountOfThreads;
        this.experimentConfigurations = experimentConfigurations;
    }
}
