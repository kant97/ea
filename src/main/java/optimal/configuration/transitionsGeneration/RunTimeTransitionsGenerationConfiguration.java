package optimal.configuration.transitionsGeneration;

import optimal.configuration.ConfigurationVisitor;
import optimal.configuration.probability.ProbabilitySamplingConfiguration;
import optimal.configuration.runs.StopConditionConfiguration;
import org.jetbrains.annotations.NotNull;

public class RunTimeTransitionsGenerationConfiguration extends TransitionsGenerationConfiguration {
    public RunTimeTransitionsGenerationConfiguration(@NotNull ProbabilitySamplingConfiguration probabilityEnumeration, @NotNull StopConditionConfiguration stopConditionConfig) {
        super(TransitionsGenerationStrategy.RUN_TIME_TRANSITIONS_GENERATION, probabilityEnumeration, stopConditionConfig);
    }
}
