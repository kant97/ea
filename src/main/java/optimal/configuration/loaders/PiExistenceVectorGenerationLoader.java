package optimal.configuration.loaders;

import com.google.gson.Gson;
import optimal.configuration.PiExistenceTransitionClusterConfiguration;
import org.jetbrains.annotations.NotNull;

public class PiExistenceVectorGenerationLoader  extends ConfigurationsLoader<PiExistenceTransitionClusterConfiguration> {
    private String myFileName;
    private final ProbabilityVectorGenerationConfigurationLoader myProbabilityVectorGenerationConfigurationLoader;

    public PiExistenceVectorGenerationLoader(String myFileName) {
        this.myFileName = myFileName;
        this.myProbabilityVectorGenerationConfigurationLoader = new ProbabilityVectorGenerationConfigurationLoader(myFileName);
    }

    @Override
    protected Class<PiExistenceTransitionClusterConfiguration> getConfigurationTypeClass() {
        return PiExistenceTransitionClusterConfiguration.class;
    }

    @Override
    protected @NotNull String getConfigurationFilename() {
        return myFileName;
    }

    @Override
    protected Gson createGsonInstanceForSerialization() {
        return myProbabilityVectorGenerationConfigurationLoader.createGsonInstanceForSerialization();
    }

    @Override
    protected @NotNull Gson createGsonInstanceForDeserialization() {
        return myProbabilityVectorGenerationConfigurationLoader.createGsonInstanceForDeserialization();
    }
}
