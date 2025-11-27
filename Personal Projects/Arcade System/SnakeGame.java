import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
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
    private boolean gameover = false;
    private int size = 25;
    private ArrayList<Point> snakePositions = new ArrayList<Point>();
    private Point snakeDirection = new Point(1, 0);
    private Point applePosition = new Point(0, 0);
    private Point specialApple = new Point(0, 0);
    private int score = 0;

    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private static final int DELAY = 120;
    private static final int SPECIAL_TIMER = 60000;
    private static final int SPECIAL_DURATION = 10000;
    private Random random = new Random();
    private boolean specialAppleVisible = false;
    private JButton playAgainButton;
    private Timer gameTimer;
    private Timer specialAppleSpawnTimer;
    public JButton quitButton;
    
    // Color scheme
    private final Color BACKGROUND_COLOR = Color.decode("#1a1a2e");
    private final Color GRID_COLOR = Color.decode("#16213e");
    private final Color SNAKE_HEAD_COLOR = Color.decode("#00b894");
    private final Color SNAKE_BODY_COLOR = Color.decode("#00a085");
    private final Color APPLE_COLOR = Color.decode("#e74c3c");
    private final Color SPECIAL_APPLE_COLOR = Color.decode("#9b59b6");
    private final Color BORDER_COLOR = Color.decode("#0f3460");
    private final Color TEXT_COLOR = Color.decode("#ecf0f1");
    private final Color BUTTON_COLOR = Color.decode("#3498db");
    private final Color BUTTON_HOVER_COLOR = Color.decode("#2980b9");

    // Sound effects class placeholder
    private class sEffects {
        public void setFile(int s) {}
        public void Play() {}
    }
    private sEffects sound = new sEffects();

    public void playSE(int s) {
        sound.setFile(s);
        sound.Play();
    }

    public SnakeGame() {
        setBounds(150, 0, WIDTH, HEIGHT);
        setBackground(BACKGROUND_COLOR);
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

        // Initialize the quit button with better styling
        quitButton = createStyledButton("Quit", 30, 18, 80, 35);
        quitButton.addActionListener(e -> System.exit(0));
        add(quitButton);

        // Initialize the play again button
        playAgainButton = createStyledButton("Play Again", WIDTH / 2 - 90, HEIGHT / 2 + 20, 180, 40);
        playAgainButton.addActionListener(e -> handlePlayAgain());
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

    private JButton createStyledButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isRollover()) {
                    g2.setColor(BUTTON_HOVER_COLOR);
                } else {
                    g2.setColor(BUTTON_COLOR);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                g2.setColor(TEXT_COLOR);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(getText(), textX, textY);
            }
        };
        
        button.setBounds(x, y, width, height);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        return button;
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

        Point newHead = new Point(snakePositions.get(0).x + snakeDirection.x * size, 
                                 snakePositions.get(0).y + snakeDirection.y * size);

        if (newHead.equals(applePosition)) {
            playSE(5);
            score++;
            snakePositions.add(0, newHead);
            randomize(applePosition, 0, (WIDTH / size - 1) * size, 0, (HEIGHT / size - 1) * size);
        } else if (specialAppleVisible && newHead.equals(specialApple)) {
            playSE(5);
            score += 2;
            snakePositions.add(0, newHead);
            specialAppleVisible = false;
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
        Graphics2D g2d = (Graphics2D) g;
        
        // Enable anti-aliasing for smoother graphics
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw grid background
        drawGrid(g2d);
        
        // Draw score with better styling
        drawScore(g2d);
        
        // Draw the apple with gradient
        drawApple(g2d);
        
        // Draw the special apple with pulsing effect
        if (specialAppleVisible) {
            drawSpecialApple(g2d);
        }
        
        // Draw the snake with gradient and better styling
        drawSnake(g2d);
        
        // Draw border with gradient
        drawBorder(g2d);
        
        // Draw game over screen
        if (gameover) {
            drawGameOver(g2d);
        }
    }

    private void drawGrid(Graphics2D g2d) {
        g2d.setColor(GRID_COLOR);
        for (int x = 0; x < WIDTH; x += size) {
            for (int y = 0; y < HEIGHT; y += size) {
                g2d.drawRect(x, y, size, size);
            }
        }
    }

    private void drawScore(Graphics2D g2d) {
        g2d.setColor(TEXT_COLOR);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 18));
        g2d.drawString("Score: " + score, WIDTH / 2 - 40, 30);
        
        // Draw score background
        g2d.setColor(new Color(255, 255, 255, 30));
        g2d.fillRoundRect(WIDTH / 2 - 50, 10, 100, 30, 15, 15);
    }

    private void drawApple(Graphics2D g2d) {
        GradientPaint gradient = new GradientPaint(
            applePosition.x, applePosition.y, APPLE_COLOR,
            applePosition.x + size, applePosition.y + size, APPLE_COLOR.darker()
        );
        g2d.setPaint(gradient);
        g2d.fillOval(applePosition.x + 2, applePosition.y + 2, size - 4, size - 4);
        
        // Add highlight
        g2d.setColor(new Color(255, 255, 255, 100));
        g2d.fillOval(applePosition.x + 5, applePosition.y + 5, size / 3, size / 3);
    }

    private void drawSpecialApple(Graphics2D g2d) {
        // Create a pulsing effect using system time
        long time = System.currentTimeMillis();
        int pulse = (int) ((Math.sin(time * 0.01) + 1) * 20);
        Color pulseColor = new Color(
            SPECIAL_APPLE_COLOR.getRed(),
            SPECIAL_APPLE_COLOR.getGreen(),
            SPECIAL_APPLE_COLOR.getBlue(),
            150 + pulse
        );
        
        GradientPaint gradient = new GradientPaint(
            specialApple.x, specialApple.y, pulseColor,
            specialApple.x + size, specialApple.y + size, SPECIAL_APPLE_COLOR.darker()
        );
        g2d.setPaint(gradient);
        g2d.fillOval(specialApple.x + 2, specialApple.y + 2, size - 4, size - 4);
        
        // Add star effect
        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString("★", specialApple.x + size/2 - 4, specialApple.y + size/2 + 4);
    }

    private void drawSnake(Graphics2D g2d) {
        for (int i = 0; i < snakePositions.size(); i++) {
            Point position = snakePositions.get(i);
            
            // Use different colors for head and body
            if (i == 0) {
                // Head with gradient
                GradientPaint headGradient = new GradientPaint(
                    position.x, position.y, SNAKE_HEAD_COLOR,
                    position.x + size, position.y + size, SNAKE_HEAD_COLOR.darker()
                );
                g2d.setPaint(headGradient);
            } else {
                // Body with gradient
                float ratio = (float) i / snakePositions.size();
                Color bodyColor = interpolateColor(SNAKE_BODY_COLOR, SNAKE_BODY_COLOR.darker(), ratio);
                GradientPaint bodyGradient = new GradientPaint(
                    position.x, position.y, bodyColor,
                    position.x + size, position.y + size, bodyColor.darker()
                );
                g2d.setPaint(bodyGradient);
            }
            
            g2d.fillRoundRect(position.x + 2, position.y + 2, size - 4, size - 4, 8, 8);
            
            // Add eyes to the head
            if (i == 0) {
                g2d.setColor(Color.WHITE);
                int eyeSize = size / 5;
                if (snakeDirection.x == 1) { // Right
                    g2d.fillOval(position.x + size - eyeSize - 4, position.y + 6, eyeSize, eyeSize);
                    g2d.fillOval(position.x + size - eyeSize - 4, position.y + size - 12, eyeSize, eyeSize);
                } else if (snakeDirection.x == -1) { // Left
                    g2d.fillOval(position.x + 4, position.y + 6, eyeSize, eyeSize);
                    g2d.fillOval(position.x + 4, position.y + size - 12, eyeSize, eyeSize);
                } else if (snakeDirection.y == 1) { // Down
                    g2d.fillOval(position.x + 6, position.y + size - eyeSize - 4, eyeSize, eyeSize);
                    g2d.fillOval(position.x + size - 12, position.y + size - eyeSize - 4, eyeSize, eyeSize);
                } else if (snakeDirection.y == -1) { // Up
                    g2d.fillOval(position.x + 6, position.y + 4, eyeSize, eyeSize);
                    g2d.fillOval(position.x + size - 12, position.y + 4, eyeSize, eyeSize);
                }
            }
        }
    }

    private void drawBorder(Graphics2D g2d) {
        Stroke oldStroke = g2d.getStroke();
        g2d.setStroke(new BasicStroke(4.0f));
        
        GradientPaint borderGradient = new GradientPaint(
            0, 0, BORDER_COLOR,
            WIDTH, HEIGHT, BORDER_COLOR.brighter()
        );
        g2d.setPaint(borderGradient);
        g2d.drawRoundRect(2, 2, WIDTH - 5, HEIGHT - 5, 20, 20);
        
        g2d.setStroke(oldStroke);
    }

    private void drawGameOver(Graphics2D g2d) {
        // Semi-transparent overlay
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, WIDTH, HEIGHT);
        
        // Game over text
        g2d.setColor(TEXT_COLOR);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 36));
        String gameOverText = "GAME OVER";
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(gameOverText);
        g2d.drawString(gameOverText, (WIDTH - textWidth) / 2, HEIGHT / 2 - 30);
        
        // Final score
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 24));
        String scoreText = "Final Score: " + score;
        textWidth = g2d.getFontMetrics().stringWidth(scoreText);
        g2d.drawString(scoreText, (WIDTH - textWidth) / 2, HEIGHT / 2 + 10);
    }

    private Color interpolateColor(Color color1, Color color2, float ratio) {
        int red = (int) (color1.getRed() + (color2.getRed() - color1.getRed()) * ratio);
        int green = (int) (color1.getGreen() + (color2.getGreen() - color1.getGreen()) * ratio);
        int blue = (int) (color1.getBlue() + (color2.getBlue() - color1.getBlue()) * ratio);
        return new Color(red, green, blue);
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