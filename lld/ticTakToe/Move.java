package lld.ticTakToe;

class Move {
    private final int row;
    private final int col;
    private final CellState player;

    public Move(int row, int col, CellState player) {
        this.row = row;
        this.col = col;
        this.player = player;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
    public CellState getPlayer() { return player; }
}