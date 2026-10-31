package src.panels;

import src.util.Assets;
import src.util.GameConfig;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {

    private Image bgMenu;
    private JButton btnPvP;
    private JButton btnQuit;

    public MenuPanel() {
        this.setPreferredSize(GameConfig.SCREEN_SIZE);
        this.setBackground(Color.BLACK);
        this.setLayout(null);

        bgMenu = Assets.loadImage("/res/menu/menu_bg.jpg");

        final int width = 300;
        final int height = 80;
        final int gap = 20;
        final int startY = 320;

        int x = (GameConfig.WINDOW_WIDTH - width) / 2;

        btnPvP = new JButton("Player vs Player");
        btnPvP.setFont(new Font("Arial", Font.BOLD, 24));
        btnPvP.setFocusPainted(false);
        btnPvP.setBounds(x, startY, width, height);
        this.add(btnPvP);

        btnQuit = new JButton("Quit");
        btnQuit.setFont(new Font("Arial", Font.BOLD, 24));
        btnQuit.setFocusPainted(false);
        btnQuit.setBounds(x, startY + (height + gap), width, height);
        this.add(btnQuit);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bgMenu != null) {
            g.drawImage(bgMenu, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public JButton getPvPButton() { return btnPvP; }
    public JButton getQuitButton() { return btnQuit; }
}
