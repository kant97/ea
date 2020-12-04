package pictures;

import org.ejml.simple.SimpleMatrix;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public interface PlottableInMatrixData {
    @NotNull List<HeatmapCellCoordinate> getOrderedMatrixCoordinates(@NotNull SimpleMatrix matrix);
    @NotNull Color getDataInPlotColor();
}
