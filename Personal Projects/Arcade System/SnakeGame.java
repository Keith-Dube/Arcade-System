import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SnakeGame extends JPanel implements KeyListener {
    boolean gameover = false;
    int size = 25; // size of each object in the game i.e snake's head and apple
    ArrayList<Point> snakePositions = new ArrayList<Point>(); // tracks all the positions of the snake's head to tail
    Point snakeDirection = new Point(1, 0); // tracks direction of the snake's head
    Point applePosition = new Point(0, 0); // Specifies the position of the apple
    Point specialApple = new Point(0, 0); // Specifies the position of the special apple
    int score = 0;

    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private static final int DELAY = 120; // Delay in milliseconds for the game loop
    private static final int SPECIAL_TIMER = 60000; // 60 seconds in milliseconds
    private static final int SPECIAL_DURATION = 10000; // 10 seconds in milliseconds
    private Random random = new Random();
    private boolean specialAppleVisible = false;
    private JButton playAgainButton;
    private Timer gameTimer;
    private Timer specialAppleSpawnTimer;
    JButton Quit = new JButton("Quit");
    private sEffects sound = new sEffects();

    public void playSE(int s){
		sound.setFile(s);
		sound.Play();
    }

    public SnakeGame() {
        setBounds(150, 0, WIDTH, HEIGHT);
        setBackground(Color.decode("#69B41E"));
        setFocusable(true);
        addKeyListener(this);
        startGame();

        gameTimer = new Timer(DELAY, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameLoop();
            }
        });
        gameTimer.start();
        setLayout(null);

        // Initialize the quit button
        Quit.setFont(new Font("MV Boli", Font.BOLD, 15));
        Quit.setBounds(30, 18, 70, 30);
        Quit.setBackground(Color.decode("#94B447"));
        Quit.setForeground(Color.WHITE);
        Quit.setBorderPainted(false);
        Quit.setOpaque(false);
        add(Quit);

        // Initialize the play again button
        playAgainButton = new JButton("Play Again");
        playAgainButton.setBounds(WIDTH / 2 - 75, HEIGHT / 2 - 15, 150, 30);
        playAgainButton.setBackground(Color.decode("#69B41E"));
        playAgainButton.setFont(new Font("MV Boli", Font.BOLD, 15));
        playAgainButton.setForeground(Color.WHITE);
        playAgainButton.setBorderPainted(false);
        playAgainButton.addActionListener(e -> handlePlayAgain());

        // Add the button to the panel but initially set it as invisible
        playAgainButton.setVisible(false);
        add(playAgainButton);

        // Create the timer for spawning the special apple every 60 seconds
        specialAppleSpawnTimer = new Timer(SPECIAL_TIMER, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                spawnSpecialApple();
            }
        });
        specialAppleSpawnTimer.setRepeats(true);
        specialAppleSpawnTimer.start();
    }

    public void startGame() {
        gameover = false;
        score = 0;
        snakePositions.clear();
        Point initialPosition = new Point(WIDTH / 2, HEIGHT / 2);
        snakePositions.add(initialPosition);
        randomize(applePosition, 0, (WIDTH / size - 1) * size, 0, (HEIGHT / size - 1) * size);
    }

    public void handlePlayAgain() {
        startGame();
        gameover = false;
        playAgainButton.setVisible(false);
        requestFocus();
    }

    public void spawnSpecialApple() {
        randomize(specialApple, 0, (WIDTH / size - 1) * size, 0, (HEIGHT / size - 1) * size);
        specialAppleVisible = true;

        // Create a timer for making the special apple disappear after 10 seconds
        Timer specialAppleDisappearTimer = new Timer(SPECIAL_DURATION, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSpecialAppleTimer();
            }
        });
        specialAppleDisappearTimer.setRepeats(false);
        specialAppleDisappearTimer.start();
    }

    public void handleSpecialAppleTimer() {
        if (specialAppleVisible) {
            specialAppleVisible = false;
        }
    }

    public void randomize(Point position, int minX, int maxX, int minY, int maxY) {
        position.x = random.nextInt((maxX - minX) / size) * size + minX;
        position.y = random.nextInt((maxY - minY) / size) * size + minY;
    }

    public void endGame() {
        gameover = true;
        playAgainButton.setVisible(true);
        repaint();
    }

    public void gameLoop() {
        if (gameover) {
            return;
        }

        Point newHead = new Point(snakePositions.get(0).x + snakeDirection.x * size, snakePositions.get(0).y + snakeDirection.y * size);

        // Check if the new head position is equal to the apple position
        if (newHead.equals(applePosition)) {
            playSE(5);
            score++;
            snakePositions.add(0, newHead);
            randomize(applePosition, 0, (WIDTH / size - 1) * size, 0, (HEIGHT / size - 1) * size);
        } else if (newHead.equals(specialApple)) {
            playSE(5);
            score += 2;
            snakePositions.add(0, newHead);
            specialAppleVisible = false; // Special apple is consumed
        } else {
            snakePositions.add(0, newHead);
            snakePositions.remove(snakePositions.size() - 1);
        }

        // Check for collision with the wall
        if (newHead.x < 0 || newHead.x >= WIDTH || newHead.y < 0 || newHead.y >= HEIGHT) {
            playSE(2);
            endGame();
        }

        // Check for collision with itself
        for (int i = 1; i < snakePositions.size(); i++) {
            if (newHead.equals(snakePositions.get(i))) {
                playSE(3);
                endGame();
                break;
            }
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Cast Graphics to Graphics2D to use setStroke
        Graphics2D g2d = (Graphics2D) g;

        // Draw score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
        g.drawString("Score: " + score, 220, 25);

        // Draw the apple in red
        g.setColor(Color.RED);
        g.fillOval(applePosition.x, applePosition.y, size, size);

        // Draw the special apple in blue if it's visible
        if (specialAppleVisible) {
            g.setColor(Color.BLUE);
            g.fillOval(specialApple.x, specialApple.y, size, size);
        }

        // Draw the snake's body in Black
        g.setColor(Color.BLACK);
        for (Point position : snakePositions) {
            g.fillOval(position.x, position.y, size, size);
        }

        // Set the thickness for the border lines
        Stroke sk = g2d.getStroke();
        g2d.setStroke(new BasicStroke(6.0f));
        // Draw border lines
        g.setColor(Color.BLACK); // set the color of the border lines
        g.drawRect(0, 0, WIDTH - 1, HEIGHT - 1); // Draw a rectangle around the entire panel

        // Restore the previous stroke
        g2d.setStroke(sk);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (!gameover) {
            if (keyCode == KeyEvent.VK_LEFT && snakeDirection.x != 1) {
                snakeDirection.setLocation(-1, 0);
            } else if (keyCode == KeyEvent.VK_RIGHT && snakeDirection.x != -1) {
                snakeDirection.setLocation(1, 0);
            } else if (keyCode == KeyEvent.VK_UP && snakeDirection.y != 1) {
                snakeDirection.setLocation(0, -1);
            } else if (keyCode == KeyEvent.VK_DOWN && snakeDirection.y != -1) {
                snakeDirection.setLocation(0, 1);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}

