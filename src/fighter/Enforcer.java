package src.fighter;

import java.awt.Color;

public class Enforcer extends Fighter {

    public Enforcer(int startX, Color color) {
        super(startX, color, 120, "enforcer");
    }

    @Override
    public int getSpeed() { return 5; }

    @Override
    public int getJumpForce() { return -22; }

    @Override
    public int getAttackRange() { return 80; }

    @Override
    public int getAttackDamage() { return 11; }

    @Override
    public int getAttackDuration() { return 15; }

    @Override
    public String getName() { return "Enforcer"; }

    @Override
    public boolean canDoubleJump() { return true; }
}
