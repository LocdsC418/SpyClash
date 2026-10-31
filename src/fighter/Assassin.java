package src.fighter;

import java.awt.Color;

public class Assassin extends Fighter {

    public Assassin(int startX, Color color) {
        super(startX, color, 100, "assassin");
    }

    @Override
    public int getSpeed() { return 6; }

    @Override
    public int getJumpForce() { return -24; }

    @Override
    public int getAttackRange() { return 67; }

    @Override
    public int getAttackDamage() { return 7; }

    @Override
    public int getAttackDuration() { return 10; }

    @Override
    public String getName() { return "Assassin"; }

    @Override
    public boolean canDoubleJump() { return true; }
}
