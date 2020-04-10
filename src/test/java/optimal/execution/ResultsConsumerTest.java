package optimal.execution;

import optimal.configuration.Configuration;
import optimal.configuration.ConfigurationsLoader;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.naming.ConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResultsConsumerTest {

    @Test
    void waitAndLogResults() throws IOException, URISyntaxException, InterruptedException, ConfigurationException {
        ResultsConsumer writer = ResultsConsumer.createResultsConsumer(false);
        writer.addWriter(new ResultWriter("testResults.csv", ResultsConsumer.ResultType.OPTIMAL));

        Configuration configuration = new ConfigurationsLoader().getConfiguration();
        writer.consumeResult(new ResultEntity(configuration.experimentConfigurations.get(0), 17, 0.12, 300.14),
                ResultsConsumer.ResultType.OPTIMAL);
        writer.waitAndLogResults();
    }
}