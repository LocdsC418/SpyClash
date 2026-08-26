package src;

import java.awt.Color;

public class Ninja extends Fighter {

    public Ninja(int startX, Color color) {
        super(startX, color, 80);
    }

    @Override
    public int getSpeed() { return 7; }

    @Override
    public int getJumpForce() { return -30; }

    @Override
    public int getAttackRange() { return 100; }

    @Override
    public int getAttackDamage() { return 7; }

    @Override
    public int getAttackDuration() { return 10; }

    @Override
    public String getName() { return "Ninja"; }

    @Override
    public boolean canDoubleJump() { return true; }
}
