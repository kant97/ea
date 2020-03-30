package optimal;

import optimal.execution.ExperimentRunner;

import java.io.IOException;
import java.net.URISyntaxException;

public class MultithreadingExperimenters {
    public static void main(String[] args) {
        try {
            new ExperimentRunner().runExperiments();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
