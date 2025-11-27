import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MainMenu extends JPanel {

    JButton[] buttons = new JButton[3];
    private BufferedImage backgroundImage;

    public MainMenu() {
        setSize(800, 525);
        setLayout(null);

        // Accessing the image for the background
        try {
            backgroundImage = ImageIO.read(new File("Arcade wallpaper.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Button to enter the Ping Pong selection
        buttons[0] = new JButton("Pong Wars");
        buttons[0].setBounds(200, 210, 170, 35);
        buttons[0].setFont(new Font("Comic Sans MS", Font.PLAIN,25));
        buttons[0].setForeground(Color.WHITE);
        buttons[0].setBackground(new Color(0x123456));
        buttons[0].setFocusable(false);
        buttons[0].setOpaque(false);
        buttons[0].setBorderPainted(false);
        add(buttons[0]);

        // Button to enter the Snake game
        buttons[1] = new JButton("Snake");
        buttons[1].setBounds(500, 210, 170, 35);
        buttons[1].setFont(new Font("Comic Sans MS", Font.PLAIN,25));
        buttons[1].setForeground(Color.WHITE);
        buttons[1].setBackground(new Color(0x123456));
        buttons[1].setFocusable(false);
        buttons[1].setOpaque(false);
        buttons[1].setBorderPainted(false);
        add(buttons[1]);

        // Button to enter the TicTacToe game
        buttons[2] = new JButton("TicTacToe");
        buttons[2].setBounds(350, 280, 170, 35);
        buttons[2].setFont(new Font("Comic Sans MS", Font.PLAIN,25));
        buttons[2].setForeground(Color.WHITE);
        buttons[2].setBackground(new Color(0x123456));
        buttons[2].setFocusable(false);
        buttons[2].setOpaque(false);
        buttons[2].setBorderPainted(false);
        add(buttons[2]);
    }

    // Draw the background image
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, this);
        }
    }

}