import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class GameFrame extends JFrame implements Runnable, ActionListener {
    
    private PingPongPanel panel = new PingPongPanel();
    private PPSelection selection = new PPSelection();
    private MainMenu menu = new MainMenu();
    private SnakeGame snake = new SnakeGame();
    private TicTacToe tictactoe = new TicTacToe();
    private Thread gameThread; // Add a Thread for the game loop
    private boolean SinglePlayer;

    GameFrame() {
        setTitle("404 Arcade");
        setSize(800, 525);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.white);
        setLayout(null);
        setResizable(false);

        // Add the main menu
        menu.buttons[0].addActionListener(this);
        menu.buttons[1].addActionListener(this);
        menu.buttons[2].addActionListener(this);
        add(menu);

        // Selecting a game mode for Ping Pong
        selection.SButton.addActionListener(this);
        selection.MButton.addActionListener(this);
        selection.Back.addActionListener(this);
        selection.setVisible(false);
        add(selection);
        
        // Add the TicTacToe game
        tictactoe.setVisible(false);
        tictactoe.Quit.addActionListener(this);
        add(tictactoe);

        // Add the Ping Pong game
        panel.setVisible(false);
        panel.Quit.addActionListener(this);
        add(panel);

        // Add the Snake game
        snake.setVisible(false);
        snake.Quit.addActionListener(this);
        add(snake);

        // App icon
        ImageIcon image = new ImageIcon("icon.png");
		setIconImage(image.getImage());

        setVisible(true);
    }

    // Game loop
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks =60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        if (SinglePlayer) {
            while(panel.isRunning()) {  
                long now = System.nanoTime();
                delta += (now -lastTime)/ns;
                lastTime = now;
                if (delta >= 1) {
                        panel.moveBall();
                        panel.sPlayerMove();
                        panel.collision();
                        //panel.pause();
                        panel.winner();
                        repaint();
                    delta--;
                }
            }
        } else {
            while(panel.isRunning()) {  
                long now = System.nanoTime();
                delta += (now -lastTime)/ns;
                lastTime = now;
                if (delta >= 1) {
                        panel.moveBall();
                        panel.mPlayerMove();
                        panel.collision();
                        //panel.pause();
                        panel.winner();
                        repaint();
                    delta--;
                }
            }
        }
        if (panel.isWinner()) {
            panel.playSE(4);
        }
    }

    // Start a new Ping Pong game
    public void startGame() {
        panel.newGame();
        panel.setRounds(Integer.parseInt(selection.rounds.getText()));
        panel.setVisible(true);
        selection.setVisible(false);
        panel.setFocusable(true);
        panel.setRunning(true);
        gameThread = new Thread(this::run); // Thread for the game loop
        gameThread.start(); // Start the game loop thread
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if (e.getSource() == menu.buttons[0]) {
            menu.setVisible(false);
            selection.setVisible(true);
        }
        if (e.getSource() == menu.buttons[1]) {
            menu.setVisible(false);
            snake.startGame();
            snake.handlePlayAgain();
            snake.setVisible(true);
        }
        if (e.getSource() == menu.buttons[2]) {
            menu.setVisible(false);
            tictactoe.newGame();
            tictactoe.setVisible(true);
        }
        if (e.getSource() == selection.Back) {
            selection.setVisible(false);
            menu.setVisible(true);
        }
        if(e.getSource() == selection.SButton) {
            SinglePlayer = true;
            startGame();
        }
        if (e.getSource() == selection.MButton) {
            SinglePlayer = false;
            startGame();
        }
        if (e.getSource() == panel.Quit) {
            panel.setRunning(false);
            panel.setVisible(false);
            selection.rounds.setText("");
            selection.setVisible(true);
        }
        if (e.getSource() == tictactoe.Quit) {
            menu.setVisible(true);
            tictactoe.setVisible(false);
        }
        if (e.getSource() == snake.Quit) {
            menu.setVisible(true);
            snake.endGame();
            snake.setVisible(false);
        }
    }

}
