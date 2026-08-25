package src;

import javax.swing.JFrame;

public class Main{

    public static void main(String[] args){
        JFrame window = new JFrame("Fighting Game");
        GamePanel panel = new GamePanel();

        window.add(panel);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}