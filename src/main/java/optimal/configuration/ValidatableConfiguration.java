package optimal.configuration;

import javax.naming.ConfigurationException;

public interface ValidatableConfiguration {
    void validate() throws ConfigurationException;
}
