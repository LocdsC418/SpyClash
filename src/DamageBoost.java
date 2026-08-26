package src;

import java.awt.Color;

public class DamageBoost extends Item {

    public DamageBoost(int x, int y) {
        super(x, y);
    }

    @Override
    public String getName() { return "DMG"; }

    @Override
    public Color getColor() { return Color.ORANGE; }

    @Override
    public int getEffectDuration() { return 300; }

    @Override
    public void applyEffect(Fighter f) {
        f.setDamageBoost(10);
    }

    @Override
    public void removeEffect(Fighter f) {
        f.setDamageBoost(0);
    }
}
