import java.awt.*;
import javax.swing.*;

public class Start extends JPanel {
    private final JFrame frame;
    private final JTextField textField;
    private final int tailleCase = 36;
    private String inputText;
    private boolean done;
    private int score;
    private final Hop hop;

    public Start(JFrame frame, Hop hop) {
        this.frame = frame;
        this.hop = hop;
        setPreferredSize(new Dimension(9 * tailleCase, 9 * tailleCase));

        // Create UI components
        JLabel nameLabel = new JLabel("Username:");
        textField = new JTextField(22);
        JButton addButton = new JButton("Play!");

        // Add button actions
        addButton.addActionListener(e -> {
            inputText = textField.getText();
            if (!inputText.isEmpty()) {
                setDone(true);
                frame.setVisible(false);
                hop.startGame(inputText, score);
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter a username!");
            }
        });

        // Add components to panel
        add(nameLabel);
        add(textField);
        add(addButton);
    }

    public String getName() {
        return inputText;
    }

    public void setDone(boolean b) {
        done = b;
    }

    public boolean getDone() {
        return done;
    }

    public int getScore() {
        return score;
    }
}