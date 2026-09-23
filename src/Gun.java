package src;

import java.awt.Color;

public class Gun extends Weapon {

    public Gun() {
        super(15);
    }

    @Override
    public String getName() { return "Gun"; }
    @Override
    public int getDamage() { return 10; }
    @Override
    public int getRange() { return 220; }
    @Override
    public int getAttackDuration() { return 30; }
    @Override
    public Color getColor() { return Color.GRAY; }

    @Override
    public Bullet createBullet(Fighter owner) {
        return new Bullet(owner);
    }
    @Override
    public boolean isMelee() {
        return false;
    }
}
