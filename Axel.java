public class Axel {
    public static final double MAX_FALL_SPEED = -20;
    public static final double JUMP_SPEED = 20;
    public static final double GRAVITY = 1;
    public static final double DIVE_SPEED = 3 * GRAVITY;
    public static final double LATERAL_SPEED = 8;

    public static final int AXEL_WIDTH = 10;
    public static final int AXEL_HEIGHT = 10;
    public static final int BLOCK_HEIGHT = 10;

    private int x, y;

    private boolean falling = true;
    private boolean jumping;
    private boolean diving;
    private boolean left;
    private boolean right;

    private boolean surviving;

    private final Field field;

    private double ySpeed;


    public Axel(Field f, int x, int y) {
        this.field = f;
        this.x = x;
        this.y = y;
        this.surviving = true;
    }



    public void update() {
        if (left && x > 0) x -= LATERAL_SPEED;
        if (right && x < field.width - AXEL_WIDTH) {
            x += LATERAL_SPEED;
            if (x > field.width - AXEL_WIDTH) {
                x = field.width - AXEL_WIDTH;
            }
        }
        
        if (!checkStandingOnBlock()) {
            falling = true;
        }

        if (falling && !jumping) {
            ySpeed -= GRAVITY;
            if (diving) {
                ySpeed -= DIVE_SPEED;
                diving = false;
                if (ySpeed < MAX_FALL_SPEED) {
                    ySpeed = MAX_FALL_SPEED;
                }
            }
            if (ySpeed < MAX_FALL_SPEED) {
                ySpeed = MAX_FALL_SPEED;
            }
        }
    
        if (jumping && !falling) {
            ySpeed = JUMP_SPEED;
            falling = true;
            jumping = false;
        }
    
        y -= ySpeed;
    
        if (falling) {
            boolean collision = checkCollision();
            if (collision) {
                falling = false;
                ySpeed = 0;
            }
        }
    
        if (y > field.height) {
            surviving = false;
        }
    }
        

    private boolean checkStandingOnBlock() {
        for (Block block : field.getBlocks()) {
            boolean onBlock = (x + AXEL_WIDTH / 2 > block.getX()) &&
                              (x - AXEL_WIDTH / 2 < block.getX() + block.getWidth()) &&
                              (y == block.getY());
            if (onBlock) {
                return true;
            }
        }
        return false;
    }

    private boolean checkCollision() {
        for (Block block : field.getBlocks()) {
            boolean xOverlap = (x - AXEL_WIDTH / 2) < (block.getX() + block.getWidth()) && 
                               (x + AXEL_WIDTH / 2) > block.getX();
            boolean yTouch = y <= block.getY() && y >= block.getY() - AXEL_HEIGHT;
            boolean isFalling = ySpeed < 0;

            if (xOverlap && yTouch && isFalling ) { 
                y = block.getY(); 
                ySpeed = 0;
                falling = false;
                return true;
            }
        }
        return false;
    }



    public int getX() { return x; }
    public int getY() { return y; }

    public boolean isSurviving(){
        return surviving;
    }

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
