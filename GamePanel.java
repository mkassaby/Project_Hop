import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;

public class GamePanel extends JPanel implements KeyListener {
    private static final int BLOCK_HEIGHT = 10;
    private static final int AXEL_WIDTH = 10;
    private static final int AXEL_HEIGHT = 10;

    private final Axel axel;
    private final Field field;
    private final Theme currentTheme;

    private int score = 0;
    private int lastBlockY = -1;  // Track the Y position of the last block we stood on
    private boolean wasOnBlock = false;
    private double totalScrollSinceLastLanding = 0;

    private Image yodaImg, doodleImg, StarsImg, JapanImg, jblocksImg, appleImg, coinImg, glowblockImg;
    private Clip backgroundMusic;

    public GamePanel(Field field, Axel axel, Theme theme) {
        this.field = field;
        this.axel = axel;
        this.currentTheme = theme;

        setPreferredSize(new Dimension(field.width, field.height));
        setFocusable(true);
        addKeyListener(this);

        try {
            yodaImg = ImageIO.read(new File("media/babyyoda.png"));
            doodleImg = ImageIO.read(new File("media/doodle.png"));
            JapanImg = ImageIO.read(new File("media/japan.png"));
            StarsImg = ImageIO.read(new File("media/Stars.png"));
            jblocksImg = ImageIO.read(new File("media/jblocks.png"));
            appleImg = ImageIO.read(new File("media/apple.png"));
            coinImg = ImageIO.read(new File("media/coin.png"));
            glowblockImg = ImageIO.read(new File("media/glowblock.png"));

            // Only load the music, don't play it yet
            String musicFile;
            if (theme == Theme.STAR_WARS) {
                musicFile = "media/rebel-theme.wav";
            } else {
                musicFile = "media/jsong.wav";
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(musicFile));
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);

        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void startMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.setFramePosition(0);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundMusic.start();
        }
    }

    public void addScore() {
        totalScrollSinceLastLanding += field.getScrollSpeed();
        
        if (axel.checkStandingOnBlock()) {
            int currentBlockY = getCurrentBlockY();
            
            if (!wasOnBlock) {  // Just landed on a block
                if (lastBlockY != -1) {
                    // Calculate actual height difference considering scroll
                    int scrollAdjustedLastY = lastBlockY + (int)totalScrollSinceLastLanding;
                    int heightDifference = Math.max(0, scrollAdjustedLastY - currentBlockY);
                    
                    // Award points for any upward movement
                    if (heightDifference > 0) {
                        // More granular scoring - award points for smaller jumps too
                        score += (heightDifference / 80) * 80;
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

    public int getLevel() {
        return 1 + (score / 1000); 
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (currentTheme == Theme.STAR_WARS) {
            g.drawImage(StarsImg, 0, 0, field.width, field.height, this);
            g.setColor(Color.YELLOW);
            for (Block block : field.getBlocks()) {
                g.drawImage(glowblockImg, block.getX(), block.getY(), block.getWidth(), BLOCK_HEIGHT, this);
            }
            g.drawImage(yodaImg, axel.getX() - AXEL_WIDTH/2, axel.getY() - ((AXEL_HEIGHT * 3) - 5), AXEL_WIDTH * 3, AXEL_HEIGHT * 3, this);
            g.setColor(Color.WHITE);
        } else {
            g.drawImage(JapanImg, 0, 0, field.width, field.height, this);
            for (Block block : field.getBlocks()) {
                g.drawImage(jblocksImg, block.getX(), block.getY(), block.getWidth(), BLOCK_HEIGHT, this);
            }
            g.drawImage(doodleImg, axel.getX() - AXEL_WIDTH/2, axel.getY() - ((AXEL_HEIGHT * 3) - 5), AXEL_WIDTH * 3, AXEL_HEIGHT * 3, this);
            g.setColor(Color.RED);
        }

        // Draw power-up
        PowerUp powerUp = field.getCurrentPowerUp();
        if (powerUp != null) {
            if (!powerUp.isCollected()) {
                if (currentTheme == Theme.STAR_WARS) {
                    g.drawImage(coinImg, powerUp.getX(), powerUp.getY(), 
                              powerUp.getWidth(), powerUp.getHeight(), this);
                } else {
                    g.drawImage(appleImg, powerUp.getX(), powerUp.getY(), 
                              powerUp.getWidth(), powerUp.getHeight(), this);
                }
            } else if (axel.hasDoubleJump()) {
                // Draw power-up timer
                int timeLeft = (int)((axel.getDoubleJumpEndTime() - System.currentTimeMillis()) / 1000);
                if (timeLeft > 0) {
                    g.setFont(new Font("Arial", Font.BOLD, 16));
                    g.drawString("Power-up: " + timeLeft + "s", field.width - 120, 50);
                }
            }
        }

        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Score: " + score , 10, 20);
        g.drawString("Level: " + getLevel(), 10, 40);
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

    public void stopMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
            backgroundMusic.close();
        }
    }

    public void checkPowerUpCollision() {
        PowerUp powerUp = field.getCurrentPowerUp();
        if (powerUp != null && !powerUp.isCollected() && 
            powerUp.intersects(axel.getX() - AXEL_WIDTH/2, axel.getY() - AXEL_HEIGHT/2, 
                             AXEL_WIDTH, AXEL_HEIGHT)) {
            powerUp.collect();
            axel.activateDoubleJump();
        }
    }
}
