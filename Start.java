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
        JLabel error = new JLabel("Choose a difficulty!");
        textField = new JTextField(22);
        JButton addButton = new JButton("Play!");
        JRadioButton ez = new JRadioButton("Easy");
        JRadioButton hard = new JRadioButton("Hard");

        // Create button group
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(ez);
        buttonGroup.add(hard);

        // Add button actions
        addButton.addActionListener(e -> {
            if (score != 0) {
                inputText = textField.getText();
                if (!inputText.isEmpty()) {
                    setDone(true);
                    frame.setVisible(false);
                    hop.startGame(inputText, score);
                } else {
                    JOptionPane.showMessageDialog(frame, "Please enter a username!");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please choose a difficulty!");
            }
        });

        ez.addActionListener(e -> score = 1);
        hard.addActionListener(e -> score = 3);

        // Add components to panel
        add(nameLabel);
        add(textField);
        add(ez);
        add(hard);
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