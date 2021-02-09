package pictures;

public class MatrixCellCoordinate {
    private final int row;
    private final int col;

    public MatrixCellCoordinate(int row, int col) {
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
