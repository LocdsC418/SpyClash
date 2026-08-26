package src;

import java.awt.Color;

public class SpeedBoost extends Item {

    private int originalSpeed = 0;

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
        originalSpeed = f.getSpeed();
        f.setSpeedBoost(5);
    }

    @Override
    public void removeEffect(Fighter f) {
        f.setSpeedBoost(0);
    }
}
