package optimal.optimal2.generation;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ClusterPlacesManager {
    private final String dirForDirsName;
    private final String outputDirectoryForThisExperiment;
    private final String outputFileName;

    public ClusterPlacesManager(String dirForDirsName, String outputDirectoryForThisExperiment, String outputFileName) {
        this.dirForDirsName = dirForDirsName;
        this.outputDirectoryForThisExperiment = outputDirectoryForThisExperiment;
        this.outputFileName = outputFileName;
    }

    public Path getPathToOutputFile() {
        return Paths.get(getStringPathToOutputFile());
    }

    public String getStringPathToOutputFile() {
        return dirForDirsName + '/' + outputDirectoryForThisExperiment + '/' + outputFileName;
    }

    public String getStringFileDirectory() {
        return dirForDirsName + '/' + outputDirectoryForThisExperiment;
    }
}
