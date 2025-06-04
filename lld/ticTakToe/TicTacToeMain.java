package lld.ticTakToe;


public class TicTacToeMain {
    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        TicTacToeGame game = new TicTacToeGame();

        System.out.print("Enter Player 1 name: ");
        String player1 = scanner.nextLine();
        System.out.print("Enter Player 2 name: ");
        String player2 = scanner.nextLine();

        game.initializePlayers(player1, player2);

        boolean playAgain = true;
        while (playAgain) {
            game.startGame();

            System.out.print("Play again? (y/n): ");
            String response = scanner.nextLine().toLowerCase();
            playAgain = response.equals("y") || response.equals("yes");

            if (playAgain) {
                game.resetGame();
            }
        }

        System.out.println("Thanks for playing!");
        scanner.close();
    }
}