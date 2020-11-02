package optimal;

import optimal.configuration.loaders.OneExperimentConfigurationLoader;
import optimal.execution.cluster.ClusterJsonConfigsGenerator;
import optimal.execution.cluster.FitnessLevelVectorGenerator;
import optimal.execution.cluster.Utils;

public class InClusterExperiments {
    private static final Tool[] TOOLS = new Tool[]{new ConfigsGenerator(), new VectorGenerator()};

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
            super("configsGenerator");
        }

        @Override
        void handle(String[] args) {
            final ClusterJsonConfigsGenerator clusterJsonConfigsGenerator =
                    new ClusterJsonConfigsGenerator(new OneExperimentConfigurationLoader(Utils.GLOBAL_CONFIGS_FILE_NAME_JSON));
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
        VectorGenerator() {
            super("vectorGenerator");
        }

        @Override
        void handle(String[] args) {
            if (args.length != 2) {
                throw new IllegalStateException("The configuration file name is supposed to be passed as the only " +
                        "argument to the tool + " + name + ".");
            }
            final String configFileName = args[1];
            final FitnessLevelVectorGenerator fitnessLevelVectorGenerator =
                    new FitnessLevelVectorGenerator(configFileName);
            fitnessLevelVectorGenerator.generateFitnessIncreasesVector();
        }

        @Override
        String getHelp() {
            return name + " (with one option - name of the configuration file)";
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Pass a name of the tool to execute:\n" + getHelp());
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
