import java.util.Random;

public class Block {
    protected  int x, y;
    private final int width;

    public Block(int x, int y, int width) {
        this.x = x;
        this.y = y;
        this.width = width;
    }

    public Block(int x, int y){
        this(x, y, 50 + new Random().nextInt(50));
    }

    public int getX() { 
        return x; 
    }
    public int getY() { 
        return y; 
    }
    public int getWidth() { 
        return width; 
    }
}
