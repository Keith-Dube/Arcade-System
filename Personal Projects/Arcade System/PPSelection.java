import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PPSelection extends JPanel {

    JButton SButton = new JButton("Single Player");
    JButton MButton = new JButton("Multiplayer");
    JButton Back = new JButton("Back");
    JTextField rounds = new JTextField("");
    JLabel numOfRounds = new JLabel("First to get to:");
    private BufferedImage backgroundImage;

    PPSelection() {
        setBounds(0, 0, 800, 525);
        setBackground(new Color(0x123456));
        setLayout(null);

        // Accessing the image for the background
        try {
            backgroundImage = ImageIO.read(new File("Selection Background.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Button to return to main menu
        Back.setBounds(30, 30, 70, 27);
        Back.setFont(new Font("MV Boli", Font.PLAIN,15));
        Back.setForeground(Color.WHITE);
        Back.setBackground(new Color(0x123456));
        Back.setOpaque(false);
        Back.setBorderPainted(false);
        add(Back);

        // Text box to enter the number of rounds
        rounds.setBounds(getWidth()/2 + 50, getHeight()/2 - 77, 30, 27);
        rounds.setOpaque(false);
        rounds.setBackground(new Color(0x123456));
        rounds.setForeground(Color.WHITE);
        rounds.setFont(new Font("MV Boli", Font.PLAIN,15));
        add(rounds);

        // Button to play single player
        SButton.setBounds(getWidth()/2 - 60, getHeight()/2 - 30, 150, 27);
        SButton.setFont(new Font("MV Boli", Font.PLAIN,15));
        SButton.setForeground(new Color(0x123456));
        SButton.setBackground(Color.WHITE);
        add(SButton);

        // Button to play multiplayer
        MButton.setBounds(getWidth()/2 - 60, getHeight()/2 + 30, 150, 27);
        MButton.setFont(new Font("MV Boli", Font.PLAIN,15));
        MButton.setForeground(new Color(0x123456));
        MButton.setBackground(Color.WHITE);
        add(MButton);

        numOfRounds.setBounds(getWidth()/2 - 70, getHeight()/2 - 77, 150, 27);
        numOfRounds.setFont(new Font("MV Boli", Font.PLAIN,15));
        numOfRounds.setForeground(Color.WHITE);
        add(numOfRounds);
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
