import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;

public class GamePanel extends JPanel {

    final int WIDTH = 800;
    final int HEIGHT = 500;

    int playerX = 100;
    int playerY = 350;

    public GamePanel(){
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.RED);
        g.fillRect(playerX, playerY, 50, 100);
    }
}