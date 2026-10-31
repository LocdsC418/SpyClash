package src.ui;

import src.fighter.Fighter;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class ItemStatus {

    private static final int TICKS_PER_SECOND = 60;
    private static final Font FONT = new Font("Arial", Font.BOLD, 26);

    private int x;
    private int y;
    private boolean alignRight;

    public ItemStatus(int x, int y, boolean alignRight) {
        this.x = x;
        this.y = y;
        this.alignRight = alignRight;
    }

    public void draw(Graphics g, Fighter fighter) {
        String text = "";
        if (fighter.hasWeapon()) {
            text += fighter.getWeapon().getName() + " x" + fighter.getWeapon().getUseLeft();
        }
        if (fighter.getSpeedBoostTimer() > 0) {
            int seconds = (fighter.getSpeedBoostTimer() + TICKS_PER_SECOND - 1) / TICKS_PER_SECOND;
            if (!text.isEmpty()) {
                text += "    ";
            }
            text += "Speed Buff " + seconds + "s";
        }
        if (text.isEmpty()) {
            return;
        }

        g.setFont(FONT);
        int drawX = alignRight ? x - g.getFontMetrics().stringWidth(text) : x;

        g.setColor(Color.BLACK);
        g.drawString(text, drawX + 2, y + 2);
        g.setColor(Color.WHITE);
        g.drawString(text, drawX, y);
    }
}
