package LLD.ticTakToe;

class Cell {
    private CellState state;

    public Cell() {
        this.state = CellState.EMPTY;
    }

    public CellState getState() {
        return state;
    }

    public void setState(CellState state) {
        this.state = state;
    }

    public boolean isEmpty() {
        return state == CellState.EMPTY;
    }

    @Override
    public String toString() {
        switch (state) {
            case X: return "X";
            case O: return "O";
            default: return " ";
        }
    }
}
