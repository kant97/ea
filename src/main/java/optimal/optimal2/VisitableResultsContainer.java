package optimal.optimal2;

import org.jetbrains.annotations.NotNull;

public interface VisitableResultsContainer {
    @NotNull String accept(@NotNull ExtraDataCollectorVisitor visitor);
}
