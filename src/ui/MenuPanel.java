package src.ui;

import src.core.Assets;
import src.core.GameConfig;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {

    private static final int BTN_W = 300;
    private static final int BTN_H = 80;
    private static final int BTN_GAP = 20;
    private static final int BTN_START_Y = 320;

    private Image bgMenu;
    private JButton btnPvP;
    private JButton btnQuit;

    public MenuPanel() {
        this.setPreferredSize(GameConfig.SCREEN_SIZE);   // size the window uses when pack() is called
        this.setBackground(Color.BLACK);                 // colour behind everything that is drawn
        this.setLayout(null);                            // no layout manager, buttons are placed with setBounds

        bgMenu = Assets.loadImage("/res/menu/menu_bg.jpg");

        btnPvP = createMenuButton("Player vs Player");
        btnQuit = createMenuButton("Quit");

        int x = (GameConfig.WINDOW_WIDTH - BTN_W) / 2;
        btnPvP.setBounds(x, BTN_START_Y, BTN_W, BTN_H);                       // x, y, width, height on the panel
        btnQuit.setBounds(x, BTN_START_Y + (BTN_H + BTN_GAP), BTN_W, BTN_H);

        this.add(btnPvP);   // add() puts the button on the panel so it shows up
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

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setFocusPainted(false);   // no focus rectangle around the label after clicking
        return button;
    }
}
