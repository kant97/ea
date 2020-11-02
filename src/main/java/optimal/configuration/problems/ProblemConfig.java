package optimal.configuration.problems;

import com.google.gson.annotations.SerializedName;
import optimal.configuration.ValidatableConfiguration;
import problem.ProblemsManager;

import javax.naming.ConfigurationException;
import java.util.Objects;

public class ProblemConfig implements ValidatableConfiguration {
    @SerializedName(value = "type", alternate = "myType")
    private final ProblemsManager.ProblemType myType;

    @SerializedName(value = "size", alternate = "mySize")
    private final int mySize;

    public ProblemConfig(ProblemsManager.ProblemType myType, int size) {
        this.myType = myType;
        this.mySize = size;
    }

    public ProblemsManager.ProblemType getProblemType() {
        return myType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProblemConfig that = (ProblemConfig) o;
        return mySize == that.mySize &&
                myType == that.myType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(myType, mySize);
    }

    @Override
    public void validate() throws ConfigurationException {
        if (myType == null) {
            throw new ConfigurationException("Problems type should be configured");
        }
    }

    public int getSize() {
        return mySize;
    }

    @Override
    public String toString() {
        return "ProblemConfig{" +
                "myType=" + myType +
                ", mySize=" + mySize +
                '}';
    }
}
