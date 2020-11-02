package optimal.configuration.algorithms;

import com.google.gson.annotations.SerializedName;
import optimal.configuration.ConfigurationVisitor;
import optimal.configuration.ValidatableConfiguration;
import optimal.configuration.VisitableConfiguration;
import optimal.oneStepAlgorithms.OneStepAlgorithmsManager;
import org.jetbrains.annotations.NotNull;

import javax.naming.ConfigurationException;
import java.util.Objects;

public class AlgorithmConfig implements ValidatableConfiguration, VisitableConfiguration {
    @SerializedName(value = "type", alternate = "myType")
    private final OneStepAlgorithmsManager.AlgorithmType myType;
    @SerializedName(value = "lambda", alternate = "myLambda")
    private final int myLambda;

    public AlgorithmConfig(OneStepAlgorithmsManager.AlgorithmType type, int lambda) {
        this.myType = type;
        this.myLambda = lambda;
    }

    public OneStepAlgorithmsManager.AlgorithmType getAlgorithmType() {
        return myType;
    }

    public int getLambda() {
        return myLambda;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlgorithmConfig that = (AlgorithmConfig) o;
        return myLambda == that.myLambda &&
                myType == that.myType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(myType, myLambda);
    }

    @Override
    public void validate() throws ConfigurationException {
        if (myType == null) {
            throw new ConfigurationException("Algorithm type should be configured");
        }
        if (myLambda <= 0) {
            throw new ConfigurationException("Lambda should be positive, but it is " + myLambda);
        }
    }

    @Override
    public String toString() {
        return "AlgorithmConfig{" +
                "myType=" + myType +
                ", myLambda=" + myLambda +
                '}';
    }

    @Override
    public @NotNull String accept(@NotNull ConfigurationVisitor visitor) {
        return visitor.visitAlgorithmConfig(this);
    }
}
