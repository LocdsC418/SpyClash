import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
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

    HealthBar hpBar1;
    HealthBar hpBar2;

    boolean gameOver = false;
    String winnerText = "";

    Timer timer;

    public GamePanel(){
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);

        player1 = new Fighter(100, Color.RED);
        player2 = new Fighter(600, Color.BLUE);

        hpBar1 = new HealthBar(20, 40);
        hpBar2 = new HealthBar(580, 40);

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

        hpBar1.draw(g, player1.getHP(), player1.getMaxHP(), "Player 1 [Arrow + /]");
        hpBar2.draw(g, player2.getHP(), player2.getMaxHP(), "Player 2 [WASD + F]");

        if (gameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 40));

            int textWidth = g.getFontMetrics().stringWidth(winnerText);
            g.drawString(winnerText, (WIDTH - textWidth) / 2, 250);

            g.setFont(new Font("Arial", Font.PLAIN, 20));
            String restart = "Press R to restart";
            int restartWidth = g.getFontMetrics().stringWidth(restart);
            g.drawString(restart, (WIDTH - restartWidth) / 2, 290);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (gameOver) {
            return;
        }
        player1.update();
        player2.update();

        Rectangle attack1 = player1.getAttackHitbox();
        if (attack1 != null && attack1.intersects(player2.getBodyHitbox())) {
            player2.takeDamage(player1.getAttackDamage());
        }

        Rectangle attack2 = player2.getAttackHitbox();
        if (attack2 != null && attack2.intersects(player1.getBodyHitbox())) {
            player1.takeDamage(player2.getAttackDamage());
        }

        if (!player1.isAlive()) {
            gameOver = true;
            winnerText = "Player 2 wins!";
        }
        if (!player2.isAlive()) {
            gameOver = true;
            winnerText = "Player 1 wins!";
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
            player1 = new Fighter(100, Color.RED);
            player2 = new Fighter(600, Color.BLUE);
            gameOver = false;
            winnerText = "";
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            player1.setLeftPressed(true);
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            player1.setRightPressed(true);
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            player1.jump();
        }
        if (e.getKeyCode() == KeyEvent.VK_SLASH) {
            player1.attack();
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
        if (e.getKeyCode() == KeyEvent.VK_F) {
            player2.attack();
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