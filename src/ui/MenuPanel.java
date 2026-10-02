package src.ui;

import src.core.GameConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MenuPanel extends JPanel implements KeyListener {

    private final String[] options = {"Player vs Player", "Player vs Com", "Quit"};

    private Image bgMenu;
    private Runnable onPvP;
    private Runnable onPvC;

    private int selected = 0;

    public MenuPanel(Runnable onPvP, Runnable onPvC) {
        this.onPvP = onPvP;
        this.onPvC = onPvC;
        this.setPreferredSize(GameConfig.SCREEN_SIZE);
        this.setFocusable(true);
        this.addKeyListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.scale((double) getWidth() / GameConfig.WIDTH, (double) getHeight() / GameConfig.HEIGHT);

        int w = 400, h = 90, gap = 30;
        int x = (GameConfig.WIDTH / 2 ) - (w / 2);
        g2.setFont(new Font("Arial", Font.BOLD, 32));

        for (int i = 0; i<options.length ; i++) {
            int y = 480 + i * (h + gap);

            g2.setColor(i == selected ? Color.YELLOW : Color.WHITE);
            g2.fillRoundRect(x, y, w, h, 40, 40);
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(4));
            g2.drawRoundRect(x, y, w, h, 40, 40);

            int textWidth = g2.getFontMetrics().stringWidth(options[i]);
            g2.drawString(options[i], x + (w - textWidth) / 2, y + 58);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
            selected = (selected - 1 + options.length) % options.length;
        } else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
            selected = (selected + 1) % options.length;
        }

        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            if (selected == 0) onPvP.run();
            else if (selected == 1) onPvC.run();
            else System.exit(0);
        }

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

}
