import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Field {
    public static final int ALTITUDE_GAP = 80;
    public static final int START_ALTITUDE = 40;
    private static final int MIN_BLOCK_WIDTH = 50;
    private static final int MAX_BLOCK_WIDTH = 100; 

    public final int width, height;

    private final List<Block> blocks;


    private double scrollSpeed = 1.0;

    private PowerUp currentPowerUp;
    private long lastPowerUpTime;

    private static final long POWERUP_INTERVAL = 20000; 
    private static final long POWERUP_DURATION = 10000; 

    public Field(int width, int height) {
        this.width = width;
        this.height = height;
        this.blocks = new ArrayList<>();
        generateBlocks();
        this.lastPowerUpTime = System.currentTimeMillis();
    }

    private void generateBlocks() {
        /*mehtode qui genere des blocks avec un random width qui respectent les tailles minimales et maximale */
        Random rand = new Random();
        for (int altitude = START_ALTITUDE; altitude < height; altitude += ALTITUDE_GAP) {
            int blockWidth = MIN_BLOCK_WIDTH + rand.nextInt(MAX_BLOCK_WIDTH - MIN_BLOCK_WIDTH + 1);
            int blockX = rand.nextInt(width - blockWidth);
            blocks.add(new Block(blockX, altitude, blockWidth));
        }
    }

    public void update(double difficulty) {
        /* method qui gere tout ce qui est en rapport avec le mouvements des blocks et le field */ 
        scrollSpeed = difficulty * 1.5; //devient plus dur avec le temps 
        
        for (Block block : blocks) {
            blocks.set(blocks.indexOf(block), 
                new Block(block.getX(), (int) (block.getY() + scrollSpeed), block.getWidth()));
        }

        blocks.removeIf(block -> block.getY() > height);
        int topBlockY = height;
        for (Block block : blocks) {
            topBlockY = Math.min(topBlockY, block.getY());
        }

        Random rand = new Random();
        while (topBlockY > START_ALTITUDE) {
            int newY = topBlockY - ALTITUDE_GAP;
            int blockWidth = MIN_BLOCK_WIDTH + rand.nextInt(MAX_BLOCK_WIDTH - MIN_BLOCK_WIDTH + 1);
            int blockX = rand.nextInt(width - blockWidth);
            blocks.add(new Block(blockX, newY, blockWidth));
            topBlockY = newY;
        } //comme generate blocks mais dynamique
        
        // update du logique des pouvoirs
        long currentTime = System.currentTimeMillis();
        if (currentPowerUp == null && currentTime - lastPowerUpTime >= POWERUP_INTERVAL) {
            generatePowerUp();
        } else if (currentPowerUp != null) {
            if (!currentPowerUp.isCollected() && currentTime - lastPowerUpTime >= POWERUP_DURATION) {
                currentPowerUp = null;
            } else if (currentPowerUp.isCollected() && 
                      currentTime - currentPowerUp.getCollectionTime() >= 10000) {
                currentPowerUp = null;
                lastPowerUpTime = currentTime; 
            }
        }
        
    }

    private void generatePowerUp() {
        /*methode qui genere les pouvoirs */
        Random rand = new Random();
        int x = rand.nextInt(width - 20); 
        int y = START_ALTITUDE + rand.nextInt((height/2) - START_ALTITUDE); 
        
        
        PowerUp.Type type;
        if (rand.nextBoolean()) {
            type = PowerUp.Type.DOUBLE_JUMP;
        } else {
            type = PowerUp.Type.SPEED_BOOST;
        }
        
        currentPowerUp = new PowerUp(x, y, type);
        lastPowerUpTime = System.currentTimeMillis();
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public Block getFirstBlock(){
        return blocks.get(6);
    }


    public double getScrollSpeed() {
        return scrollSpeed;
    }

    public PowerUp getCurrentPowerUp() {
        return currentPowerUp;
    }

}
