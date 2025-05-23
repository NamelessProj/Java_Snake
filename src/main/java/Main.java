import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        final int BOARD_WIDTH = 600;
        final int BOARD_HEIGHT = BOARD_WIDTH;

        JFrame frame = new JFrame("Snake");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(BOARD_WIDTH, BOARD_HEIGHT);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        SnakeGame game = new SnakeGame(BOARD_WIDTH, BOARD_HEIGHT);
        frame.add(game);
        frame.pack();
        game.requestFocus();

        frame.setVisible(true);
    }
}