package pictures;

public class HeatmapCellCoordinate {
    private final int row;
    private final int col;

    public HeatmapCellCoordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
