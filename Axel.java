public class Axel {
    public static final double MAX_FALL_SPEED = -20;
    public static final double JUMP_SPEED = 18;
    public static final double GRAVITY = 1;
    public static final double DIVE_SPEED = 3 * GRAVITY;
    public static final double LATERAL_SPEED = 10;

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
    private boolean hasDoubleJump = false;
    private long doubleJumpStartTime;
    private static final long DOUBLE_JUMP_DURATION = 10000; // 5 seconds
    private boolean canDoubleJump = false;  // Add this new field

    public Axel(Field f, int x, int y) {
        this.field = f;
        this.x = x;
        this.y = y;
        this.surviving = true;
    }

    public void computeMove(int difficulty) {
        if (left && x > 0) x -= LATERAL_SPEED;
        if (right && x < field.width - AXEL_WIDTH) x += LATERAL_SPEED;
        

        //boolean onBlock = checkStandingOnBlock();
        if (!checkCollision()) {
            falling = true;
        } else {
            falling = false;
            canJump = true;
            canDoubleJump = true;  
            ySpeed = 0;
        }

        if (jumping && canJump) {
            ySpeed = JUMP_SPEED + difficulty;
            falling = true;
            jumping = false;
            canJump = false;
        }


        if (falling) {
            double gr = GRAVITY * (1 + (difficulty * 0.1));
            ySpeed -= gr * 1.5;
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
            //currentBlock = null;
            checkCollision();
        }

        if (y > field.height) {
            surviving = false;
        }
    }

    public void update(int difficulty) {
        computeMove(difficulty);
        
        // Check if double jump has expired
        if (hasDoubleJump && System.currentTimeMillis() - doubleJumpStartTime > DOUBLE_JUMP_DURATION) {
            hasDoubleJump = false;
        }
    }
        

    public boolean checkStandingOnBlock() {
        for (Block block : field.getBlocks()) {
            if ((x + AXEL_WIDTH/2 > block.getX()) && 
                (x - AXEL_WIDTH/2 < block.getX() + block.getWidth()) && 
                (y == block.getY())) {
                return true;
            }
        }
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
                //currentBlock = block;
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
            canDoubleJump = hasDoubleJump;  // Enable double jump if power-up is active
        } else if (hasDoubleJump && canDoubleJump && falling) {
            // Perform double jump with half the speed
            ySpeed = JUMP_SPEED / 2;
            canDoubleJump = false;  // Use up the double jump
        }
    }

    public void dive() {
        diving = true;
    }

    public void stop() {
        left = false;
        right = false;
    }

    public void activateDoubleJump() {
        hasDoubleJump = true;
        doubleJumpStartTime = System.currentTimeMillis();
    }

    // Add these methods to access double jump status and end time
    public boolean hasDoubleJump() {
        return hasDoubleJump;
    }

    public long getDoubleJumpEndTime() {
        return doubleJumpStartTime + DOUBLE_JUMP_DURATION;
    }
}
