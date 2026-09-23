package src;

import java.awt.*;

public class Bullet {
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
            this.x = owner.getX() + owner.getWidth();
        } else {
            this.x = owner.getX() - width;
        }

        this.y = owner.getY() + 20;
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
