import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GamePanel extends JPanel implements KeyListener, ActionListener {

    final int WIDTH = 800;
    final int HEIGHT = 500;
    final int GROUND = 350;

    Fighter player1;
    Fighter player2;

    Timer timer;

    public GamePanel(){
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);

        player1 = new Fighter(100, Color.RED);
        player2 = new Fighter(600, Color.BLUE);

        timer = new Timer(16, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, GROUND + 100, WIDTH, HEIGHT);

        player1.draw(g);
        player2.draw(g);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        player1.update();
        player2.update();

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            player1.setLeftPressed(true);
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            player1.setRightPressed(true);
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            player1.jump();
        }

        if (e.getKeyCode() == KeyEvent.VK_A) {
            player2.setLeftPressed(true);
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            player2.setRightPressed(true);
        }
        if (e.getKeyCode() == KeyEvent.VK_W) {
            player2.jump();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            player1.setLeftPressed(false);
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            player1.setRightPressed(false);
        }

        if (e.getKeyCode() == KeyEvent.VK_A) {
            player2. setLeftPressed(false);
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            player2.setRightPressed(false);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}