package optimal.execution.events;

public enum EventType {
    OPTIMAL_RESULT_READY {
        @Override
        public String toString() {
            return "OptimalResult";
        }
    }, INTERMEDIATE_RESULT_READY {
        @Override
        public String toString() {
            return "IntermediateResult";
        }
    },
    PROGRESS_UPDATE {
        @Override
        public String toString() {
            return "Progress";
        }
    }
}
