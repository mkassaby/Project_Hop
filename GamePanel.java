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
    private int lastBlockY = -1; 
    private boolean wasOnBlock = false;
    private double totalScrollSinceLastLanding = 0;

    //images
    private Image yodaImg, doodleImg, StarsImg, JapanImg, jblocksImg, appleImg, coinImg, glowblockImg;
    private Image falconImg, drinkImg;
    
    //sons
    private Clip backgroundMusic;
    private Clip jumpSound;
    private Clip powerupSound;
    private Clip speedSound;



    public GamePanel(Field field, Axel axel, Theme theme) {
        this.field = field;
        this.axel = axel;
        this.currentTheme = theme;

        setPreferredSize(new Dimension(field.width, field.height));
        setFocusable(true);
        addKeyListener(this);

        //load les images
        try {
            yodaImg = ImageIO.read(new File("media/babyyoda.png"));
            doodleImg = ImageIO.read(new File("media/doodle.png"));
            JapanImg = ImageIO.read(new File("media/japan.png"));
            StarsImg = ImageIO.read(new File("media/Stars.png"));
            jblocksImg = ImageIO.read(new File("media/jblocks.png"));
            appleImg = ImageIO.read(new File("media/apple.png"));
            coinImg = ImageIO.read(new File("media/coin.png"));
            glowblockImg = ImageIO.read(new File("media/glowblock.png"));
            falconImg = ImageIO.read(new File("media/falcon.png"));
            drinkImg = ImageIO.read(new File("media/drink.png"));

            // load les sons
            String musicFile;
            String jumpSoundFile;
            String powerupSoundFile;
            String speedBoostFile;
            if (theme == Theme.STAR_WARS) {
                musicFile = "media/rebel-theme.wav";
                jumpSoundFile = "media/yo_jump.wav";
                powerupSoundFile = "media/r2.wav";
                speedBoostFile = "media/chew.wav";
            } else {
                musicFile = "media/jsong.wav";
                jumpSoundFile = "media/nin_jump.wav";
                powerupSoundFile = "media/bite.wav";
                speedBoostFile = "media/ahh.wav";
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(musicFile));
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);

            AudioInputStream jumpAudioStream = AudioSystem.getAudioInputStream(new File(jumpSoundFile));
            jumpSound = AudioSystem.getClip();
            jumpSound.open(jumpAudioStream);

            AudioInputStream powerupAudioStream = AudioSystem.getAudioInputStream(new File(powerupSoundFile));
            powerupSound = AudioSystem.getClip();
            powerupSound.open(powerupAudioStream);

            AudioInputStream speedAudioStream = AudioSystem.getAudioInputStream(new File(speedBoostFile));
            speedSound = AudioSystem.getClip();
            speedSound.open(speedAudioStream);

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
            
            if (!wasOnBlock) {  
                if (lastBlockY != -1) {
                    
                    int scrollAdjustedLastY = lastBlockY + (int)totalScrollSinceLastLanding;
                    int heightDifference = scrollAdjustedLastY - currentBlockY;
                    
                    
                    if (heightDifference > 0) {
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


        PowerUp powerUp = field.getCurrentPowerUp();
        if (powerUp != null) {
            if (!powerUp.isCollected()) {
                Image powerUpImg;
                if (powerUp.getType() == PowerUp.Type.SPEED_BOOST) {
                    if(currentTheme == Theme.STAR_WARS){
                        powerUpImg = falconImg;
                    }else{
                        powerUpImg = drinkImg;
                    }
                } else {
                    if(currentTheme == Theme.STAR_WARS){
                        powerUpImg = coinImg;
                    }else{
                        powerUpImg = appleImg;
                    }
                }
                g.drawImage(powerUpImg, powerUp.getX(), powerUp.getY(), 
                          powerUp.getWidth(), powerUp.getHeight(), this);
            } else {

                int y = 50;
                if (axel.hasDoubleJump()) {
                    int timeLeft = (int)((axel.getDoubleJumpEndTime() - System.currentTimeMillis()) / 1000);
                    if (timeLeft > 0) {
                        g.setFont(new Font("Arial", Font.BOLD, 16));
                        g.drawString("Double Jump: " + timeLeft + "s", field.width - 120, y);
                        y += 20;
                    }
                }
                if (axel.hasSpeedBoost()) {
                    int timeLeft = (int)((axel.getSpeedBoostEndTime() - System.currentTimeMillis()) / 1000);
                    if (timeLeft > 0) {
                        g.setFont(new Font("Arial", Font.BOLD, 16));
                        g.drawString("Speed Boost: " + timeLeft + "s", field.width - 120, y);
                    }
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
            case KeyEvent.VK_UP -> {
                axel.jump(this);
            }
            case KeyEvent.VK_DOWN -> axel.dive();
        }
        
        wasOnBlock = axel.checkStandingOnBlock();
    }



    public void playJumpSound() {
        if (jumpSound != null) {
            jumpSound.setFramePosition(0);
            jumpSound.start();
        }
    }

    private void playSpeedBoostSound(){
        if(speedSound != null){
            speedSound.setFramePosition(0);
            speedSound.start();
        }
    }

    private void playDoubleJumpPowerSound() {
        if (powerupSound != null) {
            powerupSound.setFramePosition(0);
            powerupSound.start();
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
        if (jumpSound != null) {
            jumpSound.close();
        }
        if (powerupSound != null) {
            powerupSound.close();
        }

        
    }

    public void checkPowerUpCollision() {
        PowerUp powerUp = field.getCurrentPowerUp();
        if (powerUp != null && !powerUp.isCollected() && 
            powerUp.intersects(axel.getX() - AXEL_WIDTH/2, axel.getY() - AXEL_HEIGHT/2, 
                             AXEL_WIDTH, AXEL_HEIGHT)) {
            powerUp.collect();
            if (powerUp.getType() == PowerUp.Type.DOUBLE_JUMP) {
                axel.activateDoubleJump();
                playDoubleJumpPowerSound();
            } else {
                axel.activateSpeedBoost();
                playSpeedBoostSound();
            }
        }
    }


}
