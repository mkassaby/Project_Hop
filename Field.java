import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Field {
    public static final int ALTITUDE_GAP = 80;
    public static final int START_ALTITUDE = 40;
    private static final int MIN_BLOCK_WIDTH = 50;
    private static final int MAX_BLOCK_WIDTH = 100; 

    public final int width, height;
    private int bottom, top; // bottom and top altitude
    private List<Block> blocks;
    private int currentLevel = 0;
    private int scrollSpeed = 1;

    public Field(int width, int height) {
        this.width = width;
        this.height = height;
        this.blocks = new ArrayList<>();
        generateBlocks();
    }

    private void generateBlocks() {
        Random rand = new Random();
        for (int altitude = START_ALTITUDE; altitude < height; altitude += ALTITUDE_GAP) {
            int blockWidth = MIN_BLOCK_WIDTH + rand.nextInt(MAX_BLOCK_WIDTH - MIN_BLOCK_WIDTH + 1);
            int blockX = rand.nextInt(width - blockWidth);
            blocks.add(new Block(blockX, altitude, blockWidth));
        }
    }

    public void update() {
        scrollSpeed = 1 + (currentLevel / 1000);
        for (Block block : blocks) {
            blocks.set(blocks.indexOf(block), 
                new Block(block.getX(), block.getY() + scrollSpeed, block.getWidth()));
        }

    
        int highestY = blocks.get(0).getY();
        for (Block block : blocks) {
            if (block.getY() < highestY) {
                highestY = block.getY();
            }
        }
        blocks.removeIf(block -> block.getY() > height);

        if (highestY > START_ALTITUDE) {
            Random rand = new Random();
            int blockWidth = MIN_BLOCK_WIDTH + rand.nextInt(MAX_BLOCK_WIDTH - MIN_BLOCK_WIDTH + 1);
            int blockX = rand.nextInt(width - blockWidth);
            blocks.add(new Block(blockX, highestY - ALTITUDE_GAP, blockWidth));
        }
        currentLevel += scrollSpeed;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public Block getFirstBlock(){
        return blocks.get(6);
    }

    // Add getter for current level
    public int getCurrentLevel() {
        return currentLevel;
    }
}
