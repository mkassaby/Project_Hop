import java.awt.*;
import javax.swing.*;

public class MainMenu extends JPanel {
    private final JFrame frame;
    private final Hop hop;
    private final int PANEL_WIDTH = 400;
    private final int PANEL_HEIGHT = 600;

    public MainMenu(JFrame frame, Hop hop) {
        this.frame = frame;
        this.hop = hop;
        setupUI();
    }

    private void setupUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));

        // Title
        JLabel titleLabel = new JLabel("HOP!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Single Play Button
        JButton playButton = new JButton("Play Game");
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playButton.addActionListener(e -> startGame());

        // Add components
        add(Box.createVerticalStrut(50));
        add(titleLabel);
        add(Box.createVerticalStrut(50));
        add(playButton);
        add(Box.createVerticalStrut(30));
        add(new Leaderboard(frame));
    }

    private void startGame() {
        frame.setVisible(false);
        frame.getContentPane().removeAll();
        hop.reset(); // Reset the game before starting
        frame.add(new Start(frame, hop));
        frame.pack();
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}