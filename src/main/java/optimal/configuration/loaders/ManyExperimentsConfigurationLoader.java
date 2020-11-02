package optimal.configuration.loaders;

import com.google.gson.Gson;
import optimal.configuration.ManyExperimentsConfiguration;
import org.jetbrains.annotations.NotNull;

public class ManyExperimentsConfigurationLoader extends ConfigurationsLoader<ManyExperimentsConfiguration> {
    private final String myFileName;
    private final OneExperimentConfigurationLoader myOneExperimentConfigurationLoader;

    public ManyExperimentsConfigurationLoader(String myFileName) {
        this.myFileName = myFileName;
        myOneExperimentConfigurationLoader = new OneExperimentConfigurationLoader(myFileName);
    }

    @Override
    protected Class<ManyExperimentsConfiguration> getConfigurationTypeClass() {
        return ManyExperimentsConfiguration.class;
    }

    @Override
    protected @NotNull String getConfigurationFilename() {
        return myFileName;
    }

    @Override
    protected @NotNull Gson createGsonInstanceForDeserialization() {
        return myOneExperimentConfigurationLoader.createGsonInstanceForDeserialization();
    }

    @Override
    protected Gson createGsonInstanceForSerialization() {
        return myOneExperimentConfigurationLoader.createGsonInstanceForSerialization();
    }

    public OneExperimentConfigurationLoader getOneExperimentConfigurationSerializer() {
        return myOneExperimentConfigurationLoader;
    }
}
