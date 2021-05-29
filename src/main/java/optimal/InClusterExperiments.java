package optimal;

import optimal.configuration.PiExistenceTransitionClusterConfiguration;
import optimal.configuration.loaders.ManyExperimentsConfigurationLoader;
import optimal.configuration.loaders.PiExistenceVectorGenerationLoader;
import optimal.execution.cluster.GeneratedVectorProcessing;
import optimal.execution.cluster.Utils;
import optimal.execution.cluster.generation.configs.ClusterJsonConfigsGenerator;
import optimal.execution.cluster.generation.vectors.FitnessLevelVectorGenerator;
import optimal.optimal2.generation.ClusterPlacesManager;
import optimal.optimal2.generation.InClusterTransitionsGenerator;
import optimal.optimal2.generation.PiExistenceTransitionsWriter;
import org.jetbrains.annotations.Nullable;

import javax.naming.ConfigurationException;
import java.io.IOException;

public class InClusterExperiments {
    private static final Tool[] TOOLS = new Tool[]{new ConfigsGenerator(), new VectorGenerator(),
            new VectorProcessor()};

    private static String getHelp() {
        StringBuilder stringBuilder = new StringBuilder("Available tools are:\n");
        for (Tool tool : TOOLS) {
            stringBuilder.append(tool.getHelp());
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    private static abstract class Tool {
        final String name;

        Tool(String name) {
            this.name = name;
        }

        abstract void handle(String[] args);

        abstract String getHelp();
    }

    private static final class ConfigsGenerator extends Tool {
        ConfigsGenerator() {
            super("generateConfigs");
        }

        @Override
        void handle(String[] args) {
            final ClusterJsonConfigsGenerator clusterJsonConfigsGenerator =
                    new ClusterJsonConfigsGenerator(new ManyExperimentsConfigurationLoader(Utils.GLOBAL_CONFIGS_FILE_NAME_JSON));
            clusterJsonConfigsGenerator.generateJsonConfigs();
            System.out.println("Generated " + clusterJsonConfigsGenerator.getAmountOfGeneratedFiles() + " files.\n" +
                    "Files ids lay in [" + clusterJsonConfigsGenerator.getMinFileId() + ", " +
                    clusterJsonConfigsGenerator.getMaxFileId() + "].");
        }

        @Override
        String getHelp() {
            return name;
        }
    }

    private static final class VectorGenerator extends Tool {
        private static final String[] MODES = new String[]{"piExist", "piRecovery", "usualRecovery", "usual"};

        private @Nullable String myConfigFileName = null;
        private @Nullable String myMode = null;
        private @Nullable ClusterPlacesManager clusterPlacesManager = null;
        private @Nullable PiExistenceTransitionClusterConfiguration piExistenceTransitionClusterConfiguration = null;
        private @Nullable InClusterTransitionsGenerator inClusterTransitionsGenerator = null;

        VectorGenerator() {
            super("generateTransitions");
        }

        private void parseArguments(String[] args) {
            if (args.length < 2) {
                throw new IllegalStateException("The configuration file name is supposed to be passed as the only " +
                        "argument to the tool + " + name + ".");
            }
            myConfigFileName = args[1];
            if (args.length == 3) {
                boolean ok = false;
                final String pickedMode = args[2];
                for (String mode : MODES) {
                    if (pickedMode.equals(mode)) {
                        ok = true;
                        break;
                    }
                }
                if (!ok) {
                    throw new IllegalStateException("You picked an unknown option " + pickedMode
                            + "\nHelp:\n" + getHelp());
                }
                myMode = args[2];
            }
        }

        void createPiExistenceExperimentEnv() {
            inClusterTransitionsGenerator = new InClusterTransitionsGenerator();
            final PiExistenceVectorGenerationLoader piExistenceVectorGenerationLoader = new PiExistenceVectorGenerationLoader(myConfigFileName);
            try {
                this.piExistenceTransitionClusterConfiguration = piExistenceVectorGenerationLoader.getConfiguration();
            } catch (IOException | ConfigurationException e) {
                throw new IllegalStateException("Unable to load configuration for the experiment", e);
            }
            this.clusterPlacesManager = new ClusterPlacesManager(".", piExistenceTransitionClusterConfiguration.getOutputDirectory(), piExistenceTransitionClusterConfiguration.getOutputFileName());
        }

        void runMode() {
            assert myConfigFileName != null;
            if (myMode == null || myMode.equals(MODES[3])) {
                FitnessLevelVectorGenerator fitnessLevelVectorGenerator = FitnessLevelVectorGenerator.createFitnessLevelVectorGenerator(myConfigFileName, FitnessLevelVectorGenerator.Strategy.GENERATE_ALL);
                fitnessLevelVectorGenerator.generateFitnessIncreasesVector();
                return;
            }
            if (myMode.equals(MODES[2])) {
                FitnessLevelVectorGenerator fitnessLevelVectorGenerator = FitnessLevelVectorGenerator.createFitnessLevelVectorGenerator(myConfigFileName, FitnessLevelVectorGenerator.Strategy.RECOVERY);
                fitnessLevelVectorGenerator.generateFitnessIncreasesVector();
                return;
            }
            if (myMode.equals(MODES[0])) {
                createPiExistenceExperimentEnv();
                assert inClusterTransitionsGenerator != null;
                assert this.piExistenceTransitionClusterConfiguration != null;
                assert this.clusterPlacesManager != null;
                final PiExistenceTransitionsWriter transitionsWriter = new PiExistenceTransitionsWriter(this.clusterPlacesManager);
                inClusterTransitionsGenerator.generateTransitions(this.piExistenceTransitionClusterConfiguration, transitionsWriter);
                return;
            }
            if (myMode.equals(MODES[1])) {
                createPiExistenceExperimentEnv();
                assert inClusterTransitionsGenerator != null;
                assert this.piExistenceTransitionClusterConfiguration != null;
                assert this.clusterPlacesManager != null;
                inClusterTransitionsGenerator.recoveryTransitions(this.piExistenceTransitionClusterConfiguration, this.clusterPlacesManager);
            }
        }

        @Override
        void handle(String[] args) {
            parseArguments(args);
            runMode();
        }

        @Override
        String getHelp() {
            final StringBuilder s = new StringBuilder(name + " with one option - name of the configuration file"
                    + " and one optional option - mode to run.\n Available modes are: ");
            for (String mode : MODES) {
                s.append(mode);
                s.append(", ");
            }
            return s.toString();
        }
    }

    private static final class VectorProcessor extends Tool {

        VectorProcessor() {
            super("vectorsProcessing");
        }

        @Override
        void handle(String[] args) {
            if (args.length != 2) {
                throw new IllegalStateException("The name of directory with all vectors directories is supposed to be" +
                        " passed as the only argument to the tool + " + name + ".");
            }
            final GeneratedVectorProcessing generatedVectorProcessing = new GeneratedVectorProcessing(args[1]);
            generatedVectorProcessing.processAllVectors();
        }

        @Override
        String getHelp() {
            return name + " (with one option - name of directory with all vectors directories)";
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Pass the name of a tool to execute:\n" + getHelp());
        }
        final String requiredToolName = args[0];
        boolean toolWasExecuted = false;
        for (Tool tool : TOOLS) {
            if (tool.name.equals(requiredToolName)) {
                tool.handle(args);
                toolWasExecuted = true;
                break;
            }
        }
        if (!toolWasExecuted) {
            throw new IllegalArgumentException("Unknown tool with name: " + requiredToolName + "\n Try one of the " +
                    "available options:\n" + getHelp());
        }
    }
}
