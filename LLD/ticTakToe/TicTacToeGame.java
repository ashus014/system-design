package LLD.ticTakToe;

class TicTacToeGame {
    private Board board;
    private Player[] players;
    private int currentPlayerIndex;
    private GameStatus gameStatus;

    public TicTacToeGame() {
        board = new Board();
        players = new Player[2];
        currentPlayerIndex = 0;
        gameStatus = GameStatus.IN_PROGRESS;
    }

    public void initializePlayers(String player1Name, String player2Name) {
        players[0] = new HumanPlayer(player1Name, CellState.X);
        players[1] = new HumanPlayer(player2Name, CellState.O);
    }

    public void startGame() {
        System.out.println("Welcome to Tic Tac Toe!");
        System.out.println(players[0].getName() + " (X) vs " + players[1].getName() + " (O)");

        while (gameStatus == GameStatus.IN_PROGRESS) {
            board.display();
            playTurn();
            gameStatus = board.checkGameStatus();
        }

        board.display();
        displayGameResult();
    }

    private void playTurn() {
        Player currentPlayer = players[currentPlayerIndex];
        boolean validMove = false;

        while (!validMove) {
            try {
                Move move = currentPlayer.makeMove(board);
                validMove = board.makeMove(move);

                if (!validMove) {
                    System.out.println("Invalid move! Try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter numbers only.");
                // Clear the scanner buffer
                new java.util.Scanner(System.in).nextLine();
            }
        }

        switchPlayer();
    }

    private void switchPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % 2;
    }

    private void displayGameResult() {
        switch (gameStatus) {
            case PLAYER_X_WINS:
                System.out.println("🎉 " + players[0].getName() + " (X) wins!");
                break;
            case PLAYER_O_WINS:
                System.out.println("🎉 " + players[1].getName() + " (O) wins!");
                break;
            case DRAW:
                System.out.println("🤝 It's a draw!");
                break;
        }
    }

    public void resetGame() {
        board.reset();
        currentPlayerIndex = 0;
        gameStatus = GameStatus.IN_PROGRESS;
    }
}