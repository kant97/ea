package optimal.configuration;

import optimal.configuration.algorithms.AlgorithmConfig;
import optimal.configuration.algorithms.TwoRateConfig;
import optimal.configuration.probability.ExponentialGridConfiguration;
import optimal.configuration.probability.IterativeProbabilityConfiguration;
import optimal.configuration.problems.PlateauConfig;
import optimal.configuration.problems.ProblemConfig;
import optimal.configuration.problems.RuggednessConfig;
import optimal.configuration.runs.FixedRunsConfiguration;
import optimal.configuration.runs.FixedSuccessConfiguration;
import optimal.configuration.transitionsGeneration.TransitionsGenerationConfiguration;
import optimal.configuration.vectorGeneration.PrecomputedVectorReadingConfiguration;
import optimal.configuration.vectorGeneration.RunTimeGenerationConfiguration;
import org.jetbrains.annotations.NotNull;

public class CsvExportConfigurationVisitor implements ConfigurationVisitor {
    @Override
    public @NotNull String visitAlgorithmConfig(@NotNull AlgorithmConfig algorithmConfig) {
        return algorithmConfig.getAlgorithmType().name();
    }

    @Override
    public @NotNull String visitTwoRateConfig(@NotNull TwoRateConfig twoRateConfig) {
        return "{type=" + twoRateConfig.getAlgorithmType().name() + "; lb=" + twoRateConfig.getLowerBound() + "}";
    }

    @Override
    public @NotNull String visitExponentialGridConfig(@NotNull ExponentialGridConfiguration exponentialGridConfiguration) {
        return "{type=" + exponentialGridConfiguration.getStrategy().name() + "; base=" + exponentialGridConfiguration.getBase() +
                "; minPowerValue=" + exponentialGridConfiguration.getMinPowerValue() +
                "; maxPowerValue=" + exponentialGridConfiguration.getMaxPowerValue() +
                "; precisionForPower=" + exponentialGridConfiguration.getPrecisionForPower() + "}";
    }

    @Override
    public @NotNull String visitIterativeProbabilityConfig(@NotNull IterativeProbabilityConfiguration iterativeProbabilityConfiguration) {
        return "{type=" + iterativeProbabilityConfiguration.getStrategy().name() +
                "; minProbability=" + iterativeProbabilityConfiguration.minMutationProbability +
                "; maxProbability=" + iterativeProbabilityConfiguration.maxMutationProbability +
                "; precision=" + iterativeProbabilityConfiguration.precisionForProbability + "}";
    }

    @Override
    public @NotNull String visitProblemConfig(@NotNull ProblemConfig problemConfig) {
        return problemConfig.getProblemType().name();
    }

    @Override
    public @NotNull String visitRuggednessConfig(@NotNull RuggednessConfig ruggednessConfig) {
        return "{type=" + ruggednessConfig.getProblemType() +
                "; r=" + ruggednessConfig.getR() + "}";
    }

    @Override
    public @NotNull String visitPlateauConfig(@NotNull PlateauConfig plateauConfig) {
        return "{type=" + plateauConfig.getProblemType() +
                "; k=" + plateauConfig.getK() + "}";
    }

    @Override
    public @NotNull String visitFixedRunsConfig(@NotNull FixedRunsConfiguration fixedRunsConfiguration) {
        return "{type=" + fixedRunsConfiguration.getMyStrategy() +
                "; amountOfRuns=" + fixedRunsConfiguration.getAmountOfRuns() + "}";
    }

    @Override
    public @NotNull String visitFixedSuccessConfig(@NotNull FixedSuccessConfiguration fixedSuccessConfiguration) {
        return "{type=" + fixedSuccessConfiguration.getMyStrategy() +
                "; amountOfSuccess=" + fixedSuccessConfiguration.getAmountOfSuccess() +
                "; globalMaximumRuns=" + fixedSuccessConfiguration.getGlobalMaximumRuns() + "}";
    }

    @Override
    public @NotNull String visitPrecomputedVectorReadingConfig(@NotNull PrecomputedVectorReadingConfiguration vectorsReadingConfiguration) {
        return vectorsReadingConfiguration.getStrategy().name();
    }

    @Override
    public @NotNull String visitRunTimeGenerationConfig(@NotNull RunTimeGenerationConfiguration vectorRuntimeGenerationConfiguration) {
        return "{type=" + vectorRuntimeGenerationConfiguration.getStrategy().name() +
                "; stopCondition=" + vectorRuntimeGenerationConfiguration.getStopConditionConfiguration().accept(this) +
                "}";
    }

    @Override
    public @NotNull String visitOneExperimentConfig(@NotNull OneExperimentConfiguration oneExperimentConfiguration) {
        return oneExperimentConfiguration.problemConfig.accept(this) +
                ',' +
                oneExperimentConfiguration.algorithmConfig.accept(this) +
                ',' +
                oneExperimentConfiguration.problemConfig.getSize() +
                ',' +
                oneExperimentConfiguration.algorithmConfig.getLambda() +
                ',' +
                oneExperimentConfiguration.beginFitness +
                ',' +
                oneExperimentConfiguration.endFitness +
                ',' +
                oneExperimentConfiguration.probabilityEnumeration.accept(this) +
                ',' +
                oneExperimentConfiguration.stopConditionConfig.accept(this);
    }

    @Override
    public @NotNull String visitOptimalMutationRateSearcherConfig(@NotNull OptimalMutationRateSearchingSingleExperimentConfiguration configuration) {
        return configuration.problemConfig.accept(this) +
                ',' +
                configuration.algorithmConfig.accept(this) +
                ',' +
                configuration.problemConfig.getSize() +
                ',' +
                configuration.algorithmConfig.getLambda() +
                ',' +
                configuration.beginFitness +
                ',' +
                configuration.endFitness +
                ',' +
                configuration.probabilityEnumeration.accept(this) +
                ',' +
                configuration.getVectorGenerationConfig().accept(this);
    }

    @Override
    public @NotNull String visitProbabilityVectorGenerationConfiguration(@NotNull ProbabilityVectorGenerationConfiguration configuration) {
        throw new IllegalStateException("Export of this configuration is not supported");
    }

    @Override
    public @NotNull String visitUsualConfiguration(@NotNull UsualConfiguration configuration) {
        throw new IllegalStateException("Export of this configuration is not supported");
    }

    @Override
    public @NotNull String visitTransitionsGenerationConfiguration(@NotNull TransitionsGenerationConfiguration configuration) {
        return "{strategy=" + configuration.getStrategy().name() +
                "; probEnumeration=" + configuration.getProbabilityEnumeration().accept(this) +
                "; stopCondition=" + configuration.getStopConditionConfig().accept(this) +
                "}";
    }

    @Override
    public @NotNull String visitMainConfiguration(@NotNull MainConfiguration configuration) {
        return configuration.getProblemConfig().accept(this) +
                ',' +
                configuration.getAlgorithmConfig().accept(this) +
                ',' +
                configuration.getProblemConfig().getSize() +
                ',' +
                configuration.getAlgorithmConfig().getLambda() +
                ',' +
                configuration.getBeginFitness() +
                ',' +
                configuration.getEndFitness() +
                ',' +
                configuration.getTransitionsGeneration().accept(this);
    }
}
