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

    private boolean falling;
    private boolean jumping;
    private boolean diving;
    private boolean left;
    private boolean right;

    private boolean surviving;

    private final Field field;

    private double ySpeed;

    private boolean canJump = true; 

    private Block currentBlock;

    public Axel(Field f, int x, int y) {
        this.field = f;
        this.x = x;
        this.y = y;
        this.surviving = true;
    }

    public void computeMove() {
        if (left && x > 0) x -= LATERAL_SPEED;
        if (right && x < field.width - AXEL_WIDTH) x += LATERAL_SPEED;
        

        //boolean onBlock = checkStandingOnBlock();
        if (!checkStandingOnBlock()) {
            falling = true;
        } else {
            falling = false;
            canJump = true;
            ySpeed = 0;
        }

        if (jumping && canJump) {
            ySpeed = JUMP_SPEED;
            falling = true;
            jumping = false;
            canJump = false;
        }


        if (falling) {
            ySpeed -= GRAVITY;
            if (diving) {
                ySpeed -= DIVE_SPEED;
                diving = false;
            }
            if (ySpeed < MAX_FALL_SPEED) {
                ySpeed = MAX_FALL_SPEED;
            }
        }
        y -= ySpeed;

        if (falling) {
            checkCollision();
        }

        if (y > field.height) {
            surviving = false;
        }
    }

    public void update() {
        computeMove();
    }
        

    public boolean checkStandingOnBlock() {
        for (Block block : field.getBlocks()) {
            boolean onBlock = (x + AXEL_WIDTH / 2 > block.getX()) &&
                              (x - AXEL_WIDTH / 2 < block.getX() + block.getWidth()) &&
                              (y == block.getY() + 10);
            if (onBlock) {
                currentBlock = block;
                return true;
            }
        }
        currentBlock = null;
        return false;
    }

    public boolean checkCollision() {
        for (Block block : field.getBlocks()) {
            boolean xOverlap = (x + AXEL_WIDTH/2 > block.getX()) && 
                             (x - AXEL_WIDTH/2 < block.getX() + block.getWidth());
            boolean yTouch = y >= block.getY() - AXEL_HEIGHT && y <= block.getY() + BLOCK_HEIGHT;
            
            if (xOverlap && yTouch && ySpeed < 0) {
                y = block.getY();
                ySpeed = 0;
                falling = false;
                canJump = true;
                return true;
            }
        }
        return false;
    }

    public Block getCurrentBlock() {
        return currentBlock;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public boolean isSurviving(){
        return surviving;
    }

    public void moveLeft() {
        left = true;
    }

    public void moveRight() {
        right = true;
    }

    public void jump() {
        if (canJump) {
            jumping = true;
            canJump = false;
        }
    }

    public void dive() {
        diving = true;
    }

    public void stop() {
        left = false;
        right = false;
    }
}
