package src.weapon;

import src.Fighter;

import java.awt.Color;

public abstract class Weapon {
    private int useLeft;

    public Weapon(int uses) {
        this.useLeft = uses;
    }

    public abstract String getName();
    public abstract int getDamage();
    public abstract int getRange();
    public abstract int getAttackDuration();
    public abstract Color getColor();

    public Bullet createBullet(Fighter owner) {
        return null;
    }

    public boolean isMelee() {
        return true;
    }

    public void used() { this.useLeft--; }
    public boolean isBroken() { return this.useLeft <= 0; }
    public int getUseLeft() { return this.useLeft; }
}
