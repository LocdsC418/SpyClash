package src;

import java.awt.Color;

public class HealItem extends Buff {

    public HealItem(int x, int y) {
        super(x, y);
    }

    @Override
    public String getName() { return "HEAL"; }

    @Override
    public Color getColor() { return Color.GREEN; }

    @Override
    public int getEffectDuration() { return 1; }

    @Override
    public int getAmount() { return 40; }

    @Override
    public void applyTo(Fighter f) {
        f.heal(getAmount());
    }
}
