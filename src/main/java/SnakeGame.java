import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

/**
 * SnakeGame class representing the Snake game.
 * <p>
 * This class handles the game logic, rendering, and user input for the Snake game.
 */
public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private final int BOARD_WIDTH;
    private final int BOARD_HEIGHT;
    private final int TILE_SIZE = 25;

    private final Random random;

    // Snake
    private final Color SNAKE_HEAD_COLOR = new Color(40, 150, 40);
    private final Color SNAKE_BODY_COLOR = Color.GREEN;
    private final Tile snakeHead;
    private final ArrayList<Tile> snakeBody;

    // Food
    private final Tile food;

    // Game logic
    private final Timer GAMELOOP;
    private int velocityX;
    private int velocityY;
    private boolean gameOver = false;

    /**
     * Constructor for SnakeGame class.
     * @param boardWidth The width of the game board.
     * @param boardHeight The height of the game board.
     */
    SnakeGame(int boardWidth, int boardHeight) {
        this.BOARD_WIDTH = boardWidth;
        this.BOARD_HEIGHT = boardHeight;

        this.setPreferredSize(new Dimension(this.BOARD_WIDTH, this.BOARD_HEIGHT));
        this.setBackground(Color.BLACK);
        this.addKeyListener(this);
        this.setFocusable(true);

        random = new Random();

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<>();

        food = new Tile(10, 10);
        placeFood();

        velocityX = 0;
        velocityY = 1;

        GAMELOOP = new Timer(100, this);
        GAMELOOP.start();
    }

    /**
     * Paints the game components.
     * @param g The Graphics object used for painting.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    /**
     * Draws the game components.
     * @param g The Graphics object used for drawing.
     */
    public void draw(Graphics g) {
        // Draw the grid
        /* for (int i = 0; i < BOARD_WIDTH / TILE_SIZE; i++) {
            g.drawLine(i * TILE_SIZE, 0, i * TILE_SIZE, BOARD_HEIGHT);
            g.drawLine(0, i * TILE_SIZE, BOARD_WIDTH, i * TILE_SIZE);
        } */

        // Draw the food
        g.setColor(Color.RED);
        // g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        g.fill3DRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE, true);

        // Draw the snake body
        g.setColor(SNAKE_BODY_COLOR);
        for (Tile snakePart : snakeBody) {
            // g.fillRect(snakePart.x * TILE_SIZE, snakePart.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            g.fill3DRect(snakePart.x * TILE_SIZE, snakePart.y * TILE_SIZE, TILE_SIZE, TILE_SIZE, true);
        }

        // Draw the snake head
        g.setColor(SNAKE_HEAD_COLOR);
        // g.fillRect(snakehead.x * TILE_SIZE, snakehead.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        g.fill3DRect(snakeHead.x * TILE_SIZE, snakeHead.y * TILE_SIZE, TILE_SIZE, TILE_SIZE, true);

        // Draw score
        int score = snakeBody.size();
        g.setFont(new Font("Arial", Font.BOLD, 20));
        if (gameOver) {
            g.setColor(Color.RED);
            g.drawString("Game Over: " + score, TILE_SIZE - 20, TILE_SIZE);
        } else {
            g.setColor(Color.WHITE);
            g.drawString("Score: " + score, TILE_SIZE - 20, TILE_SIZE);
        }
    }

    /**
     * Places the food at a random location on the board.
     */
    public void placeFood() {
        food.x = random.nextInt(BOARD_WIDTH / TILE_SIZE);
        food.y = random.nextInt(BOARD_HEIGHT / TILE_SIZE);
    }

    /**
     * Checks if two tiles are colliding.
     * @param a The first tile.
     * @param b The second tile.
     * @return True if the tiles are colliding, false otherwise.
     */
    public boolean collision(Tile a, Tile b) {
        return a.x == b.x && a.y == b.y;
    }

    /**
     * Moves the snake and checks for collisions.
     */
    public void move() {
        // Eat food
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        // Move the snake body
        int snakeBodySize = snakeBody.size() - 1;
        for (int i = snakeBodySize; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else {
                Tile previousPart = snakeBody.get(i - 1);
                snakePart.x = previousPart.x;
                snakePart.y = previousPart.y;
            }
        }

        // Snake Head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        // Game over conditions
        // Check for snake head collision with himself
        for (Tile snakePart : snakeBody) {
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
                break;
            }
        }
        // Check for snake head collision with walls
        if (snakeHead.x * TILE_SIZE < 0 || snakeHead.x * TILE_SIZE > BOARD_WIDTH ||
           snakeHead.y * TILE_SIZE < 0 || snakeHead.y * TILE_SIZE > BOARD_HEIGHT) {
            gameOver = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        this.repaint();
        if (gameOver) {
            GAMELOOP.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> {
                if (velocityY != 1) {
                    velocityX = 0;
                    velocityY = -1;
                }
            }
            case KeyEvent.VK_DOWN -> {
                if (velocityY != -1) {
                    velocityX = 0;
                    velocityY = 1;
                }
            }
            case KeyEvent.VK_LEFT -> {
                if (velocityX != 1) {
                    velocityX = -1;
                    velocityY = 0;
                }
            }
            case KeyEvent.VK_RIGHT -> {
                if (velocityX != -1) {
                    velocityX = 1;
                    velocityY = 0;
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}