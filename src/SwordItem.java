package src;

import src.weapon.Sword;
import src.weapon.Weapon;

import java.awt.Color;

public class SwordItem extends WeaponItem {

    public SwordItem(int x, int y) {
        super(x, y);
    }

    @Override
    public Weapon createWeapon() { return new Sword(); }
    @Override
    public String getName() { return "Sword"; }
    @Override
    public Color getColor() { return Color.GRAY; }
}
