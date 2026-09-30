package src;

import src.fighter.Assassin;
import src.fighter.Enforcer;
import src.fighter.Fighter;
import src.item.*;
import src.weapon.Bullet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements KeyListener, ActionListener {

    Image bgStage;

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

    ArrayList<Bullet> bullets = new ArrayList<>();

    ArrayList<Item> items = new ArrayList<>();
    Random random = new Random();
    int spawnTimer = 0;
    final int SPAWN_INTERVAL = 300;

    public GamePanel(){
        this.setPreferredSize(new Dimension(GameConfig.WIDTH, GameConfig.HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);

        bgStage = Assets.loadImage("/res/background/stage.gif");

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

        handlePlayerCollision();
        handleItemSpawn();
        handleItemFallAndCollect();
        handleBullets();
        handleAttackCollision();
        handleWinCondition();

        repaint();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);



        Graphics2D g2d = (Graphics2D) g;
        double scaleX = (double) getWidth() / GameConfig.WIDTH;
        double scaleY = (double) getHeight() / GameConfig.HEIGHT;
        g2d.scale(scaleX, scaleY);

        if (selectingCharacter) {
            drawCharacterSelect(g);
            return;
        }

        drawArena(g);

        drawFighter(g);
        drawBullet(g);
        drawItem(g);
        drawHUD(g);

        if (gameOver) {
            drawGameOver(g);
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
            return new Assassin(startX, color);
        } else {
            return new Enforcer(startX, color);
        }
    }

    private void startGame() {

        items.clear();
        bullets.clear();
        spawnTimer = 0;

        player1 = createFighter(p1Choice, 200, Color.RED);
        player2 = createFighter(p2Choice, 1600, Color.BLUE);
        player2.setFacingDirection(-1);

        hpBar1 = new HealthBar(40, 60);
        hpBar2 = new HealthBar(1480, 60);
        selectingCharacter = false;
        gameOver = false;
        winnerText = "";

    }

    private void drawCharacterSelect(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 60));
        String title = "SELECT YOUR FIGHTER";
        int titleWidth = g.getFontMetrics().stringWidth(title);
        g.drawString(title, (GameConfig.WIDTH - titleWidth) / 2, 120);

        String[] names = {"Assassin", "Enforcer"};
        String[] stats = {
                "SPD:7  DMG:7 HP:80 JMP:High",
                "SPD:4  DMG:15 HP:120 JMP:Low"
        };

        for (int i = 0; i <2 ; i++) {
            int boxX = 360 + i * 670;
            int boxY = 200;
            int boxW = 480;
            int boxH = 500;

            g.setColor(Color.DARK_GRAY);
            g.fillRect(boxX, boxY, boxW, boxH);

            Color previewColor = (i == 0) ? Color.GREEN : Color.ORANGE;
            g.setColor(previewColor);
            g.fillRect(boxX + 165, boxY + 60, 150, 250);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 44));
            int nameWidth = g.getFontMetrics().stringWidth(names[i]);
            g.drawString(names[i], boxX + (boxW - nameWidth) / 2, boxY + 370);

            g.setFont(new Font("Arial", Font.PLAIN, 24));
            int statWidth = g.getFontMetrics().stringWidth(stats[i]);
            g.drawString(stats[i], boxX + (boxW - statWidth) / 2, boxY + 410);

            if (p1Choice == i) {
                g.setColor(Color.RED);
                g.setFont(new Font("Arial", Font.BOLD, 32));
                String p1Label = p1Ready ? "P1 READY!" : "> P1! <";
                int labelW = g.getFontMetrics().stringWidth(p1Label);
                g.drawString(p1Label, boxX + (boxW - labelW) / 2, boxY + 450);
            }
            if (p2Choice == i) {
                g.setColor(Color.BLUE);
                g.setFont(new Font("Arial", Font.BOLD, 32));
                String p2Label = p2Ready ? "P2 READY!" : "> P2! <";
                int labelW = g.getFontMetrics().stringWidth(p2Label);
                g.drawString(p2Label, boxX + (boxW - labelW) / 2, boxY + 490);
            }
        }

        g.setColor(Color.GRAY);
        g.setFont(new Font("Arial", Font.PLAIN, 28));
        String help1 = "P1: A/D to choose, F to confirm";
        String help2 = "P2: Arrow Left/Right to choose, ENTER to confirm";
        int h1Width = g.getFontMetrics().stringWidth(help1);
        int h2Width = g.getFontMetrics().stringWidth(help2);
        g.drawString(help1, (GameConfig.WIDTH - h1Width) / 2, 850);
        g.drawString(help2, (GameConfig.WIDTH - h2Width) / 2, 900);
    }
    private void drawArena(Graphics g) {
        g.drawImage(bgStage, 0, 0, GameConfig.WIDTH, GameConfig.HEIGHT, this);
    }
    private void drawFighter(Graphics g) {
        if (player1.getX() <= player2.getX()) {
            player1.draw(g);
            player2.draw(g);
        } else {
            player2.draw(g);
            player1.draw(g);
        }
    }
    private void drawItem(Graphics g) {
        for (Item item : items) {
            item.draw(g);
        }
    }
    private void drawHUD(Graphics g) {
        hpBar1.draw(g, player1.getHP(), player1.getMaxHP(), "Player1: " + player1.getName() + "[WASD + F]");
        hpBar2.draw(g, player2.getHP(), player2.getMaxHP(), "Player2: " + player2.getName() + "[Arrow + /]");
    }
    private void drawGameOver(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 80));

        int textWidth = g.getFontMetrics().stringWidth(winnerText);
        g.drawString(winnerText, (GameConfig.WIDTH - textWidth) / 2, 480);

        g.setFont(new Font("Arial", Font.PLAIN, 40));
        String restart = "Press R to restart";
        int restartWidth = g.getFontMetrics().stringWidth(restart);
        g.drawString(restart, (GameConfig.WIDTH - restartWidth) / 2, 550);
    }
    private void drawBullet(Graphics g) {
        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }
    }


    private void handleItemSpawn() {
        spawnTimer++;
        if (spawnTimer >= SPAWN_INTERVAL) {
            spawnTimer = 0;
            if (items.size() < 3) {
                spawnRandomItem();
            }
        }
    }
    private void spawnRandomItem() {
        int x = 300 +  random.nextInt(1320);
        int y = GameConfig.GROUND - 600;

        int type = random.nextInt(5);

        if (type == 0) {
            items.add(new SpeedBoost(x, y));
        } else if (type == 1) {
            items.add(new DamageBoost(x, y));
        } else if (type == 2) {
            items.add(new GunItem(x, y));
        } else if (type == 3) {
            items.add(new SwordItem(x, y));
        } else if (type == 4){
            items.add(new HealItem(x, y));
        }
    }
    private void handleItemFallAndCollect() {
        for (Item item : items) {

            if (item.getY() < GameConfig.GROUND - item.getSize()) {
                item.plusY();
                if (item.getY() >= GameConfig.GROUND - item.getSize()) {
                    item.setY(GameConfig.GROUND - item.getSize());
                }
            }

            if (!item.isCollected()) {
                if (player1.getBodyHitbox().intersects(item.getHitbox())) {
                    item.collect(player1);
                } else if (player2.getBodyHitbox().intersects(item.getHitbox())) {
                    item.collect(player2);
                }
            }
        }
        items.removeIf(Item::isCollected);
    }
    private void handleAttackCollision(){
        Rectangle attack1 = player1.getAttackHitbox();
        if (attack1 != null && attack1.intersects(player2.getBodyHitbox())) {
            int  dir = (player1.getX() < player2.getX()) ? 1 : -1;
            applyHit(player2, player1.getTotalDamage(), dir);
        }

        Rectangle attack2 = player2.getAttackHitbox();
        if (attack2 != null && attack2.intersects(player1.getBodyHitbox())) {
            int  dir = (player2.getX() < player1.getX()) ? 1 : -1;
            applyHit(player1, player2.getTotalDamage(), dir);
        }
    }
    private void handleWinCondition() {
        if (!player1.isAlive()) {
            gameOver = true;
            winnerText = "Player 2 wins!";
        }
        if (!player2.isAlive()) {
            gameOver = true;
            winnerText = "Player 1 wins!";
        }
    }
    private void handlePlayerCollision() {
        Rectangle body1 = player1.getBodyHitbox();
        Rectangle body2 = player2.getBodyHitbox();

        if (body1.intersects(body2)) {
            int overlap;

            if (player1.getX() < player2.getX()) {
                overlap = player1.getX() + player1.getWidth() - player2.getX();
                player1.pushX(-overlap / 5);
                player2.pushX(overlap / 5);
            } else {
                overlap = player2.getX() + player2.getWidth() - player1.getX();
                player2.pushX(-overlap / 5);
                player1.pushX(overlap / 5);
            }
        }


    }
    private void handleBullets() {
        Bullet bullet1 = player1.takePendingBullet();
        if (bullet1 != null) {
            bullets.add(bullet1);
        }

        Bullet bullet2 = player2.takePendingBullet();
        if (bullet2 != null) {
            bullets.add(bullet2);
        }

        for (Bullet bullet : bullets) {
            bullet.update();

            Fighter target = (bullet.getOwner() == player1) ? player2 : player1;

            if (bullet.isActive() && (bullet.getHitbox().intersects(target.getBodyHitbox()))) {
                applyHit(target, bullet.getDamage(), bullet.getDirection());
                bullet.deactivate();
            }

        }
        bullets.removeIf(b -> !b.isActive());

    }

    private void applyHit(Fighter target, int damage, int direction) {
        target.takeDamage(damage);

        freezeTimer = FREEZE_DURATION;

        target.getKnockBack(20 * direction);
        target.getStunned(10);
    }
}