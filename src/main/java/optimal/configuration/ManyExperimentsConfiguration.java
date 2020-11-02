package optimal.configuration;

import javax.naming.ConfigurationException;
import java.util.List;

public class ManyExperimentsConfiguration implements ValidatableConfiguration {
    private final List<OneExperimentConfiguration> configurations;

    public ManyExperimentsConfiguration(List<OneExperimentConfiguration> configurations) {
        this.configurations = configurations;
    }

    @Override
    public void validate() throws ConfigurationException {
        for (OneExperimentConfiguration oneExperimentConfiguration : configurations) {
            oneExperimentConfiguration.validate();
        }
    }

    public List<OneExperimentConfiguration> getConfigurations() {
        return configurations;
    }
}
