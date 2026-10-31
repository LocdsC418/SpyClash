package src.ui;

import src.core.Assets;
import src.core.GameConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CharacterSelection extends JPanel implements ActionListener {

    private static final String[] NAMES = {"Assassin", "Enforcer"};
    private static final String[] STATS = {
            "SPD:7  DMG:7  HP:80  JMP:High",
            "SPD:4  DMG:15  HP:120  JMP:Low"
    };
    private static final String[] MAPS = {"Rooftop Night", "Rooftop Morning"};
    private static final String[] IMAGE_PATHS = {"/res/fighter/assassin/idle.png", "/res/fighter/enforcer/idle.png"};
    private static final Color[] PREVIEW_COLORS = {Color.GREEN, Color.ORANGE};

    private static final int P1_CENTER_X = GameConfig.WINDOW_WIDTH / 4;
    private static final int P2_CENTER_X = GameConfig.WINDOW_WIDTH * 3 / 4;
    private static final int PICK_W = 260;
    private static final int PICK_H = 45;
    private static final int PICK_Y = 180;
    private static final int PREVIEW_W = 140;
    private static final int PREVIEW_H = 280;
    private static final int PREVIEW_Y = 255;
    private static final int NAV_W = 200;
    private static final int NAV_H = 55;
    private static final int NAV_GAP = 20;
    private static final int NAV_Y = 640;

    private Image[] fighterImages = new Image[NAMES.length];

    private JComboBox<String> mapBox;
    private JComboBox<String> p1Box;
    private JComboBox<String> p2Box;
    private JButton btnStart;
    private JButton btnBack;

    private int p1Choice = 0;
    private int p2Choice = 1;
    private int mapChoice = 0;

    public CharacterSelection() {
        this.setPreferredSize(GameConfig.SCREEN_SIZE);   // size the window uses when pack() is called
        this.setBackground(Color.BLACK);                 // colour behind everything that is drawn
        this.setLayout(null);                            // no layout manager, components are placed with setBounds

        for (int i = 0; i < NAMES.length; i++) {
            fighterImages[i] = Assets.loadImage(IMAGE_PATHS[i]);
        }

        p1Box = createPickBox(P1_CENTER_X, NAMES);
        p2Box = createPickBox(P2_CENTER_X, NAMES);
        mapBox = createPickBox(GameConfig.WINDOW_WIDTH / 2, MAPS);

        int navX = (GameConfig.WINDOW_WIDTH - (NAV_W * 2 + NAV_GAP)) / 2;
        btnBack = createNavButton("BACK", navX);
        btnStart = createNavButton("START", navX + NAV_W + NAV_GAP);

        reset();
    }

    public void reset() {
        mapBox.setSelectedIndex(0);
        p1Box.setSelectedIndex(0);
        p2Box.setSelectedIndex(1);
        repaint();
    }

    public JButton getStartButton() { return btnStart; }
    public JButton getBackButton() { return btnBack; }
    public int getP1Choice() { return p1Choice; }
    public int getP2Choice() { return p2Choice; }
    public int getMapChoice() { return mapChoice; }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == p1Box) {
            p1Choice = p1Box.getSelectedIndex();
        } else if (e.getSource() == p2Box) {
            p2Choice = p2Box.getSelectedIndex();
        } else if (e.getSource() == mapBox) {
            mapChoice = mapBox.getSelectedIndex();
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 44));
        drawCentered(g, "SELECT YOUR FIGHTER", GameConfig.WINDOW_WIDTH / 2, 80);

        g.setFont(new Font("Arial", Font.BOLD, 32));
        g.setColor(Color.RED);
        drawCentered(g, "PLAYER 1", P1_CENTER_X, 150);
        g.setColor(Color.CYAN);
        drawCentered(g, "PLAYER 2", P2_CENTER_X, 150);
        g.setColor(Color.WHITE);
        drawCentered(g, "CHOOSE MAP", GameConfig.WINDOW_WIDTH / 2, 150);

        drawPreview(g, p1Choice, P1_CENTER_X);
        drawPreview(g, p2Choice, P2_CENTER_X);
    }

    private void drawPreview(Graphics g, int choice, int centerX) {
        int x = centerX - PREVIEW_W / 2;
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, PREVIEW_Y, PREVIEW_W, PREVIEW_H);

        if (fighterImages[choice] != null) {
            g.drawImage(fighterImages[choice], centerX - PREVIEW_H / 2, PREVIEW_Y, PREVIEW_H, PREVIEW_H, this);
        } else {
            g.setColor(PREVIEW_COLORS[choice]);
            g.fillRect(x, PREVIEW_Y, PREVIEW_W, PREVIEW_H);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 28));
        drawCentered(g, NAMES[choice], centerX, PREVIEW_Y + PREVIEW_H + 40);

        g.setFont(new Font("Arial", Font.PLAIN, 18));
        drawCentered(g, STATS[choice], centerX, PREVIEW_Y + PREVIEW_H + 70);
    }

    private void drawCentered(Graphics g, String text, int centerX, int y) {
        int textWidth = g.getFontMetrics().stringWidth(text);
        g.drawString(text, centerX - textWidth / 2, y);
    }

    private JComboBox<String> createPickBox(int centerX, String[] init) {
        JComboBox<String> box = new JComboBox<>(init);
        box.setFont(new Font("Arial", Font.BOLD, 22));
        box.setBounds(centerX - PICK_W / 2, PICK_Y, PICK_W, PICK_H);   // x, y, width, height on the panel
        box.addActionListener(this);                                   // actionPerformed() runs when the choice changes
        this.add(box);
        return box;
    }

    private JButton createNavButton(String text, int x) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setFocusPainted(false);   // no focus rectangle around the label after clicking
        button.setBounds(x, NAV_Y, NAV_W, NAV_H);
        this.add(button);
        return button;
    }
}
