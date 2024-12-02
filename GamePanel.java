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

    public GamePanel(Field field, Axel axel) {
        this.field = field;
        this.axel = axel;

        setPreferredSize(new Dimension(field.width, field.height));
        setFocusable(true);
        addKeyListener(this);
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
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:axel.setLeft(true); break;
            case KeyEvent.VK_RIGHT:axel.setRight(true); break;
            case KeyEvent.VK_UP:axel.setJumping(true); break;
            case KeyEvent.VK_DOWN:axel.setDiving(true); break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:axel.setLeft(false); break;
            case KeyEvent.VK_RIGHT:axel.setRight(false); break;
            case KeyEvent.VK_UP:axel.setJumping(false); break;
            case KeyEvent.VK_DOWN:axel.setDiving(false); break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) { }
}
