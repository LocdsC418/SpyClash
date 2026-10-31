package src.fighter;

import src.core.Assets;
import src.core.GameConfig;
import src.weapon.Bullet;
import src.weapon.Weapon;

import java.awt.*;

public abstract class Fighter {

    private static final int GRAVITY = 2;
    private static final int SPRITE_SIZE = 240;
    private static final int SPRITE_PIXELS = 64;
    private static final int WALK_FRAME_COUNT = 4;
    private static final int WALK_FRAME_TICKS = 6;

    private int x;
    private int y;
    private int width = 120;
    private int height = 216;
    private int velocityY = 0;
    private Color color;

    private String spritePath;
    private Image idleImage;
    private Image attackImage;
    private Image hurtImage;
    private Image[] walkFrames = new Image[WALK_FRAME_COUNT];
    private int walkFrame = 0;
    private int walkTick = 0;
    private boolean walking = false;

    private int stunTimer = 0;
    private int knockbackX = 0;

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

    public Fighter(int startX, Color color, int maxHP, String spriteFolder) {
        this.x = startX;
        this.y = GameConfig.GROUND - height;
        this.color = color;
        this.maxHP = maxHP;
        this.hp = maxHP;

        spritePath = "/res/fighter/" + spriteFolder + "/";
        idleImage = Assets.loadImage(spritePath + "idle.png");
        attackImage = Assets.loadImage(spritePath + "attack.png");
        hurtImage = Assets.loadImage(spritePath + "hurt.png");
        for (int i = 0; i < walkFrames.length; i++) {
            walkFrames[i] = Assets.loadImage(spritePath + "walk" + (i + 1) + ".png");
        }
    }

    public void update() {

        updateBuff();
        updateKnockback();

        walking = false;
        if (stunTimer > 0) {
            updateStun();
        } else {
            updateMovement();
            updateAttack();
        }

        updateGravity();
        updateAnimation();
        checkPosition();
    }

    public void draw(Graphics g) {
        Image image = getCurrentImage();
        if (image != null) {
            int drawX = x + width / 2 - SPRITE_SIZE / 2;
            int drawY = y + height - SPRITE_SIZE;
            int drawW = image.getWidth(null) * SPRITE_SIZE / SPRITE_PIXELS;   // attack.png is wider than 64px

            if (facingDirection == 1) {
                g.drawImage(image, drawX, drawY, drawW, SPRITE_SIZE, null);
            } else {
                g.drawImage(image, drawX + SPRITE_SIZE, drawY, -drawW, SPRITE_SIZE, null);
            }
        }

        g.setColor(color);
        g.drawRect(x, y, width, height);

        if (isLongRange()) return;
        if (attacking) {
            g.setColor(getAttackColor());
            int attackX = (facingDirection == 1) ? x + width : x - getFinalAttackRange();
            g.drawRect(attackX, y + 60, getFinalAttackRange(), 120);
        }
    }

    public void getStunned(int duration) {
        stunTimer = duration;
    }

    public void getKnockBack(int knockback) {
        knockbackX = knockback;
    }

    public void attack() {
        if (!attacking) {
            attacking = true;
            attackTimer = 0;

            if (hasWeapon()) {
                this.pendingBullet = weapon.createBullet(this);
            }
        }
    }
    public void jump() {
        int maxJump = canDoubleJump() ? 2 : 1;
        if (jumpCount < maxJump) {
            velocityY = getJumpForce();
            onGround = false;
            jumpCount++;
        }
    }
    public void takeDamage(int damage) {
        hp -= damage;
        if (hp < 0) {
            hp = 0;
        }
    }
    public boolean isAlive() { return hp > 0; }
    public void setLeftPressed(boolean pressed) {
        this.leftPressed = pressed;
    }
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
            return new Rectangle(x + width, y + 60, getFinalAttackRange(), 120);
        } else {
            return new Rectangle(x - getFinalAttackRange(), y + 60, getFinalAttackRange(), 120);
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

    public int getSpeedBoostTimer() { return speedBoostTimer; }

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
            walking = leftPressed != rightPressed;
        }
    }
    private void updateAnimation() {
        if (walking && onGround) {
            walkTick++;
            if (walkTick >= WALK_FRAME_TICKS) {
                walkTick = 0;
                walkFrame = (walkFrame + 1) % walkFrames.length;
            }
        } else {
            walkTick = 0;
            walkFrame = 0;
        }
    }
    private Image getCurrentImage() {
        if (isStunned() && hurtImage != null) {
            return hurtImage;
        }
        if (attacking) {
            Image image = hasWeapon()
                    ? Assets.loadImage(spritePath + "attack_" + weapon.getName().toLowerCase() + ".png")
                    : attackImage;
            if (image != null) {
                return image;
            }
        }
        if (walking && onGround) {
            return walkFrames[walkFrame];
        }
        return idleImage;
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
