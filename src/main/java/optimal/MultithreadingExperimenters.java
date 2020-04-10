package optimal;

import optimal.execution.ExperimentRunner;

import java.io.IOException;

public class MultithreadingExperimenters {
    public static void main(String[] args) {
        try {
            new ExperimentRunner().runExperiments();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
