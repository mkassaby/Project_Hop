import java.awt.event.ActionEvent;
import javax.swing.*;

public class Hop {
    public static final int WIDTH = 400;
    public static final int HEIGHT = 600;
    public static final int DELAY = 40;

    private final JFrame frame;
    private final Field field;
    private final Axel axel;
    private Timer timer;
    private GamePanel gamePanel;
    private double difficulty = 1.0;


    public Hop() {
        this.field = new Field(WIDTH, HEIGHT);
        this.axel = new Axel(field, field.getFirstBlock().getX()+(field.getFirstBlock().getWidth()/2), field.getFirstBlock().getY());
        this.gamePanel = new GamePanel(field, axel);

        this.frame = new JFrame("Hop!");
        frame.add(gamePanel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        Hop game = new Hop();

        game.timer = new Timer(DELAY, (ActionEvent e) -> {
            game.round();
            if (game.over()) {
                game.timer.stop();
                game.frame.remove(game.gamePanel);
            }
        });
        game.timer.start();
    }
}
