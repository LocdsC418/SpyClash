package src;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Fighter implements Movable, Attackable {

    private int x;
    private int y;
    private int width = 60;
    private int height = 120;
    private int velocityY = 0;
    private Color color;

    private int stunTimer = 0;
    private int knockbackX = 0;

    private final int GROUND = 920;
    private final int GRAVITY = 2;
    private final int SCREEN_WIDTH = 1920;

    private boolean onGround = true;
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    private int maxHP;
    private int hp;

    private int speedBoost = 0;
    private int speedBoostTimer = 0;
    private int damageBoost = 0;
    private int damageBoostTimer = 0;

    private int jumpCount = 0;

    private boolean attacking = false;
    private int attackTimer = 0;
    private int facingDirection = 1;

    public abstract int getSpeed();
    public abstract int getJumpForce();
    public abstract int getAttackRange();
    public abstract int getAttackDamage();
    public abstract int getAttackDuration();
    public abstract String getName();

    public abstract boolean canDoubleJump();

    public Fighter(int startX, Color color, int maxHP) {
        this.x = startX;
        this.y = GROUND - height;
        this.color = color;
        this.maxHP = maxHP;
        this.hp = maxHP;
    }

    public void update() {

        updateBuff();
        updateKnockback();

        if (stunTimer > 0) {
            updateStun();
        } else {
            updateMovement();
            updateAttack();
        }

        updateGravity();
        checkPosition();
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);

        if (attacking) {
            g.setColor(Color.YELLOW);
            if (facingDirection == 1) {
                g.fillRect(x + width, y + 20, getAttackRange(), 30);
            } else {
                g.fillRect(x - getAttackRange(), y + 20, getAttackRange(), 30);
            }
        }
    }

    public void getStunned(int duration) {
        stunTimer = duration;
    }

    public void getKnockBack(int knockback) {
        knockbackX = knockback;
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
        int maxJump = canDoubleJump() ? 2 : 1;
        if (jumpCount < maxJump) {
            velocityY = getJumpForce();
            onGround = false;
            jumpCount++;
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

    public void addSpeedBoost(int duration, int amount) {
        speedBoostTimer += duration;
        speedBoost = amount;
    }

    public void addDamageBoost(int duration, int amount) {
        damageBoostTimer += duration;
        damageBoost = amount;
    }

    public Rectangle getAttackHitbox() {
        if (!attacking || attackTimer != 1) {
            return null;
        }
        if (facingDirection == 1) {
            return new Rectangle(x + width, y + 20, getAttackRange(), 30);
        } else {
            return new Rectangle(x - getAttackRange(), y + 20, getAttackRange(), 30);
        }
    }

    public Rectangle getBodyHitbox() {
        return new Rectangle(x, y, width, height);
    }

    public void setFacingDirection(int facingDirection) {
        this.facingDirection = facingDirection;
    }

    public void setSpeedBoost(int boost) {
        this.speedBoost = boost;
    }

    public void setDamageBoost(int boost) {
        this.damageBoost = boost;
    }

    public void heal(int amount) {
        hp += amount;
        if (hp > maxHP) hp = maxHP;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getTotalDamage() { return getAttackDamage() + damageBoost;}
    
    public boolean isStunned() {
        return stunTimer > 0;
    }

    public int getHP() { return hp; }
    public int getMaxHP() { return maxHP; }

    private void updateBuff() {
        if (speedBoostTimer > 0) {
            speedBoostTimer--;
            if (speedBoostTimer <= 0) {
                speedBoostTimer = 0;
                speedBoost = 0;
            }
        }
        if (damageBoostTimer > 0) {
            damageBoostTimer--;
            if (damageBoostTimer <= 0) {
                damageBoostTimer = 0;
                damageBoost = 0;
            }
        }
    }
    private void updateKnockback() {
        if (Math.abs(knockbackX) > 0) {
            x += knockbackX;
            if (knockbackX > 0) {
                knockbackX--;
            } else { knockbackX++; }
        }
    }
    private void updateStun() { stunTimer--; }
    private void updateMovement() {
        if (!attacking || !onGround) {
            if (leftPressed){
                x -= getSpeed() + speedBoost;
                facingDirection = -1;
            }
            if (rightPressed){
                x += getSpeed() + speedBoost;
                facingDirection = 1;
            }
        }
    }
    private void updateAttack() {
        if (attacking) {
            attackTimer++;
            if (attackTimer >= getAttackDuration()) {
                attacking = false;
                attackTimer = 0;
            }
        }
    }
    private void checkPosition() {
        if (x < 0) x = 0;
        if (x > SCREEN_WIDTH - width) x = SCREEN_WIDTH - width;
    }
    private void updateGravity() {
        if (!onGround){
            velocityY += GRAVITY;
            y += velocityY;

            if (y + height >= GROUND){
                y = GROUND - height;
                velocityY = 0;
                onGround = true;
                jumpCount = 0;
            }
        }
    }

}
