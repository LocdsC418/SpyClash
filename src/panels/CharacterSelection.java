package src.panels;

import src.util.Assets;
import src.util.GameConfig;

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
        this.setPreferredSize(GameConfig.SCREEN_SIZE);
        this.setBackground(Color.BLACK);
        this.setLayout(null);

        for (int i = 0; i < NAMES.length; i++) {
            fighterImages[i] = Assets.loadImage(IMAGE_PATHS[i]);
        }

        p1Box = createPickBox(P1_CENTER_X, NAMES);
        p2Box = createPickBox(P2_CENTER_X, NAMES);
        mapBox = createPickBox(GameConfig.WINDOW_WIDTH / 2, MAPS);

        final int navWidth = 200;
        final int navHeight = 55;
        final int navGap = 20;
        final int navY = 640;

        int navX = (GameConfig.WINDOW_WIDTH - (navWidth * 2 + navGap)) / 2;

        btnBack = new JButton("BACK");
        btnBack.setFont(new Font("Arial", Font.BOLD, 24));
        btnBack.setFocusPainted(false);
        btnBack.setBounds(navX, navY, navWidth, navHeight);
        this.add(btnBack);

        btnStart = new JButton("START");
        btnStart.setFont(new Font("Arial", Font.BOLD, 24));
        btnStart.setFocusPainted(false);
        btnStart.setBounds(navX + navWidth + navGap, navY, navWidth, navHeight);
        this.add(btnStart);

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
        final int width = 140;
        final int height = 280;
        final int y = 255;

        int x = centerX - width / 2;
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, width, height);

        if (fighterImages[choice] != null) {
            g.drawImage(fighterImages[choice], centerX - height / 2, y, height, height, this);
        } else {
            g.setColor(PREVIEW_COLORS[choice]);
            g.fillRect(x, y, width, height);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 28));
        drawCentered(g, NAMES[choice], centerX, y + height + 40);

        g.setFont(new Font("Arial", Font.PLAIN, 18));
        drawCentered(g, STATS[choice], centerX, y + height + 70);
    }

    private void drawCentered(Graphics g, String text, int centerX, int y) {
        int textWidth = g.getFontMetrics().stringWidth(text);
        g.drawString(text, centerX - textWidth / 2, y);
    }

    private JComboBox<String> createPickBox(int centerX, String[] init) {
        final int width = 260;
        final int height = 45;
        final int y = 180;

        JComboBox<String> box = new JComboBox<>(init);
        box.setFont(new Font("Arial", Font.BOLD, 22));
        box.setBounds(centerX - width / 2, y, width, height);
        box.addActionListener(this);
        this.add(box);
        return box;
    }
}
