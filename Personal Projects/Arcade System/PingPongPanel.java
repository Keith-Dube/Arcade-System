import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PingPongPanel extends JPanel implements KeyListener {

        // Left paddle
        final Paddle paddle1;
        // Right paddle
        final Paddle paddle2;

        final Lines Horizontal_Line;
        final Ball ball;
        private int Velocity = 3;
        private Random random = new Random();

        private JLabel Score1 = new JLabel("0  "); // Display score for player 1
        private JLabel Score2 = new JLabel("0"); // Display score for player 2

        private int p1Score = 0; // Score for player 1
        private int p2Score = 0; // Score for player 2

        private int xVelocity = Velocity;  // Horizontal velocity of ball
        private int yVelocity = Velocity;  // Vertical velocity of ball
        private int speed = 10; // speed of AI paddle
        private int rounds;
        
        private boolean running = true;

        private JLabel WinnerMessage = new JLabel();
        JButton Quit = new JButton("Quit");

        private sEffects sound = new sEffects();
		
        // Play sound effect
		public void playSE(int i){
			sound.setFile(i);
			sound.Play();
		}

        PingPongPanel() {
            paddle1 = new Paddle(0, 200, 13, 100, Color.decode("#24788F"));
            paddle2 = new Paddle(775, 200, 25, 100, Color.decode("#F18A85"));
            Horizontal_Line = new Lines(0, 250, 800, 1, Color.white);
            ball = new Ball(395, 245, 10, 10, Color.white);
            this.setLayout(null);

            Quit.setFont(new Font("MV Boli", Font.PLAIN,15));
            Quit.setBounds(30, 18, 70, 30);
            Quit.setBackground(Color.BLACK);
            Quit.setForeground(Color.WHITE);
            Quit.setBorderPainted(false);
            Quit.setOpaque(false);
            add(Quit);

            WinnerMessage.setVisible(false);
            WinnerMessage.setForeground(Color.decode("#C8DBCD"));
            WinnerMessage.setFont(new Font("Calibri", Font.BOLD, 50));
            WinnerMessage.setBounds(250, 20, 400, 50);
            add(WinnerMessage);

            setBackground(Color.black);
            setBounds(0, 25, 800, 500);
            Score1.setBounds(200,10,50,50);
            Score1.setForeground(Color.decode("#24788F"));
            Score1.setFont(new Font("Comic Sans MS", Font.BOLD, 30));

            Score2.setBounds(getWidth()-225, 10, 50, 50);
            Score2.setForeground(Color.decode("#F18A85"));
            Score2.setFont(new Font("Comic Sans MS", Font.BOLD, 30));

            add(Score1);
            add(Score2);

            setFocusable(true);
            addKeyListener(this);
        }

        // Returns a value for whether the game is running
        public boolean isRunning() {
            return this.running;
        }

        // Setter to change the running variable
        public void setRunning(boolean running) {
            this.running = running;
        }

        // Change the number of rounds
        public void setRounds(int rounds) {
            this.rounds = rounds;
        }

        // Moves the ball by changing coordinates
        public void moveBall() {
            ball.x += xVelocity;
            ball.y += yVelocity;
            repaint();
        }

        // Method for single player
        public void sPlayerMove() {
            paddle1.movePaddle();
            moveAI(paddle2);
            repaint();
        }

        // Method for multiplayer
        public void mPlayerMove() {
            paddle1.movePaddle();
            paddle2.movePaddle();
            repaint();
        }

        // Method for AI paddle to move
        public void moveAI(Paddle paddle) {
            if (xVelocity > 0) {
                if (ball.x >= getWidth()/2) {
                    if (ball.y < paddle.y) {
                        paddle.yDirection(-speed);
                        paddle.movePaddle();
                    } else if (ball.y > (paddle.y + paddle.height)) {
                        paddle.yDirection(speed);
                        paddle.movePaddle();
                    } else {
                        paddle.yDirection(0);
                        paddle.movePaddle();
                    }
                } else {
                    paddle.yDirection(0);
                    paddle.movePaddle();
                }
            } else {
                paddle.yDirection(0);
                paddle.movePaddle();
            }
            repaint();
        }

        public void changeSpeed() {
            if (p1Score - p2Score == 2) {
                speed += 2;
            } else if (p2Score - p1Score == 2) {
                speed -= 2;
            }
        }

        public void collision() {
            // Check if paddle goes off screen
            if (paddle1.y <= 0)
                paddle1.y = 0;
            if (paddle1.y + paddle1.height >= getHeight())
                paddle1.y = getHeight() - paddle1.height;
            if (paddle2.y <= 0)
                paddle2.y = 0;
            if (paddle2.y + paddle2.height >= getHeight())
                paddle2.y = getHeight() - paddle2.height;

            // Resets the ball and paddle positions when ball goes out of bounds
            if (ball.x + 10 > getWidth() || ball.x < 0) {
                scoreCount();
                Velocity *= -1;
                xVelocity = Velocity;
                yVelocity = Velocity;
                newObjects();
            }

            // Reverse direction if the circle reaches the panel's boundaries
            if (ball.y + 50 >= getHeight() || ball.y < 0) {
                yVelocity *= -1;
                playSE(2); //sound effect
            }

            // Reverse direction when ball collides with paddles
            if (ball.intersects(paddle1) || ball.intersects(paddle2)) {
                xVelocity *= -1;
                if (xVelocity > 0) {
                    if (xVelocity <= 11) {
                        xVelocity++;
                    }
                } else {
                    if (xVelocity >= -11) { 
                        xVelocity--;
                    }
                }
                playSE(1); //sound effect
            }
    
            repaint();
        }

        // Resets positions for objects
        public void newObjects() {
            paddle1.y = getHeight()/2 - paddle1.height/2;
            paddle2.y = getHeight()/2 - paddle2.height/2;
            ball.x = getWidth()/2 - ball.width/2;
            ball.y = random.nextInt(getHeight()/2 - 100, getHeight()/2 + 100);
            Score1.setText("" + p1Score + "  ");
            Score2.setText("" + p2Score);
            repaint();
        }

        // Called whenever the game is started
        public void newGame() {
            p1Score = 0;
            p2Score = 0;
            WinnerMessage.setVisible(false);
            newObjects();
        }

        // called when a player wins
        public void winner() {
            if (p1Score == rounds) {
                WinnerMessage.setVisible(true);
                WinnerMessage.setText("Player 1 wins!");
                running = false;
            } else if (p2Score == rounds) {
                WinnerMessage.setVisible(true);
                WinnerMessage.setText("Player 2 wins!");
                running = false;
            }
        }

        // Checks if someone has won yet
        public boolean isWinner() {
            if (p1Score == rounds || p2Score == rounds) {
                return true;
            } else {
                return false;
            }
        }

        // Increments the scores
        public void scoreCount() {
            if (ball.x <= 0) {
                p2Score++;
                Score2.setText("" + p2Score);
                System.out.println(p2Score);
                playSE(3); //sound effect
            }
            if (ball.x + 10 >= 800) {
                p1Score++;
                Score1.setText("" + p1Score + "  ");
                System.out.println(p2Score);
                playSE(3); //sound effect
            }
        }

        // Paint the components onto the game screen
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            g.setColor(Horizontal_Line.color);
            g.drawLine(Horizontal_Line.x, Horizontal_Line.y, Horizontal_Line.x + Horizontal_Line.width, Horizontal_Line.y + Horizontal_Line.height);

            g2d.setColor(paddle1.color);
            g2d.fillRect(paddle1.x, paddle1.y, paddle1.width, paddle1.height);

            g2d.setColor(paddle2.color);
            g2d.fillRect(paddle2.x, paddle2.y, paddle2.width, paddle2.height);

            g2d.setColor(ball.color);
            g2d.fillOval(ball.x, ball.y, ball.width, ball.height);

        }   


        @Override
        public void keyTyped(KeyEvent e) {
            // TODO Auto-generated method stub
            // throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
        }

        @Override
        public void keyPressed(KeyEvent e) {
            // TODO Auto-generated method stub
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    // Move the left paddle up when the up arrow is pressed
                    paddle2.yDirection(-10);
                    paddle2.movePaddle(); // Move up
                    break;
                case KeyEvent.VK_DOWN:
                    // Move the left paddle down when the down arrow is pressed
                    paddle2.yDirection(10);
                    paddle2.movePaddle(); // Move down
                    break;
                case KeyEvent.VK_W:
                    // Move the right paddle up when the w button is pressed
                    paddle1.yDirection(-10);
                    paddle1.movePaddle(); // Move up
                    break;
                case KeyEvent.VK_S:
                    // Move the right paddle down when the s button is pressed
                    paddle1.yDirection(10);
                    paddle1.movePaddle(); // Move down
                    break;
                case KeyEvent.VK_ESCAPE:
                    // Escape button to stop the game loop
                    running = true;
                    setVisible(false);
                    // setVisible(false);
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            // TODO Auto-generated method stub
            System.out.println(e.getKeyCode());
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    // Right paddle stops moving when the up arrow is released
                    paddle2.yDirection(0);
                    paddle2.movePaddle();
                    break;
                case KeyEvent.VK_DOWN:
                    // Left paddle stops moving when the down arrow is released
                    paddle2.yDirection(0);
                    paddle2.movePaddle();
                    break;
                case KeyEvent.VK_W:
                    // Right paddle stops moving when the w button is released
                    paddle1.yDirection(0);
                    paddle1.movePaddle();
                    break;
                case KeyEvent.VK_S:
                    // Left paddle stops moving when the s button is released
                    paddle1.yDirection(0);
                    paddle1.movePaddle();
                    break;
            }
        }

        public class Lines extends Rectangle {

            Color color;
    
            Lines(int x, int y, int width, int height, Color color) {
                super(x, y, width, height);
                this.color = color;
            }
            
        }

        public class Ball extends Rectangle {

            Color color;
    
            Ball(int x, int y, int width, int height, Color color) {
                super(x, y, width, height);
                this.color = color;
            }
            
        }

    }