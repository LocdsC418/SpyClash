package src;

import java.awt.Color;

public class DamageBoost extends Buff {

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
    public int getAmount() { return 10; }

    @Override
    public void applyTo(Fighter f) { f.addDamageBoost(getEffectDuration(), getAmount()); }
}
