package optimal;

import optimal.execution.ExperimentRunner;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class MultithreadingExperimenters {
    public static void main(String[] args) {
        try {
            final Instant start = Instant.now();
            new ExperimentRunner().runExperiments();
            final Instant end = Instant.now();
            System.out.println("\nExecution time: " + Duration.between(start, end).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
