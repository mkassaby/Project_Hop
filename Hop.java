import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.*;

public class Hop {
    public static final int WIDTH = 400;
    public static final int HEIGHT = 600;
    public static final int DELAY = 40;

    private final JFrame frame;
    private Field field;
    private Axel axel;
    private Timer timer;
    private GamePanel gamePanel;
    private double difficulty = 1.0;


    public Hop() {
        this.field = new Field(WIDTH, HEIGHT);
        this.axel = new Axel(field, field.getFirstBlock().getX()+(field.getFirstBlock().getWidth()/2), field.getFirstBlock().getY());
        this.gamePanel = new GamePanel(field, axel);
        this.frame = new JFrame("Hop!");
    }

    public void showMainMenu() {
        frame.getContentPane().removeAll();
        frame.add(new MainMenu(frame, this));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void startGame(String playerName, int difficulty) {
        frame.getContentPane().removeAll();
        frame.add(gamePanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.difficulty = difficulty;
        
        timer = new Timer(DELAY, (ActionEvent e) -> {
            round();
            if (over()) {
                timer.stop();
                frame.dispose();
                saveScore(playerName, (int)gamePanel.getScore());
                showMainMenu();
            }
        });
        timer.start();
    }

    public void reset() {
        // Reset game components
        this.field = new Field(WIDTH, HEIGHT);
        this.axel = new Axel(field, field.getFirstBlock().getX()+(field.getFirstBlock().getWidth()/2), field.getFirstBlock().getY());
        this.gamePanel = new GamePanel(field, axel);
        this.difficulty = 1.0;
    }

    private void saveScore(String playerName, int score) {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection con = DriverManager.getConnection("jdbc:sqlite:results.db");
            PreparedStatement ps = con.prepareStatement("INSERT INTO scores (name, score) VALUES (?, ?)");
            ps.setString(1, playerName);
            ps.setInt(2, score);
            ps.executeUpdate();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void round() {
        field.update(difficulty); 
        axel.update(gamePanel.getLevel());           
        gamePanel.addScore();     
        difficulty = 1.0 + (gamePanel.getScore() / 1000.0);
        frame.repaint();
    }

    public boolean over() {
        return !axel.isSurviving();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Hop game = new Hop();
            game.showMainMenu();
        });
    }
}
