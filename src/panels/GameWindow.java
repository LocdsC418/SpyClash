package src.panels;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindow extends JFrame implements ActionListener {

    private MenuPanel menuPanel = new MenuPanel();
    private CharacterSelection selectPanel = new CharacterSelection();
    private GamePanel gamePanel = new GamePanel();

    public GameWindow() {
        super("Fighting Game");

        menuPanel.getPvPButton().addActionListener(this);
        menuPanel.getQuitButton().addActionListener(this);
        selectPanel.getStartButton().addActionListener(this);
        selectPanel.getBackButton().addActionListener(this);
        gamePanel.getPlayAgainButton().addActionListener(this);
        gamePanel.getMenuButton().addActionListener(this);
        gamePanel.getPauseButton().addActionListener(this);
        gamePanel.getResumeButton().addActionListener(this);

        this.add(menuPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == menuPanel.getPvPButton()) {
            selectPanel.reset();
            showPanel(selectPanel);
        } else if (e.getSource() == menuPanel.getQuitButton()) {
            System.exit(0);
        } else if (e.getSource() == selectPanel.getStartButton()) {
            gamePanel.startGame(selectPanel.getP1Choice(), selectPanel.getP2Choice(), selectPanel.getMapChoice());
            showPanel(gamePanel);
        } else if (e.getSource() == selectPanel.getBackButton()) {
            showPanel(menuPanel);
        } else if (e.getSource() == gamePanel.getPlayAgainButton()) {
            selectPanel.reset();
            showPanel(selectPanel);
        } else if (e.getSource() == gamePanel.getMenuButton()) {
            showPanel(menuPanel);
        } else if (e.getSource() == gamePanel.getPauseButton()) {
            gamePanel.pause();
        } else if (e.getSource() == gamePanel.getResumeButton()) {
            gamePanel.resume();
        }
    }

    private void showPanel(JPanel panel) {
        this.getContentPane().removeAll();
        this.add(panel);
        this.revalidate();
        this.repaint();
        panel.requestFocusInWindow();
    }
}
