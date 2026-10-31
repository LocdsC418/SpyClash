package src.panels;

import src.util.Assets;
import src.util.GameConfig;
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

    private static final int FREEZE_DURATION = 6;
    private static final int SPAWN_INTERVAL = 300;

    private Image bgStage;
    private Timer timer;
    private Random random = new Random();
    private JButton btnPlayAgain;
    private JButton btnMenu;
    private JButton btnPause;
    private JButton btnResume;

    private Fighter player1;
    private Fighter player2;

    private ArrayList<Bullet> bullets = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>();

    private int freezeTimer = 0;
    private int spawnTimer = 0;
    private boolean gameOver = false;
    private boolean paused = false;
    private String winnerText = "";

    public GamePanel(){
        this.setPreferredSize(GameConfig.SCREEN_SIZE);
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);
        this.setLayout(null);

        final int overWidth = 200;
        final int overHeight = 55;
        final int overGap = 20;
        final int overY = 420;

        int overX = (GameConfig.WINDOW_WIDTH - (overWidth * 2 + overGap)) / 2;

        btnPlayAgain = new JButton("PLAY AGAIN");
        btnPlayAgain.setFont(new Font("Arial", Font.BOLD, 24));
        btnPlayAgain.setFocusPainted(false);
        btnPlayAgain.setBounds(overX, overY, overWidth, overHeight);
        btnPlayAgain.setVisible(false);
        this.add(btnPlayAgain);

        btnMenu = new JButton("MENU");
        btnMenu.setFont(new Font("Arial", Font.BOLD, 24));
        btnMenu.setFocusPainted(false);
        btnMenu.setBounds(overX + overWidth + overGap, overY, overWidth, overHeight);
        btnMenu.setVisible(false);
        this.add(btnMenu);

        final int pauseWidth = 140;
        final int pauseHeight = 45;
        final int pauseY = 20;

        int pauseX = (GameConfig.WINDOW_WIDTH - pauseWidth) / 2;

        btnPause = new JButton("PAUSE");
        btnPause.setFont(new Font("Arial", Font.BOLD, 20));
        btnPause.setFocusable(false);
        btnPause.setBounds(pauseX, pauseY, pauseWidth, pauseHeight);
        btnPause.setVisible(false);
        this.add(btnPause);

        btnResume = new JButton("RESUME");
        btnResume.setFont(new Font("Arial", Font.BOLD, 20));
        btnResume.setFocusable(false);
        btnResume.setBounds(pauseX, pauseY, pauseWidth, pauseHeight);
        btnResume.setVisible(false);
        this.add(btnResume);

        timer = new Timer(16, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

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



        Graphics2D g2d = (Graphics2D) g.create();
        double scaleX = (double) getWidth() / GameConfig.WIDTH;
        double scaleY = (double) getHeight() / GameConfig.HEIGHT;
        g2d.scale(scaleX, scaleY);

        drawArena(g2d);

        drawFighter(g2d);
        drawBullet(g2d);
        drawItem(g2d);
        drawHUD(g2d);

        if (gameOver) {
            drawGameOver(g2d);
        } else if (paused) {
            drawPaused(g2d);
        }

        g2d.dispose();
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (gameOver || paused) {
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
    public void keyTyped(KeyEvent e) {}

    private Fighter createFighter(int choice, int startX, Color color) {
        if (choice == 0) {
            return new Assassin(startX, color);
        } else if(choice == 1) {
            return new Enforcer(startX, color);
        } else {
            return null;
        }
    }

    private String createBgStage(int choice) {
        if (choice == 0) {
            return "/res/background/rooftop.png";
        } else if (choice == 1) {
            return "/res/background/rooftop_morning.png";
        } else {
            return "FAILED";
        }
    }

    public void startGame(int p1Choice, int p2Choice, int mapChoice) {

        items.clear();
        bullets.clear();
        spawnTimer = 0;

        bgStage = Assets.loadImage(createBgStage(mapChoice));
        player1 = createFighter(p1Choice, 200, Color.RED);
        player2 = createFighter(p2Choice, 1600, Color.BLUE);
        player2.setFacingDirection(-1);

        gameOver = false;
        paused = false;
        winnerText = "";
        btnPlayAgain.setVisible(false);
        btnMenu.setVisible(false);
        btnPause.setVisible(true);
        btnResume.setVisible(false);
        timer.start();

    }

    public void pause() {
        if (gameOver || paused) {
            return;
        }
        paused = true;
        timer.stop();

        player1.setLeftPressed(false);
        player1.setRightPressed(false);
        player2.setLeftPressed(false);
        player2.setRightPressed(false);

        btnPause.setVisible(false);
        btnResume.setVisible(true);
        repaint();
    }

    public void resume() {
        if (!paused) {
            return;
        }
        paused = false;
        btnResume.setVisible(false);
        btnPause.setVisible(true);
        timer.start();
        this.requestFocusInWindow();
    }

    public JButton getPlayAgainButton() { return btnPlayAgain; }
    public JButton getMenuButton() { return btnMenu; }
    public JButton getPauseButton() { return btnPause; }
    public JButton getResumeButton() { return btnResume; }

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
        drawHealthBar(g, 40, player1, "Player1: " + player1.getName() + "[WASD + F]");
        drawHealthBar(g, 1480, player2, "Player2: " + player2.getName() + "[Arrow + /]");
        drawItemStatus(g, 40, player1, false);
        drawItemStatus(g, 1880, player2, true);
    }
    private void drawItemStatus(Graphics g, int x, Fighter fighter, boolean alignRight) {
        final int y = 138;
        final int ticksPerSecond = 60;

        String text = "";
        if (fighter.hasWeapon()) {
            text += fighter.getWeapon().getName() + " x" + fighter.getWeapon().getUseLeft();
        }
        if (fighter.getSpeedBoostTimer() > 0) {
            int seconds = (fighter.getSpeedBoostTimer() + ticksPerSecond - 1) / ticksPerSecond;
            if (!text.isEmpty()) {
                text += "    ";
            }
            text += "Speed Buff " + seconds + "s";
        }
        if (fighter.getDamageBoostTimer() > 0) {
            int seconds = (fighter.getDamageBoostTimer() + ticksPerSecond - 1) / ticksPerSecond;
            if (!text.isEmpty()) {
                text += "    ";
            }
            text += "Damage Buff " + seconds + "s";
        }
        if (text.isEmpty()) {
            return;
        }

        g.setFont(new Font("Arial", Font.BOLD, 26));
        int drawX = alignRight ? x - g.getFontMetrics().stringWidth(text) : x;

        drawShadowText(g, text, drawX, y);
    }
    private void drawHealthBar(Graphics g, int x, Fighter fighter, String label) {
        final int width = 400;
        final int height = 40;
        final int y = 60;

        int currentHP = fighter.getHP();
        int maxHP = fighter.getMaxHP();
        int hpWidth = (int) ((double) currentHP / maxHP * width);

        g.setColor(Color.WHITE);
        g.drawRect(x, y, width, height);

        if (currentHP > maxHP * 0.6) {
            g.setColor(Color.GREEN);
        } else if (currentHP > maxHP * 0.3) {
            g.setColor(Color.YELLOW);
        } else {
            g.setColor(Color.RED);
        }
        g.fillRect(x + 1, y + 1, hpWidth - 1, height - 1);

        g.setFont(new Font("Arial", Font.BOLD, 24));
        drawShadowText(g, label, x, y - 10);
    }
    private void drawShadowText(Graphics g, String text, int x, int y) {
        g.setColor(Color.BLACK);
        g.drawString(text, x + 2, y + 2);
        g.setColor(Color.WHITE);
        g.drawString(text, x, y);
    }
    private void drawGameOver(Graphics g) {
        g.setFont(new Font("Arial", Font.BOLD, 80));

        int textWidth = g.getFontMetrics().stringWidth(winnerText);
        drawShadowText(g, winnerText, (GameConfig.WIDTH - textWidth) / 2, 480);
    }
    private void drawPaused(Graphics g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, GameConfig.WIDTH, GameConfig.HEIGHT);

        g.setFont(new Font("Arial", Font.BOLD, 100));
        String text = "PAUSED";
        int textWidth = g.getFontMetrics().stringWidth(text);
        drawShadowText(g, text, (GameConfig.WIDTH - textWidth) / 2, 560);
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

        if (gameOver) {
            timer.stop();
            btnPause.setVisible(false);
            btnPlayAgain.setVisible(true);
            btnMenu.setVisible(true);
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