package optimal.probabilitySampling;

public enum ProbabilitySamplingStrategy {
    ITERATIVE {
        @Override
        public String toString() {
            return "IterativeStrategy";
        }
    }, TERNARY_SEARCH {
        @Override
        public String toString() {
            return "TernarySearch";
        }
    },
    EXPONENTIAL_GRID {
        @Override
        public String toString() {
            return "ExponentialGrid";
        }
    }
}
