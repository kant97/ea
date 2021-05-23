package pictures.optimal2Pictures;

import optimal.configuration.MainConfiguration;
import optimal.configuration.loaders.MainConfigurationLoader;
import optimal.optimal2.AbstractPiExistenceClassesManager;
import optimal.probabilitySampling.IntegerToProbabilityBijectiveMapping;
import optimal.probabilitySampling.ProbabilitySpace;
import optimal.utils.CorruptedCsvException;
import pictures.MathExpectationsCalc;

import javax.naming.ConfigurationException;
import java.io.IOException;
import java.nio.file.Paths;

public class BaselineCreationMain {

    public static void main(String[] args) {
        computeForLambda(2);
        computeForLambda(4);
        computeForLambda(8);
        computeForLambda(16);
        computeForLambda(32);
        computeForLambda(64);
        computeForLambda(128);
        computeForLambda(256);
        computeForLambda(512);
    }

    private static void computeForLambda(int lambda) {
        final String configFileFQN = "5-plateau_k2/processed_lambda=" + lambda + "/config0.json";
        final String experimentDataFileFQN = "5-plateau_k2/processed_lambda=" + lambda + "/result0.csv";
        final MainConfigurationLoader loader = new MainConfigurationLoader(configFileFQN);
        final MainConfiguration configuration;
        try {
            configuration = loader.getConfiguration();
        } catch (IOException | ConfigurationException e) {
            throw new IllegalStateException("Unable to parse configurations for the experiment data", e);
        }
        final IntegerToProbabilityBijectiveMapping bijection = ProbabilitySpace.createProbabilitySpace(configuration.getTransitionsGeneration().getProbabilityEnumeration()).createBijectionToIntegers();
        final HeatMap heatMap;
        try {
            heatMap = HeatMap.createHeatMapByCsvData(Paths.get(experimentDataFileFQN), bijection);
        } catch (CorruptedCsvException e) {
            throw new IllegalStateException("Unable to parse results of experiments", e);
        } catch (IOException e) {
            throw new IllegalStateException("IO exception while handling experiment csv data", e);
        }
        AbstractPiExistenceClassesManager abstractPiExistenceClassesManager = AbstractPiExistenceClassesManager.create(configuration.getProblemConfig());
        MathExpectationsCalc mathExpectationsCalc = new MathExpectationsCalc(configuration.getProblemConfig().getSize(), abstractPiExistenceClassesManager.getAnyIndividualById(1));
        double T = mathExpectationsCalc.getPiExistMathExpect(heatMap.getMinInEveryColumn(), abstractPiExistenceClassesManager);
        System.out.println(lambda + "," + T);
    }
}
