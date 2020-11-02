package optimal.configuration;

import optimal.configuration.algorithms.AlgorithmConfig;
import optimal.configuration.algorithms.TwoRateConfig;
import optimal.configuration.probability.ExponentialGridConfiguration;
import optimal.configuration.probability.IterativeProbabilityConfiguration;
import optimal.configuration.problems.ProblemConfig;
import optimal.configuration.problems.RuggednessConfig;
import optimal.configuration.runs.FixedRunsConfiguration;
import optimal.configuration.runs.FixedSuccessConfiguration;
import optimal.configuration.vectorGeneration.PrecomputedVectorReadingConfiguration;
import optimal.configuration.vectorGeneration.RunTimeGenerationConfiguration;
import org.jetbrains.annotations.NotNull;

public interface ConfigurationVisitor {
    @NotNull String visitAlgorithmConfig(@NotNull AlgorithmConfig algorithmConfig);

    @NotNull String visitTwoRateConfig(@NotNull TwoRateConfig twoRateConfig);

    @NotNull String visitExponentialGridConfig(@NotNull ExponentialGridConfiguration exponentialGridConfiguration);

    @NotNull String visitIterativeProbabilityConfig(@NotNull IterativeProbabilityConfiguration iterativeProbabilityConfiguration);

    @NotNull String visitProblemConfig(@NotNull ProblemConfig problemConfig);

    @NotNull String visitRuggednessConfig(@NotNull RuggednessConfig ruggednessConfig);

    @NotNull String visitFixedRunsConfig(@NotNull FixedRunsConfiguration fixedRunsConfiguration);

    @NotNull String visitFixedSuccessConfig(@NotNull FixedSuccessConfiguration fixedSuccessConfiguration);

    @NotNull String visitPrecomputedVectorReadingConfig(@NotNull PrecomputedVectorReadingConfiguration vectorsReadingConfiguration);

    @NotNull String visitRunTimeGenerationConfig(@NotNull RunTimeGenerationConfiguration vectorRuntimeGenerationConfiguration);

    @NotNull String visitOneExperimentConfig(@NotNull OneExperimentConfiguration oneExperimentConfiguration);

    @NotNull String visitOptimalMutationRateSearcherConfig(@NotNull OptimalMutationRateSearchingSingleExperimentConfiguration configuration);

    @NotNull String visitProbabilityVectorGenerationConfiguration(@NotNull ProbabilityVectorGenerationConfiguration configuration);

    @NotNull String visitUsualConfiguration(@NotNull UsualConfiguration configuration);
}
