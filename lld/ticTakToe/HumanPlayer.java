package lld.ticTakToe;

class HumanPlayer extends Player {
    private java.util.Scanner scanner;

    public HumanPlayer(String name, CellState symbol) {
        super(name, symbol);
        this.scanner = new java.util.Scanner(System.in);
    }

    @Override
    public Move makeMove(Board board) {
        System.out.println(name + "'s turn (" + symbol + ")");
        System.out.print("Enter row (0-2): ");
        int row = scanner.nextInt();
        System.out.print("Enter column (0-2): ");
        int col = scanner.nextInt();

        return new Move(row, col, symbol);
    }
}