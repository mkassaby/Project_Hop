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

        // Create panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        // Play Button
        JButton playButton = new JButton("Play Game");
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playButton.addActionListener(e -> startGame());

        // Theme Button
        JButton themeButton = new JButton("Choose Theme");
        themeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        themeButton.addActionListener(e -> chooseTheme());

        // Add buttons to panel
        buttonPanel.add(playButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(themeButton);

        // Add components
        add(Box.createVerticalStrut(50));
        add(titleLabel);
        add(Box.createVerticalStrut(50));
        add(buttonPanel);
        add(Box.createVerticalStrut(30));
        add(new Leaderboard(frame));
    }

    private void chooseTheme() {
        Object[] options = {"Star Wars", "Japan"};
        int choice = JOptionPane.showOptionDialog(frame,
            "Choose your theme:",
            "Theme Selection",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);
        
        if (choice == 0) {
            hop.setTheme(Theme.STAR_WARS);
        } else if (choice == 1) {
            hop.setTheme(Theme.JAPAN);
        }
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