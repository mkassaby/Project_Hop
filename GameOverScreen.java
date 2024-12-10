
import java.awt.*;
import javax.swing.*;

public class GameOverScreen extends JPanel {
    private final JFrame frame;
    private final Hop hop;
    private final String playerName;
    private final int finalScore;

    public GameOverScreen(JFrame frame, Hop hop, String playerName, int finalScore) {
        this.frame = frame;
        this.hop = hop;
        this.playerName = playerName;
        this.finalScore = finalScore;
        setupUI();
    }

    private void setupUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(400, 600));
        
        // Game Over Text
        JLabel gameOverLabel = new JLabel("GAME OVER!");
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 48));
        gameOverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Score Text
        JLabel scoreLabel = new JLabel("Score: " + finalScore);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Player Name
        JLabel nameLabel = new JLabel("Player: " + playerName);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Continue Button
        JButton continueButton = new JButton("Continue");
        continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        continueButton.addActionListener(e -> {
            hop.showMainMenu();
        });

        // Add components with spacing
        add(Box.createVerticalStrut(100));
        add(gameOverLabel);
        add(Box.createVerticalStrut(50));
        add(nameLabel);
        add(Box.createVerticalStrut(30));
        add(scoreLabel);
        add(Box.createVerticalStrut(50));
        add(continueButton);
    }
}