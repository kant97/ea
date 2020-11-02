package optimal.configuration.runs;

import optimal.configuration.ValidatableConfiguration;
import org.jetbrains.annotations.NotNull;

public abstract class StopConditionConfiguration implements ValidatableConfiguration {
    public enum Strategy {
        FIXED_RUNS {
            @Override
            public String toString() {
                return "FIXED_RUNS";
            }
        }, FIXED_SUCCESS {
            @Override
            public String toString() {
                return "FIXED_SUCCESS";
            }
        }
    }

    @NotNull
    public abstract Strategy getMyStrategy();
}
