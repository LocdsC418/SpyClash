package src;

import java.awt.*;

public class HealthBar {

    private int x;
    private int y;
    private int width = 400;
    private int height = 40;

    public HealthBar(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g, int currentHP, int maxHP, String name) {
        int hpWidth = (int) ((double) currentHP / maxHP * width);

        g.setColor(Color.WHITE);
        g.drawRect(x, y, width, height);

        if (currentHP > maxHP * 0.6) {
            g.setColor(Color.GREEN);
        } else if (currentHP > maxHP * 0.3) {
            g.setColor(Color.YELLOW);
        } else {
            g.setColor(Color.RED);
        }
        g.fillRect(x + 1, y + 1, hpWidth - 1, height - 1);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString(name, x, y - 10);
    }
}
