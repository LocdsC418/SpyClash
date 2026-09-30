package src.weapon;

import java.awt.Color;

public class Sword extends Weapon {

    public Sword() {
        super(10);
    }

    @Override
    public String getName() { return "Sword"; }
    @Override
    public int getDamage() { return 25; }
    @Override
    public int getRange() { return 180; }
    @Override
    public int getAttackDuration() { return 30; }
    @Override
    public Color getColor() { return Color.GRAY; }
}
