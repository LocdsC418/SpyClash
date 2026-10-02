package src.ui;

import src.core.GameConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.function.BiConsumer;

public class CharacterSelection extends JPanel implements KeyListener {

    private int p1Choice = 0;
    private int p2Choice = 1;
    private boolean p1Ready = false;
    private boolean p2Ready = false;
    private BiConsumer<Integer, Integer> onConfirm;
    public CharacterSelection(BiConsumer<Integer, Integer> onConfirm) {
        this.onConfirm = onConfirm;
        this.setPreferredSize(GameConfig.SCREEN_SIZE);
        this.setFocusable(true);
        this.addKeyListener(this);
        this.setBackground(Color.BLACK);
    }

    public void reset() {
        p1Ready = false;
        p2Ready = false;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.scale((double) getWidth() / GameConfig.WIDTH, (double) getHeight() / GameConfig.HEIGHT);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 60));
        String title = "SELECT YOUR FIGHTER";
        int titleWidth = g.getFontMetrics().stringWidth(title);
        g.drawString(title, (GameConfig.WIDTH - titleWidth) / 2, 120);

        String[] names = {"Assassin", "Enforcer"};
        String[] stats = {
                "SPD:7  DMG:7 HP:80 JMP:High",
                "SPD:4  DMG:15 HP:120 JMP:Low"
        };

        for (int i = 0; i <2 ; i++) {
            int boxX = 360 + i * 670;
            int boxY = 200;
            int boxW = 480;
            int boxH = 500;

            g.setColor(Color.DARK_GRAY);
            g.fillRect(boxX, boxY, boxW, boxH);

            Color previewColor = (i == 0) ? Color.GREEN : Color.ORANGE;
            g.setColor(previewColor);
            g.fillRect(boxX + 165, boxY + 60, 150, 250);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 44));
            int nameWidth = g.getFontMetrics().stringWidth(names[i]);
            g.drawString(names[i], boxX + (boxW - nameWidth) / 2, boxY + 370);

            g.setFont(new Font("Arial", Font.PLAIN, 24));
            int statWidth = g.getFontMetrics().stringWidth(stats[i]);
            g.drawString(stats[i], boxX + (boxW - statWidth) / 2, boxY + 410);

            if (p1Choice == i) {
                g.setColor(Color.RED);
                g.setFont(new Font("Arial", Font.BOLD, 32));
                String p1Label = p1Ready ? "P1 READY!" : "> P1! <";
                int labelW = g.getFontMetrics().stringWidth(p1Label);
                g.drawString(p1Label, boxX + (boxW - labelW) / 2, boxY + 450);
            }
            if (p2Choice == i) {
                g.setColor(Color.BLUE);
                g.setFont(new Font("Arial", Font.BOLD, 32));
                String p2Label = p2Ready ? "P2 READY!" : "> P2! <";
                int labelW = g.getFontMetrics().stringWidth(p2Label);
                g.drawString(p2Label, boxX + (boxW - labelW) / 2, boxY + 490);
            }
        }

        g.setColor(Color.GRAY);
        g.setFont(new Font("Arial", Font.PLAIN, 28));
        String help1 = "P1: A/D to choose, F to confirm";
        String help2 = "P2: Arrow Left/Right to choose, ENTER to confirm";
        int h1Width = g.getFontMetrics().stringWidth(help1);
        int h2Width = g.getFontMetrics().stringWidth(help2);
        g.drawString(help1, (GameConfig.WIDTH - h1Width) / 2, 850);
        g.drawString(help2, (GameConfig.WIDTH - h2Width) / 2, 900);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!p1Ready) {
            if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_D) {
                p1Choice = (p1Choice == 0) ? 1 : 0;
            }
        }

        if (!p2Ready) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                p2Choice = (p2Choice == 0) ? 1 : 0;
            }
        }

        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!p1Ready) {
            if (e.getKeyCode() == KeyEvent.VK_F) {
                p1Ready = true;
            }
        }
        if (!p2Ready) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                p2Ready = true;
            }
        }
        if (p1Ready && p2Ready) {
            onConfirm.accept(p1Choice, p2Choice);
        }

        repaint();
    }

}
