public class PowerUp {
    private int x, y;
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    private boolean collected = false;
    private long collectionTime;

    public enum Type {
        DOUBLE_JUMP,
        SPEED_BOOST
    }
    
    private final Type type;

    public PowerUp(int x, int y, Type type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public boolean intersects(int playerX, int playerY, int playerWidth, int playerHeight) {
        return !collected && 
               playerX < x + WIDTH &&
               playerX + playerWidth > x &&
               playerY < y + HEIGHT &&
               playerY + playerHeight > y;
    }

    public void collect() {
        this.collected = true;
        this.collectionTime = System.currentTimeMillis();
    }

    public boolean isCollected() {
        return collected;
    }

    public long getCollectionTime() {
        return collectionTime;
    }

    public Type getType() {
        return type;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return WIDTH; }
    public int getHeight() { return HEIGHT; }

    public void setY(int y) {
        this.y = y;
    }
    
}