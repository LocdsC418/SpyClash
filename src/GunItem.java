package src;

import java.awt.Color;

public class GunItem extends WeaponItem {

    public GunItem(int x, int y) {
        super(x, y);
    }

    @Override
    public Weapon createWeapon() { return new Gun(); }
    @Override
    public String getName() { return "Gun"; }
    @Override
    public Color getColor() { return Color.GRAY; }
}
