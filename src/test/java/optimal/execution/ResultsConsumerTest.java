package optimal.execution;

import optimal.configuration.UsualConfiguration;
import optimal.configuration.loaders.UsualConfigurationsLoader;
import optimal.execution.events.EventType;
import org.junit.jupiter.api.Test;

import javax.naming.ConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.StandardOpenOption;

import static org.mockito.Mockito.mock;

class ResultsConsumerTest {

    @Test
    void waitAndLogResults() throws IOException, URISyntaxException, InterruptedException, ConfigurationException {
        ResultsConsumer writer = ResultsConsumer.createResultsConsumer(false);
        writer.addWriter(new ResultWriter("testResults.csv", EventType.OPTIMAL_RESULT_READY, StandardOpenOption.APPEND));

        UsualConfiguration configuration = new UsualConfigurationsLoader().getConfigurationFromResources();
        writer.consumeResult(new ResultEntity(configuration.experimentConfigurations.get(0), 17, 0.12, 300.14),
                EventType.OPTIMAL_RESULT_READY);
        writer.waitAndLogResults();
    }
}