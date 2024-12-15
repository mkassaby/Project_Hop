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
    private Theme currentTheme = Theme.JAPAN; 
    private String currentPlayerName; // Add this field

    public Hop() {
        this.field = new Field(WIDTH, HEIGHT);
        this.axel = new Axel(field, field.getFirstBlock().getX()+(field.getFirstBlock().getWidth()/2), field.getFirstBlock().getY());
        this.gamePanel = new GamePanel(field, axel, currentTheme);
        this.frame = new JFrame("Hop!");
    }

    public void setTheme(Theme theme) {
        this.currentTheme = theme;
    }

    public Theme getCurrentTheme() {
        return currentTheme;
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
        this.currentPlayerName = playerName; 
        frame.getContentPane().removeAll();
        frame.add(gamePanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.difficulty = difficulty;
        
        
        gamePanel.startMusic();
        
        timer = new Timer(DELAY, (ActionEvent e) -> {
            round();
            frame.repaint();
        });
        timer.start();
    }

    public void reset() {
        if (gamePanel != null) {
            gamePanel.stopMusic();
        }
        this.field = new Field(WIDTH, HEIGHT);
        this.axel = new Axel(field, field.getFirstBlock().getX()+(field.getFirstBlock().getWidth()/2), field.getFirstBlock().getY());
        this.gamePanel = new GamePanel(field, axel, currentTheme);
        this.difficulty = 1.0;
    }

    private void saveScore(String playerName, int score) {
        gamePanel.stopMusic();  
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

    private void showGameOver(String playerName, int score) {
        frame.getContentPane().removeAll();
        GameOverScreen gameOverScreen = new GameOverScreen(frame, this, playerName, score);
        frame.add(gameOverScreen);
        frame.revalidate();
        frame.repaint();
        frame.setLocationRelativeTo(null);
        saveScore(playerName, score);
    }

    public void round() {
        field.update(difficulty); 
        axel.update(gamePanel.getLevel());
        gamePanel.checkPowerUpCollision(); 
        gamePanel.addScore();     
        difficulty = 1.0 + (gamePanel.getScore() / 1000.0);
        frame.repaint();
        if (over()) {
            timer.stop();
            gamePanel.stopMusic();
            int finalScore = (int)gamePanel.getScore();
            showGameOver(currentPlayerName, finalScore);
        }
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
