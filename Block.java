public class Block {
    private final int x, y;
    private final int width;

    public Block(int x, int y, int width) {
        this.x = x;
        this.y = y;
        this.width = width;
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
