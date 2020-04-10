package optimal.configuration;


import javax.naming.ConfigurationException;
import java.util.List;

public class Configuration {
    public final int amountOfThreads;
    public final List<OneExperimentConfiguration> experimentConfigurations;

    public Configuration(int amountOfThreads, List<OneExperimentConfiguration> experimentConfigurations) {
        this.amountOfThreads = amountOfThreads;
        this.experimentConfigurations = experimentConfigurations;
    }

    public void validate() throws ConfigurationException {
        if (amountOfThreads <= 0) {
            throw new ConfigurationException("Only positive amount of threads is possible");
        }
        for (OneExperimentConfiguration configuration : experimentConfigurations) {
            configuration.validate();
        }
    }
}
