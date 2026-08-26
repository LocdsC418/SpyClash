package src;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Graphics2D;

public class GamePanel extends JPanel implements KeyListener, ActionListener {

    final int WIDTH = 800;
    final int HEIGHT = 500;
    final int GROUND = 350;

    int freezeTimer = 0;
    final int FREEZE_DURATION = 6;

    Fighter player1;
    Fighter player2;

    HealthBar hpBar1;
    HealthBar hpBar2;

    boolean gameOver = false;
    String winnerText = "";

    boolean selectingCharacter = true;
    int p1Choice = 0;
    int p2Choice = 1;
    boolean p1Ready = false;
    boolean p2Ready = false;

    Timer timer;

    public GamePanel(){
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);

//        player1 = new Ninja(100, Color.RED);
//        player2 = new Boxer(600, Color.BLUE);
//
//        hpBar1 = new HealthBar(20, 40);
//        hpBar2 = new HealthBar(580, 40);

        timer = new Timer(16, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (selectingCharacter) {
            repaint();
            return;
        }

        if (gameOver) {
            return;
        }

        if (freezeTimer > 0) {
            freezeTimer--;
            repaint();
            return;
        }

        player1.update();
        player2.update();

        Rectangle attack1 = player1.getAttackHitbox();
        if (attack1 != null && attack1.intersects(player2.getBodyHitbox())) {
            player2.takeDamage(player1.getAttackDamage());
            freezeTimer = FREEZE_DURATION;

            int  dir = (player1.getX() < player2.getX()) ? 20 : -20;
            player2.getKnockBack(dir);
            player2.getStunned(10);
        }

        Rectangle attack2 = player2.getAttackHitbox();
        if (attack2 != null && attack2.intersects(player1.getBodyHitbox())) {
            player1.takeDamage(player2.getAttackDamage());
            freezeTimer = FREEZE_DURATION;

            int  dir = (player2.getX() < player1.getX()) ? 20 : -20;
            player1.getKnockBack(dir);
            player1.getStunned(10);
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
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        double scaleX = (double) getWidth() / WIDTH;
        double scaleY = (double) getHeight() / HEIGHT;
        g2d.scale(scaleX, scaleY);

        if (selectingCharacter) {
            drawCharacterSelect(g);
            return;
        }

        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, GROUND + 100, WIDTH, HEIGHT);

        player1.draw(g);
        player2.draw(g);

        hpBar1.draw(g, player1.getHP(), player1.getMaxHP(), "Player1: " + player1.getName() + "[WASD + F]");
        hpBar2.draw(g, player2.getHP(), player2.getMaxHP(), "Player2: " + player2.getName() + "[Arrow + /]");

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
    public void keyPressed(KeyEvent e) {

        if (selectingCharacter) {
            if (!p1Ready) {
                if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_D) {
                    p1Choice = (p1Choice == 0) ? 1 : 0;
                }
                if (e.getKeyCode() == KeyEvent.VK_F) {
                    p1Ready = true;
                }
            }

            if (!p2Ready) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    p2Choice = (p2Choice == 0) ? 1 : 0;
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    p2Ready = true;
                }
            }

            if (p1Ready && p2Ready) {
                startGame();
            }
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
            selectingCharacter = true;
            gameOver = false;
            p1Ready = false;
            p2Ready = false;
            return;
        }

        if (!player1.isStunned()) {
            if (e.getKeyCode() == KeyEvent.VK_A) {
                player1.setLeftPressed(true);
            }
            if (e.getKeyCode() == KeyEvent.VK_D) {
                player1.setRightPressed(true);
            }
            if (e.getKeyCode() == KeyEvent.VK_W) {
                player1.jump();
            }
            if (e.getKeyCode() == KeyEvent.VK_F) {
                player1.attack();
            }
        }

        if (!player2.isStunned()) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                player2.setLeftPressed(true);
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                player2.setRightPressed(true);
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                player2.jump();
            }
            if (e.getKeyCode() == KeyEvent.VK_SLASH) {
                player2.attack();
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (selectingCharacter) return;

        if (e.getKeyCode() == KeyEvent.VK_A) {
            player1.setLeftPressed(false);
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            player1.setRightPressed(false);
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            player2.setLeftPressed(false);
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            player2.setRightPressed(false);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    private Fighter createFighter(int choice, int startX, Color color) {
        if (choice == 0) {
            return new Ninja(startX, color);
        } else {
            return new Boxer(startX, color);
        }
    }

    private void startGame() {
        player1 = createFighter(p1Choice, 100, Color.RED);
        player2 = createFighter(p2Choice, 600, Color.BLUE);
        player2.setFacingDirection(-1);

        hpBar1 = new HealthBar(20, 40);
        hpBar2 = new HealthBar(580, 40);
        selectingCharacter = false;
        gameOver = false;
        winnerText = "";
    }

    private void drawCharacterSelect(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        String title = "SELECT YOUR FIGHTER";
        int titleWidth = g.getFontMetrics().stringWidth(title);
        g.drawString(title, (WIDTH - titleWidth) / 2, 60);

        String[] names = {"Ninja", "Boxer"};
        String[] stats = {
                "SPD:7  DMG:7 HP:80 JMP:High",
                "SPD:4  DMG:15 HP:120 JMP:Low"
        };

        for (int i = 0; i <2 ; i++) {
            int boxX = 150 + i * 280;
            int boxY = 100;
            int boxW = 200;
            int boxH = 250;

            g.setColor(Color.DARK_GRAY);
            g.fillRect(boxX, boxY, boxW, boxH);

            Color previewColor = (i == 0) ? Color.GREEN : Color.ORANGE;
            g.setColor(previewColor);
            g.fillRect(boxX + 75, boxY + 30, 50, 100);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 22));
            int nameWidth = g.getFontMetrics().stringWidth(names[i]);
            g.drawString(names[i], boxX + (boxW - nameWidth) / 2, boxY + 160);

            g.setFont(new Font("Arial", Font.PLAIN, 12));
            int statWidth = g.getFontMetrics().stringWidth(stats[i]);
            g.drawString(stats[i], boxX + (boxW - statWidth) / 2, boxY + 185);

            if (p1Choice == i) {
                g.setColor(Color.RED);
                g.setFont(new Font("Arial", Font.BOLD, 16));
                String p1Label = p1Ready ? "P1 READY!" : "> P1! <";
                int labelW = g.getFontMetrics().stringWidth(p1Label);
                g.drawString(p1Label, boxX + (boxW - labelW) / 2, boxY + 215);
            }
            if (p2Choice == i) {
                g.setColor(Color.BLUE);
                g.setFont(new Font("Arial", Font.BOLD, 16));
                String p2Label = p2Ready ? "P2 READY!" : "> P2! <";
                int labelW = g.getFontMetrics().stringWidth(p2Label);
                g.drawString(p2Label, boxX + (boxW - labelW) / 2, boxY + 240);
            }
        }

        g.setColor(Color.GRAY);
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        String help1 = "P1: A/D to choose, F to confirm";
        String help2 = "P2: Arrow Left/Right to choose, ENTER to confirm";
        int h1Width = g.getFontMetrics().stringWidth(help1);
        int h2Width = g.getFontMetrics().stringWidth(help2);
        g.drawString(help1, (WIDTH - h1Width) / 2, 410);
        g.drawString(help2, (WIDTH - h2Width) / 2, 435);
    }
}