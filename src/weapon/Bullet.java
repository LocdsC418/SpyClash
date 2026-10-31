package src.weapon;

import src.fighter.Fighter;

import java.awt.*;

public class Bullet {
    private static final int MUZZLE_OFFSET_X = 75;
    private static final int MUZZLE_HEIGHT = 140;

    private Fighter owner;
    private int x;
    private int y;
    private int width = 30;
    private int height = 30;
    private int direction;
    private int speed = 20;
    private int damage;
    private int distanceTraveled = 0;
    private int maxRange;
    private boolean active = true;

    public Bullet(Fighter owner) {
        this.owner = owner;
        this.direction = owner.getFacingDirection();
        this.damage = owner.getTotalDamage();
        this.maxRange = 2000;

        if (direction == 1) {
            this.x = owner.getX() + owner.getWidth() + MUZZLE_OFFSET_X;
        } else {
            this.x = owner.getX() - MUZZLE_OFFSET_X - width;
        }

        this.y = owner.getY() + owner.getHeight() - MUZZLE_HEIGHT;
    }

    public void update() {
        if (!active) return;

        x += speed*direction;
        distanceTraveled += speed;
        if (distanceTraveled >= maxRange ) {
            active = false;
        }
    }

    public void draw(Graphics g) {
        if (!active) return;

        g.setColor(Color.GRAY);
        g.fillRect(x, y, width, height);
    }

    public Rectangle getHitbox() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isActive() {
        return active;
    }

    public int getDamage() {
        return damage;
    }

    public Fighter getOwner() {
        return owner;
    }

    public int getDirection() {
        return direction;
    }

    public void deactivate() {
        this.active = false;
    }
}
