package optimal.configuration;

import optimal.configuration.algorithms.AlgorithmConfig;
import optimal.configuration.probability.ProbabilitySamplingConfiguration;
import optimal.configuration.problems.ProblemConfig;
import optimal.configuration.runs.StopConditionConfiguration;

import javax.naming.ConfigurationException;
import java.util.Objects;

public class OneExperimentConfiguration extends AbstractSingleExperimentConfiguration {
    public static final int DEFAULT_NUMBER_OF_ONE_STEP_REPETITIONS = 10000;
    public final StopConditionConfiguration stopConditionConfig;

    public OneExperimentConfiguration(ProblemConfig problemConfig, AlgorithmConfig algorithmConfig,
                                      StopConditionConfiguration stopConditionConfig,
                                      int beginFitness, int endFitness,
                                      ProbabilitySamplingConfiguration probabilityEnumeration) {
        super(problemConfig, algorithmConfig, beginFitness, endFitness, probabilityEnumeration);
        this.stopConditionConfig = stopConditionConfig;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        OneExperimentConfiguration that = (OneExperimentConfiguration) o;
        return stopConditionConfig.equals(that.stopConditionConfig);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), stopConditionConfig);
    }

    @Override
    public void validate() throws ConfigurationException {
        super.validate();
        if (stopConditionConfig == null) {
            throw new ConfigurationException("Strategy of repetitions of one step of the algorithm is not set");
        }
    }
}
