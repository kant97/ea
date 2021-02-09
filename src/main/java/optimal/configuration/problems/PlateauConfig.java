package optimal.configuration.problems;

import com.google.gson.annotations.SerializedName;
import optimal.configuration.ConfigurationVisitor;
import org.jetbrains.annotations.NotNull;
import problem.ProblemsManager;

import javax.naming.ConfigurationException;
import java.util.Objects;

public class PlateauConfig extends ProblemConfig {
    @SerializedName(value = "k")
    private final int k;

    public PlateauConfig(ProblemsManager.ProblemType myType, int size, int k) {
        super(myType, size);
        this.k = k;
    }

    @Override
    public void validate() throws ConfigurationException {
        super.validate();
        if (k <= 1) {
            throw new ConfigurationException("k parameter of Plateau should be at least 2");
        }
    }

    public int getK() {
        return k;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PlateauConfig that = (PlateauConfig) o;
        return k == that.k;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), k);
    }

    @Override
    public @NotNull String accept(@NotNull ConfigurationVisitor visitor) {
        return visitor.visitPlateauConfig(this);
    }
}
