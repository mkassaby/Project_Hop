import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

public class GamePanel extends JPanel implements KeyListener {
    private static final int BLOCK_HEIGHT = 10;
    private static final int AXEL_WIDTH = 10;
    private static final int AXEL_HEIGHT = 10;

    private final Axel axel;
    private final Field field;

    private int score = 0;
    private int lastBlockY = -1;  // Track the Y position of the last block we stood on
    private boolean wasOnBlock = false;
    private double totalScrollSinceLastLanding = 0;

    public GamePanel(Field field, Axel axel) {
        this.field = field;
        this.axel = axel;

        setPreferredSize(new Dimension(field.width, field.height));
        setFocusable(true);
        addKeyListener(this);
    }

    public void addScore() {
        // Track the scroll
        totalScrollSinceLastLanding += field.getScrollSpeed();
        
        if (axel.checkStandingOnBlock()) {
            int currentBlockY = getCurrentBlockY();
            
            if (!wasOnBlock) {  
                if (lastBlockY != -1) {
                    int scrollAdjustedLastY = lastBlockY + (int)totalScrollSinceLastLanding;
                    int heightDifference = scrollAdjustedLastY - currentBlockY;
                    
                    if (heightDifference > 0) {
                        score += heightDifference;
                        System.out.println("Height difference: " + heightDifference + 
                                         " (Last: " + scrollAdjustedLastY + 
                                         ", Current: " + currentBlockY + ")");
                    }
                }
                lastBlockY = currentBlockY;
                totalScrollSinceLastLanding = 0;
            }
            wasOnBlock = true;
        } else {
            wasOnBlock = false;
        }
    }

    // Helper method to get the Y position of the block we're standing on
    private int getCurrentBlockY() {
        for (Block block : field.getBlocks()) {
            boolean xOverlap = (axel.getX() + AXEL_WIDTH/2 > block.getX()) && 
                             (axel.getX() - AXEL_WIDTH/2 < block.getX() + block.getWidth());
            boolean yMatch = Math.abs(axel.getY() - block.getY()) <= 1;
            
            if (xOverlap && yMatch) {
                return block.getY();
            }
        }
        return -1;
    }

    private int getLevel() {
        return 1 + (score / 1000); 
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, field.width, field.height); //arriere plan
        g.setColor(Color.BLACK);
        for (Block block : field.getBlocks()) {
            g.fillRect(block.getX(),block.getY(), block.getWidth(), BLOCK_HEIGHT);
        } //pour dessiner les blocks  
        g.setColor(Color.RED);
        g.fillOval(axel.getX() - AXEL_WIDTH/2,  axel.getY() - AXEL_HEIGHT, AXEL_WIDTH, AXEL_HEIGHT);//desiner l'axel

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Score: " + score , 10, 20);
        g.drawString("Level: " + getLevel(), 10, 40); // Ajout de l'affichage du niveau

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> axel.moveLeft();
            case KeyEvent.VK_RIGHT -> axel.moveRight();
            case KeyEvent.VK_UP -> axel.jump();
            case KeyEvent.VK_DOWN -> axel.dive();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT -> axel.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    public double getScore(){
        return this.score;
    }
}
