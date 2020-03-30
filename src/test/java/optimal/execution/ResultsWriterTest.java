package optimal.execution;

import optimal.configuration.Configuration;
import optimal.configuration.ConfigurationsLoader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

class ResultsWriterTest {

    @Test
    void waitAndLogResults() throws IOException, URISyntaxException, InterruptedException {
        ResultsWriter writer = new ResultsWriter("testsResults.csv");
        Configuration configuration = new ConfigurationsLoader().getConfiguration();
        writer.queue.put(new ResultEntity(configuration.experimentConfigurations.get(0), 17, 0.12, 300.14));
        writer.waitAndLogResults();
    }
}