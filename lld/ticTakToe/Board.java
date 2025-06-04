package lld.ticTakToe;

class Board {
    private static final int SIZE = 3;
    private Cell[][] grid;

    public Board() {
        grid = new Cell[SIZE][SIZE];
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                grid[i][j] = new Cell();
            }
        }
    }

    public boolean isValidMove(int row, int col) {
        return row >= 0 && row < SIZE &&
                col >= 0 && col < SIZE &&
                grid[row][col].isEmpty();
    }

    public boolean makeMove(Move move) {
        int row = move.getRow();
        int col = move.getCol();

        if (!isValidMove(row, col)) {
            return false;
        }

        grid[row][col].setState(move.getPlayer());
        return true;
    }

    public boolean isFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j].isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    public GameStatus checkGameStatus() {
        // Check rows
        for (int i = 0; i < SIZE; i++) {
            if (checkLine(grid[i][0], grid[i][1], grid[i][2])) {
                return getWinnerStatus(grid[i][0].getState());
            }
        }

        // Check columns
        for (int j = 0; j < SIZE; j++) {
            if (checkLine(grid[0][j], grid[1][j], grid[2][j])) {
                return getWinnerStatus(grid[0][j].getState());
            }
        }

        // Check diagonals
        if (checkLine(grid[0][0], grid[1][1], grid[2][2])) {
            return getWinnerStatus(grid[0][0].getState());
        }

        if (checkLine(grid[0][2], grid[1][1], grid[2][0])) {
            return getWinnerStatus(grid[0][2].getState());
        }

        // Check for draw
        if (isFull()) {
            return GameStatus.DRAW;
        }

        return GameStatus.IN_PROGRESS;
    }

    private boolean checkLine(Cell c1, Cell c2, Cell c3) {
        return !c1.isEmpty() &&
                c1.getState() == c2.getState() &&
                c2.getState() == c3.getState();
    }

    private GameStatus getWinnerStatus(CellState state) {
        return state == CellState.X ? GameStatus.PLAYER_X_WINS : GameStatus.PLAYER_O_WINS;
    }

    public void display() {
        System.out.println("\n  0   1   2");
        for (int i = 0; i < SIZE; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(grid[i][j]);
                if (j < SIZE - 1) System.out.print(" | ");
            }
            System.out.println();
            if (i < SIZE - 1) {
                System.out.println("  ---------");
            }
        }
        System.out.println();
    }

    public void reset() {
        initializeBoard();
    }
}