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

    public Item(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract String getName();
    public abstract Color getColor();
    public abstract int getEffectDuration();
    public abstract void applyEffect(Fighter f);

    public void removeEffect(Fighter f){}

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
        if (collected) {return;}
        collected = true;
        applyEffect(f);
    }


    public boolean isCollected() { return collected; }

}
