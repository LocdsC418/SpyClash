import java.awt.Color;

public class Boxer extends Fighter {

    public Boxer(int startX, Color color) {
        super(startX, color, 120);
    }

    @Override
    public int getSpeed() { return 4; }

    @Override
    public int getJumpForce() { return -12; }

    @Override
    public int getAttackRange() { return 70; }

    @Override
    public int getAttackDamage() { return 15; }

    @Override
    public int getAttackDuration() { return 20; }

    @Override
    public String getName() { return "Boxer"; }
}
