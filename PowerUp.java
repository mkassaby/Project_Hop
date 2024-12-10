public class PowerUp {
    private int x, y;
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    private boolean collected = false;
    private long collectionTime;

    public PowerUp(int x, int y) {
        this.x = x;
        this.y = y;
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

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return WIDTH; }
    public int getHeight() { return HEIGHT; }

    public void setY(int y) {
        this.y = y;
    }
    
}