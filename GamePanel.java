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
    final int GRAVITY = 1;
    final int JUMP_FORCE = -15;

    int playerX = 100;
    int playerY = GROUND;
    int speed = 5;
    int velocityY = 0;
    boolean onGround = true;

    boolean leftPressed = false;
    boolean rightPressed = false;

    Timer timer;

    public GamePanel(){
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);

        timer = new Timer(16, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, GROUND + 100, WIDTH, HEIGHT);

        g.setColor(Color.RED);
        g.fillRect(playerX, playerY, 50, 100);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (leftPressed) { playerX -= speed; }
        if (rightPressed) { playerX += speed; }
        if (playerX < 0) { playerX = 0; }
        if (playerX > WIDTH - 50) { playerX = WIDTH - 50; }

        if (!onGround) {
            velocityY += GRAVITY;
            playerY += velocityY;

            if (playerY >= GROUND) {
                playerY = GROUND;
                velocityY = 0;
                onGround = true;
            }
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            leftPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP && onGround) {
            velocityY = JUMP_FORCE;
            onGround = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}