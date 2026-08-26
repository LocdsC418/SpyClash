package src;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Font;

public abstract class Item {

    private int x;
    private int y;
    private int size = 40;
    private boolean collected = false;

    private int effectTimer = 0;
    private Fighter appliedTo = null;

    public Item(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract String getName();
    public abstract Color getColor();
    public abstract int getEffectDuration();
    public abstract void applyEffect(Fighter f);
    public abstract void removeEffect(Fighter f);

    public void draw(Graphics g) {
        if (collected) return;

        g.setColor(getColor());
        g.fillRect(x, y, size, size);

        g.setColor(Color.WHITE);
        g.drawRect(x, y, size, size);

        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString(getName(), x, y - 8);
    }

    public Rectangle getHitbox() {
        return new Rectangle(x, y, size, size);
    }

    public void collect(Fighter f) {
        if (collected) return;
        collected = true;
        appliedTo = f;
        effectTimer = getEffectDuration();
        applyEffect(f);
    }

    public void update() {
        if (appliedTo != null && effectTimer > 0) {
            effectTimer--;
            if (effectTimer <= 0) {
                removeEffect(appliedTo);
                appliedTo = null;
            }
        }
    }

    public boolean isCollected() { return collected; }
    public boolean isExpired() { return collected && effectTimer <= 0; }
    public int getEffectTimer() { return effectTimer; }
    public Fighter getAppliedTo() { return appliedTo; }

}
