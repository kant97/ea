package optimal.configuration;

import org.jetbrains.annotations.NotNull;

public interface VisitableConfiguration {
    @NotNull String accept(@NotNull ConfigurationVisitor visitor);
}
