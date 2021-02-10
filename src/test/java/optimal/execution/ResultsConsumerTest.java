package optimal.execution;

import optimal.configuration.ManyExperimentsConfiguration;
import optimal.configuration.loaders.ManyExperimentsConfigurationLoader;
import optimal.execution.events.EventType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.naming.ConfigurationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

class ResultsConsumerTest {

    @Test
    void testResultsLogging(@TempDir Path tempDir) throws IOException, InterruptedException, ConfigurationException {
        Path restResultsFile = tempDir.resolve("testResults.csv");
        ResultsConsumer writer = ResultsConsumer.createResultsConsumer(false);
        writer.addWriter(new ResultWriter(restResultsFile.toFile().getAbsolutePath(), EventType.OPTIMAL_RESULT_READY, StandardOpenOption.APPEND));
        ManyExperimentsConfiguration configuration = new ManyExperimentsConfigurationLoader("experimentsConfiguration.json").getConfigurationFromResources();
        writer.consumeResult(new ResultEntity(configuration.getConfigurations().get(0), 17, 0.12, 300.14), EventType.OPTIMAL_RESULT_READY);
        writer.consumeResult(new ResultEntity(configuration.getConfigurations().get(1), 18, 0.13, 301.15), EventType.OPTIMAL_RESULT_READY);
        writer.consumeResult(new ResultEntity(configuration.getConfigurations().get(2), 19, 0.14, 303.16), EventType.OPTIMAL_RESULT_READY);
        writer.waitAndLogResults();
        writer.waitAndLogResults();
        writer.waitAndLogResults();
        List<String> expectedLines = Arrays.asList("problem,algorithm,problemSize,lambda,beginFitness,endFitness,probabilityEnumeration,vectorGeneration,fitness,probability,optimizationTime",
                "ONE_MAX_NEUTRALITY_3,{type=TWO_RATE; lb=0.01},100,10,17,34,{type=ITERATIVE; minProbability=0.1; maxProbability=0.5; precision=0.1},{type=FIXED_SUCCESS; amountOfSuccess=5000; globalMaximumRuns=10000},17,0.12,300.14",
                "ONE_MAX,{type=TWO_RATE; lb=0.01},100,10,50,100,{type=ITERATIVE; minProbability=0.1; maxProbability=0.5; precision=0.1},{type=FIXED_SUCCESS; amountOfSuccess=5000; globalMaximumRuns=10000},18,0.13,301.15",
                "ONE_MAX,{type=TWO_RATE; lb=0.01},100,10,50,100,{type=EXPONENTIAL_GRID; base=2.718281828459045; minPowerValue=-9.210340371976182; maxPowerValue=0.0; precisionForPower=0.09210340371976182},{type=FIXED_SUCCESS; amountOfSuccess=5000; globalMaximumRuns=10000},19,0.14,303.16");
        List<String> writtenLines = Files.readAllLines(restResultsFile);
        Assertions.assertArrayEquals(expectedLines.toArray(), writtenLines.toArray());
    }
}