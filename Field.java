import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Field {
    public static final int ALTITUDE_GAP = 80;
    public static final int START_ALTITUDE = 40;
    private static final int MIN_BLOCK_WIDTH = 50;
    private static final int MAX_BLOCK_WIDTH = 100; 

    public final int width, height;
    private int bottom, top;
    private List<Block> blocks;
    private double scrollSpeed = 1.0;
    private double totalScroll = 0;  // Add this field

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

    public void update(double difficulty) {
        scrollSpeed = difficulty * 2;
        totalScroll += scrollSpeed;
        
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
        }
            
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public Block getFirstBlock(){
        return blocks.get(6);
    }

    public double getTotalScroll() {
        return totalScroll;
    }

    public double getScrollSpeed() {
        return scrollSpeed;
    }

}
