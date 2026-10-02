package src;

import src.ui.CharacterSelection;
import src.ui.GamePanel;
import src.ui.MenuPanel;

import javax.swing.*;
import java.awt.*;
import java.nio.channels.SelectableChannel;
import java.util.function.BiConsumer;

public class Main{

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame window = new JFrame("Fighting Game");
                CardLayout cards = new CardLayout();
                JPanel cardPanel = new JPanel();
                cardPanel.setLayout(cards);

                GamePanel gamePanel = new GamePanel();

                CharacterSelection selectPanel = new CharacterSelection(new BiConsumer<Integer, Integer>() {
                    @Override
                    public void accept(Integer p1, Integer p2) {
                        gamePanel.startGame(p1, p2);
                        cards.show(cardPanel, "gamePanel");
                        gamePanel.requestFocusInWindow();

                    }
                });


                gamePanel.setOnRestart(new Runnable() {
                    @Override
                    public void run() {
                        selectPanel.reset();
                        cards.show(cardPanel, "selectPanel");
                        selectPanel.requestFocusInWindow();
                    }
                });
                Runnable startPvP = new Runnable() {
                    @Override
                    public void run() {
                        cards.show(cardPanel, "selectPanel");
                        selectPanel.requestFocusInWindow();
                    }
                };
                Runnable startPvC = new Runnable() {
                    @Override
                    public void run() {

                    }
                };
                MenuPanel menuPanel = new MenuPanel(startPvP, startPvC);


                cardPanel.add(menuPanel,"menuPanel");
                cardPanel.add(selectPanel, "selectPanel");
                cardPanel.add(gamePanel, "gamePanel");

                window.add(cardPanel);
                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                window.setResizable(true);
                window.pack();
                window.setLocationRelativeTo(null);
                window.setVisible(true);
            }
        });
    }
}