package optimal.configuration.problems;

import com.google.gson.annotations.SerializedName;
import optimal.configuration.ConfigurationVisitor;
import org.jetbrains.annotations.NotNull;
import problem.ProblemsManager;

import javax.naming.ConfigurationException;
import java.util.Objects;

public class RuggednessConfig extends ProblemConfig {
    @SerializedName(value = "r")
    private final int r;

    public RuggednessConfig(ProblemsManager.ProblemType myType, int size, int r) {
        super(myType, size, false);
        this.r = r;
    }

    public int getR() {
        return r;
    }

    @Override
    public void validate() throws ConfigurationException {
        super.validate();
        if (r <= 1) {
            throw new ConfigurationException("r parameter of Ruggedness should be at least 2");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RuggednessConfig that = (RuggednessConfig) o;
        return r == that.r;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), r);
    }

    @Override
    public String toString() {
        return "RuggednessConfig{" +
                "r=" + r +
                "} " + super.toString();
    }

    @Override
    public @NotNull String accept(@NotNull ConfigurationVisitor visitor) {
        return visitor.visitRuggednessConfig(this);
    }
}
