package ticTakToe;

abstract class Player {
    protected String name;
    protected CellState symbol;

    public Player(String name, CellState symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public String getName() { return name; }
    public CellState getSymbol() { return symbol; }

    public abstract Move makeMove(Board board);
}