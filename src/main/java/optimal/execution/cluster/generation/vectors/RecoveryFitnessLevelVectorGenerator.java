package optimal.execution.cluster.generation.vectors;

import optimal.utils.ProbabilityVectorProcessor;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class RecoveryFitnessLevelVectorGenerator extends FitnessLevelVectorGenerator {
    public RecoveryFitnessLevelVectorGenerator(String configFileName, String dirsForVectorDirsName) {
        super(configFileName, dirsForVectorDirsName, Strategy.RECOVERY);
    }

    @Override
    protected boolean needGenerateVector(@NotNull String vectorFqn) {
        if (!Files.exists(Paths.get(vectorFqn))) {
            return true;
        }
        final ProbabilityVectorProcessorImpl probabilityVectorProcessor = new ProbabilityVectorProcessorImpl(vectorFqn);
        probabilityVectorProcessor.loadData();
        probabilityVectorProcessor.getProcessedData();
        return probabilityVectorProcessor.needRecovering;
    }

    private final static class ProbabilityVectorProcessorImpl extends ProbabilityVectorProcessor {
        boolean needRecovering = false;

        public ProbabilityVectorProcessorImpl(@NotNull String csvFileName) {
            super(csvFileName);
        }

        @Override
        protected void onParseNumberError(NumberFormatException e, @NotNull List<String> record) {
            needRecovering = true;
        }

        @Override
        protected boolean shouldContinue() {
            return !needRecovering;
        }

        @Override
        protected void onEmptyRecords() {
            needRecovering = true;
        }
    }
}
