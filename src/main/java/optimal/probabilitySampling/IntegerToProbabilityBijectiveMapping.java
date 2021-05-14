package optimal.probabilitySampling;

public interface IntegerToProbabilityBijectiveMapping {
    int probabilityToInteger(double probability);

    double integerToProbability(int n);
}
