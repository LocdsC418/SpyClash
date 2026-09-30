package src.fighter;

import src.GameConfig;
import src.weapon.Bullet;
import src.weapon.Weapon;

import java.awt.*;

public abstract class Fighter implements Movable, Attackable {

    private int x;
    private int y;
    private int width = 120;
    private int height = 240;
    private int velocityY = 0;
    private Color color;
    private Image fighterImage;

    private int stunTimer = 0;
    private int knockbackX = 0;

    private final int GRAVITY = 2;

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

    private Weapon weapon = null;
    private Bullet pendingBullet = null;

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

    public Fighter(int startX, Color color, int maxHP, Image figherImage) {
        this.x = startX;
        this.y = GameConfig.GROUND - height;
        this.color = color;
        this.maxHP = maxHP;
        this.hp = maxHP;
        this.fighterImage = figherImage;
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
        if (fighterImage != null) {
            if (facingDirection == 1) {
                g.drawImage(fighterImage, x, y, width, height, null);
            } else {
                g.drawImage(fighterImage, x+width, y, -(width), height, null);
            }
        }

        g.setColor(color);
        g.drawRect(x, y, width, height);

        if (isLongRange()) return;
        if (attacking) {
            g.setColor(getAttackColor());
            if (facingDirection == 1) {
                g.fillRect(x + width, y + 20, getFinalAttackRange(), 30);
            } else {
                g.fillRect(x - getFinalAttackRange(), y + 20, getFinalAttackRange(), 30);
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

            if (hasWeapon()) {
                this.pendingBullet = weapon.createBullet(this);
            }
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

        if (isLongRange()) return null;

        if (!attacking || attackTimer != 1) {
            return null;
        }
        if (facingDirection == 1) {
            return new Rectangle(x + width, y + 20, getFinalAttackRange(), 30);
        } else {
            return new Rectangle(x - getFinalAttackRange(), y + 20, getFinalAttackRange(), 30);
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
    public void setX(int x) {
        this.x = x;
    }


    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }
    public boolean hasWeapon() { return weapon != null && !weapon.isBroken(); }
    public int getFinalAttackDuration() { return hasWeapon() ? weapon.getAttackDuration() : getAttackDuration();}
    public int getFinalAttackRange() { return hasWeapon() ? weapon.getRange() : getAttackRange(); }
    public int getTotalDamage() {
        return hasWeapon() ? weapon.getDamage() + damageBoost : getAttackDamage() + damageBoost;
    }
    public int getFacingDirection() {
        return facingDirection;
    }

    public Bullet takePendingBullet() {
        Bullet bullet = this.pendingBullet;
        this.pendingBullet = null;
        return bullet;
    }

    public Color getAttackColor() { return hasWeapon() ? weapon.getColor() : Color.YELLOW;}

    public void heal(int amount) {
        hp += amount;
        if (hp > maxHP) hp = maxHP;
    }
    public void pushX(int amount) {
        x += amount;
        checkPosition();
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    
    public boolean isStunned() {
        return stunTimer > 0;
    }

    public int getHP() { return hp; }
    public int getMaxHP() { return maxHP; }

    private boolean isLongRange() {
        return hasWeapon() && !weapon.isMelee();
    }

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
            if (attackTimer >= getFinalAttackDuration()) {
                attacking = false;
                attackTimer = 0;
                if (hasWeapon()) {
                    weapon.used();
                    if (weapon.isBroken()) {
                        weapon = null;
                    }
                }
            }
        }
    }
    private void checkPosition() {
        if (x < 0) x = 0;
        if (x > GameConfig.WIDTH - width) x = GameConfig.WIDTH - width;
    }
    private void updateGravity() {
        if (!onGround){
            velocityY += GRAVITY;
            y += velocityY;

            if (y + height >= GameConfig.GROUND){
                y = GameConfig.GROUND - height;
                velocityY = 0;
                onGround = true;
                jumpCount = 0;
            }
        }
    }

}
