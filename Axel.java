/*La classe dl'axel represente le charactere dont on joue avec, les mecaniques des mouvements sont ici */

public class Axel {
    public static final double MAX_FALL_SPEED = -20;
    public static final double JUMP_SPEED = 18;
    public static final double GRAVITY = 1;
    public static final double DIVE_SPEED = 3 * GRAVITY;
    public static final double LATERAL_SPEED = 10;

    public static final int AXEL_WIDTH = 10;
    public static final int AXEL_HEIGHT = 10;
    public static final int BLOCK_HEIGHT = 10;

    private int x, y; //coordonnes


    //mouvements
    private boolean falling;
    private boolean jumping;
    private boolean diving;
    private boolean left;
    private boolean right;
    private boolean canJump = true; 
    private boolean hasDoubleJump = false;
    private boolean canDoubleJump = false; 
    private boolean hasSpeedBoost = false;

    private boolean surviving;

    private final Field field;


    //vitesse et temps non donner par le sujet
    private double ySpeed;
    private long doubleJumpStartTime;
    private long speedBoostStartTime;
    private static final long DOUBLE_JUMP_DURATION = 10000; 
    private static final long SPEED_BOOST_DURATION = 10000; 
    

    public Axel(Field f, int x, int y) {
        this.field = f;
        this.x = x;
        this.y = y;
        this.surviving = true;
    }

    public void computeMove(int difficulty) {
        /*methode mecanique des mouvements */
        double currentLateralSpeed;
        if (hasSpeedBoost) {
            currentLateralSpeed = LATERAL_SPEED * 2;
        } else {
            currentLateralSpeed = LATERAL_SPEED;
        } //un pouvoir dans le jeu
        
        if (left && x > 0) x -= currentLateralSpeed;
        if (right && x < field.width - AXEL_WIDTH) x += currentLateralSpeed;
        

        //boolean onBlock = checkStandingOnBlock();
        if (!checkCollision()) {
            falling = true;
        } else {
            falling = false;
            canJump = true;
            //canDoubleJump = true;  
            ySpeed = 0;
        }

        if (jumping && canJump) {
            ySpeed = JUMP_SPEED + difficulty; //vitesse qui augmente avec difficulte
            falling = true;
            jumping = false;
            canJump = false;
        }


        if (falling) {
            double gr = GRAVITY * (1 + (difficulty * 0.1)); //gravite qui augmente avec la difficulte
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
        /*methode qui gere les mouvements et  pouvoirs de l'axel*/
        computeMove(difficulty);
        updatePowerUps();
    }

    private void updatePowerUps() {
        /*methode qui verifie le temps de l'usage des pouvoirs (10 secondes) */
        if (hasDoubleJump && System.currentTimeMillis() - doubleJumpStartTime > DOUBLE_JUMP_DURATION) {
            hasDoubleJump = false;
        }
        if (hasSpeedBoost && System.currentTimeMillis() - speedBoostStartTime > SPEED_BOOST_DURATION) {
            hasSpeedBoost = false;
        }
    }


    /*en dessous on a deux methodes qui verifient: 
     * si l'axel est sur un block, sans mouvements
     * si l'axel tombe sur un block (donc verifie si le fall de l'axel vient sur le block)
     */
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

    public boolean canJump() {
        return canJump || (hasDoubleJump && canDoubleJump && falling);
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

    public void jump(GamePanel panel) {
        if (canJump) {
            jumping = true;
            canJump = false;
            canDoubleJump = hasDoubleJump;
            panel.playJumpSound();  
        } else if (hasDoubleJump && canDoubleJump && falling) {
            ySpeed = JUMP_SPEED / 2;
            canDoubleJump = false;
            panel.playJumpSound();
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

    public void activateSpeedBoost() {
        hasSpeedBoost = true;
        speedBoostStartTime = System.currentTimeMillis();
    }

    public boolean hasSpeedBoost() {
        return hasSpeedBoost;
    }

    public long getSpeedBoostEndTime() {
        return speedBoostStartTime + SPEED_BOOST_DURATION;
    }

    public boolean hasDoubleJump() {
        return hasDoubleJump;
    }

    public long getDoubleJumpEndTime() {
        return doubleJumpStartTime + DOUBLE_JUMP_DURATION;
    }
}
