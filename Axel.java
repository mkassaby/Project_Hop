public class Axel {
    public static final double MAX_FALL_SPEED = -20;
    public static final double JUMP_SPEED = 20;
    public static final double GRAVITY = 1;
    public static final double DIVE_SPEED = 3 * GRAVITY;
    public static final double LATERAL_SPEED = 8;

    private int x, y;

    private boolean falling;
    private boolean jumping;
    private boolean diving;
    private boolean left;
    private boolean right;

    private boolean surviving;

    private final Field field;

    private double verticalSpeed = 0;

    public Axel(Field f, int x, int y) {
        this.field = f;
        this.x = x;
        this.y = y;
        this.surviving = true;
    }

    private void computeMove() {
        if (left && x > 0) x -= LATERAL_SPEED;
        if (right && x < field.width) x += LATERAL_SPEED;

        if (!falling) {
            if (jumping) {
                verticalSpeed = JUMP_SPEED;
                falling = true;
            }
        } else {
            verticalSpeed -= GRAVITY;
            if (diving) verticalSpeed -= DIVE_SPEED;
            if (verticalSpeed < MAX_FALL_SPEED) 
                verticalSpeed = MAX_FALL_SPEED;
        }
        y += verticalSpeed;
    }

    public void update() {
        computeMove();
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public void setLeft(boolean left) { this.left = left; 
    }
    public void setRight(boolean right) { 
        this.right = right; 
    }
    public void setJumping(boolean jumping) { 
        this.jumping = jumping; 
    }
    public void setDiving(boolean diving) { 
        this.diving = diving; 
    }
}
