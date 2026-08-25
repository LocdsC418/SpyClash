import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Fighter implements Movable, Attackable {

    private int x;
    private int y;
    private int width = 50;
    private int height = 100;
    private int velocityY = 0;
    private Color color;

    private final int GROUND = 350;
    private final int GRAVITY = 1;
    private final int SCREEN_WIDTH = 800;

    private boolean onGround = true;
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    private int maxHP;
    private int hp;

    private boolean attacking = false;
    private int attackTimer = 0;
//    private final int ATTACK_DURATION = 15;
//    private final int ATTACK_RANGE = 60;
//    private final int ATTACK_DAMAGE = 10;
    private int facingDirection = 1;

    public abstract int getSpeed();
    public abstract int getJumpForce();
    public abstract int getAttackRange();
    public abstract int getAttackDamage();
    public abstract int getAttackDuration();
    public abstract String getName();

    public Fighter(int startX, Color color, int maxHP) {
        this.x = startX;
        this.y = GROUND;
        this.color = color;
        this.maxHP = maxHP;
        this.hp = maxHP;
    }

    public void update() {
        if (leftPressed){
            x -= getSpeed();
            facingDirection = -1;
        }
        if (rightPressed){
            x += getSpeed();
            facingDirection = 1;
        }

        if (x<0){
            x = 0;
        }
        if (x > SCREEN_WIDTH - width) {
            x = SCREEN_WIDTH - width;
        }

        if (!onGround){
            velocityY += GRAVITY;
            y += velocityY;

            if (y >= GROUND){
                y = GROUND;
                velocityY = 0;
                onGround = true;
            }
        }

        if (attacking) {
            attackTimer++;
            if (attackTimer >= getAttackDuration()) {
                attacking = false;
                attackTimer = 0;
            }
        }
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);

        if (attacking) {
            g.setColor(Color.YELLOW);
            if (facingDirection == 1) {
                g.fillRect(x + width, y + 20, getAttackRange(), 15);
            } else {
                g.fillRect(x - getAttackRange(), y + 20, getAttackRange(), 15);
            }
        }
    }

    @Override
    public void attack() {
        if (!attacking) {
            attacking = true;
            attackTimer = 0;
        }
    }

    @Override
    public void jump() {
        if (onGround) {
            velocityY = getJumpForce();
            onGround = false;
        }
    }

    @Override
    public void takeDamage(int damage) {
        hp -= damage;
        if (hp < 0) {
            hp = 0;
        }
    }

    @Override
    public boolean isAlive() { return hp > 0; }

    @Override
    public void setLeftPressed(boolean pressed) {
        this.leftPressed = pressed;
    }

    @Override
    public void setRightPressed(boolean pressed) {
        this.rightPressed = pressed;
    }

    public Rectangle getAttackHitbox() {
        if (!attacking || attackTimer != 1) {
            return null;
        }
        if (facingDirection == 1) {
            return new Rectangle(x + width, y + 20, getAttackRange(), 15);
        } else {
            return new Rectangle(x - getAttackRange(), y + 20, getAttackRange(), 15);
        }
    }

    public Rectangle getBodyHitbox() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public int getHP() { return hp; }
    public int getMaxHP() { return maxHP; }

}
