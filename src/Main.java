package src;

import src.ui.GamePanel;
import src.ui.MenuPanel;

import javax.swing.*;
import java.awt.*;

public class Main{

    public static void main(String[] args){
        JFrame window = new JFrame("Fighting Game");
        CardLayout cards = new CardLayout();
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(cards);

        GamePanel gamePanel = new GamePanel();

        Runnable startPvP = new Runnable() {
            @Override
            public void run() {
                cards.show(cardPanel, "gamePanel");
                gamePanel.requestFocusInWindow();
            }
        };
        Runnable startPvC = new Runnable() {
            @Override
            public void run() {

            }
        };

        MenuPanel menuPanel = new MenuPanel(startPvP, startPvC);

        cardPanel.add(menuPanel,"menuPanel");
        cardPanel.add(gamePanel, "gamePanel");

        window.add(cardPanel);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);


    }
}