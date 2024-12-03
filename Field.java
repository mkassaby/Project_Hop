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
    private double scrollSpeed = 1;
    private int totalScroll = 0;

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
        int scrollAmount = (int)scrollSpeed;
        totalScroll += scrollAmount;
        
        for (Block block : blocks) {
            blocks.set(blocks.indexOf(block), 
                new Block(block.getX(), block.getY() + scrollAmount, block.getWidth()));
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

        

    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public Block getFirstBlock(){
        return blocks.get(6);
    }

    public int getTotalScroll() {
        return totalScroll;
    }

}
