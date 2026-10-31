package src;

import src.ui.GameWindow;

import javax.swing.*;

public class Main{

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GameWindow window = new GameWindow();
                window.setVisible(true);
            }
        });
    }
}
