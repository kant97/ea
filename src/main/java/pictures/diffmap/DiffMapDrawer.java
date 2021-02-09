package pictures.diffmap;

import org.jetbrains.annotations.NotNull;
import pictures.MatrixDrawer;
import pictures.MatrixWrapper;
import pictures.coloring.AbstractColouring;

import java.awt.*;

public class DiffMapDrawer extends MatrixDrawer {

    public DiffMapDrawer(int xOffsetLeft, int yOffsetUp, int plotWidth, int plotHeight, @NotNull Graphics graphics,
                         AbstractColouring coloring, int numCols, int numRows, @NotNull MatrixWrapper matrix) {
        super(xOffsetLeft, yOffsetUp, plotWidth, plotHeight, graphics, coloring, numCols, numRows);
    }

    public void addLine() {

    }
}
