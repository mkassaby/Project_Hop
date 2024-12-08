import java.awt.*;
import javax.swing.*;

public class Start extends JPanel{
    private final JFrame frame;
    private final JTextField textField;
    private final int tailleCase = 36;
    private String inputText;
    private boolean done;
    private int score;
    private final Hop hop; // Add a reference to Hop

    public Start(JFrame frame, Hop hop) { // Modify constructor to accept Hop
        this.frame = frame;
        this.hop = hop;
//        frame.setBackground(Color.LIGHT_GRAY);//configuration du panneau
        setPreferredSize(new Dimension(9 * tailleCase, 9 * tailleCase));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        done = false;
        JLabel nameLabel = new JLabel("Username:");
        JLabel error = new JLabel("Choose a difficulty!");

        textField = new JTextField(22);
        JButton addButton = new JButton("Play!");

       JRadioButton ez = new JRadioButton("Easy");
        JRadioButton hard = new JRadioButton("Hard");

        // Create a button group to ensure only one radio button is selected at a time
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(ez);
        buttonGroup.add(hard); 

       /* Image bckImg;
        try {
            bckImg = ImageIO.read(new File("media/wall-icon.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        JLabel backgroundLabel = new JLabel(new ImageIcon(bckImg)); */
        displayLeaderboard(frame);

        addButton.addActionListener(e -> {
            if (score != 0) {
                inputText = textField.getText();
                System.out.println("Input: " + inputText);
                setDone(true);
                frame.setVisible(false);
                hop.startGame(inputText, score); // Start the game
            } else {
                frame.add(error);
                frame.repaint();
            }
        });
        
        
        ez.addActionListener(e -> {
            score = 1;
        });

        hard.addActionListener(e -> {
            score = 3;
        });



        // Add components to the panel
        add(nameLabel);
        add(textField);
        add(ez);
        add(hard);
        add(addButton);

    }

    public String getName(){
        return inputText;
    }

    public void setDone(boolean b){
        done = b;
    }
    public boolean getDone(){
        return done;
    }


    public int getScore() {
        return score;
    }

    private void displayLeaderboard(JFrame frame) {
        if(!frame.isVisible()) {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(new Leaderboard(frame));
            frame.pack();
            frame.setVisible(true);
        }else{
            frame.setVisible(false);
        }
    }



}