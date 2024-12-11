import java.awt.*;
import java.io.File;
import javax.sound.sampled.*;
import javax.swing.*;

public class GameOverScreen extends JPanel {
    private final JFrame frame;
    private final Hop hop;
    private final String playerName;
    private final int score;
    private ImageIcon gameOverGif;
    private Clip gameOverSound;

    public GameOverScreen(JFrame frame, Hop hop, String playerName, int score) {
        this.frame = frame;
        this.hop = hop;
        this.playerName = playerName;
        this.score = score;
        
        String soundFile;

        if (hop.getCurrentTheme() == Theme.STAR_WARS) {
            gameOverGif = new ImageIcon("media/yoda.gif");
            soundFile = "media/control.wav";
        } else {
            gameOverGif = new ImageIcon("media/ninga.gif");
            soundFile = "media/goninja.wav";
        }
        try {
            gameOverSound = AudioSystem.getClip();
            gameOverSound.open(AudioSystem.getAudioInputStream(new File(soundFile)));
            gameOverSound.start();
        } catch (Exception e) {
            System.out.println("Error playing sound: " + e.getMessage());
        }
        
        setupUI();
    }

    private void setupUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(400, 600));

        // Game Over GIF label
        JLabel gifLabel = new JLabel(gameOverGif);
        gifLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Game Over text
        JLabel gameOverLabel = new JLabel("GAME OVER");
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 48));
        gameOverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Score display
        JLabel scoreLabel = new JLabel(playerName + "'s Score: " + score);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Play Again button
        JButton playAgainButton = new JButton("Play Again");
        playAgainButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playAgainButton.addActionListener(e -> {
            dispose();
            frame.setVisible(false);
            hop.showMainMenu();
        });

        // Add components with spacing
        add(Box.createVerticalStrut(50));
        add(gameOverLabel);
        add(Box.createVerticalStrut(30));
        add(gifLabel);
        add(Box.createVerticalStrut(30));
        add(scoreLabel);
        add(Box.createVerticalStrut(30));
        add(playAgainButton);
        add(Box.createVerticalStrut(50));
    }

    public void dispose() {
        if (gameOverSound != null) {
            gameOverSound.stop();
            gameOverSound.close();
        }
    }
}