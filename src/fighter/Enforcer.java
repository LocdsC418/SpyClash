package src.fighter;

import java.awt.Color;

public class Enforcer extends Fighter {

    public Enforcer(int startX, Color color) {
        super(startX, color, 120, "enforcer");
    }

    @Override
    public int getSpeed() { return 4; }

    @Override
    public int getJumpForce() { return -25; }

    @Override
    public int getAttackRange() { return 120; }

    @Override
    public int getAttackDamage() { return 15; }

    @Override
    public int getAttackDuration() { return 20; }

    @Override
    public String getName() { return "Enforcer"; }

    @Override
    public boolean canDoubleJump() { return true; }
}
