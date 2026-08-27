package src;

import java.awt.Color;

public class SpeedBoost extends Item {

    public SpeedBoost(int x, int y) {
        super(x, y);
    }

    @Override
    public String getName() { return "SPD"; }

    @Override
    public Color getColor() { return Color.CYAN; }

    @Override
    public int getEffectDuration() { return 300; }

    @Override
    public void applyEffect(Fighter f) {
        f.addSpeedBoost(getEffectDuration(), 5);
    }
}
