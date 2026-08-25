import java.awt.Color;
import java.awt.Graphics;

public class Fighter {

    private int x;
    private int y;
    private int width = 50;
    private int height = 100;
    private int speed = 5;
    private int velocityY = 0;
    private Color color;

    private final int GROUND = 350;
    private final int GRAVITY = 1;
    private final int JUMP_FORCE = -15;
    private final int SCREEN_WIDTH = 800;

    private boolean onGround = true;
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    public Fighter(int startX, Color color) {
        this.x = startX;
        this.y = GROUND;
        this.color = color;
    }

    public void update() {
        if (leftPressed){
            x -= speed;
        }
        if (rightPressed){
            x += speed;
        }

        if (x<0){
            x = 0;
        }
        if (x > SCREEN_WIDTH - width) {
            x = SCREEN_WIDTH - width;
        }

        if (!onGround){
            velocityY += GRAVITY;
            y += velocityY;

            if (y >= GROUND){
                y = GROUND;
                velocityY = 0;
                onGround = true;
            }
        }
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }

    public void jump() {
        if (onGround) {
            velocityY = JUMP_FORCE;
            onGround = false;
        }
    }

    public void setLeftPressed(boolean pressed) {
        this.leftPressed = pressed;
    }

    public void setRightPressed(boolean pressed) {
        this.rightPressed = pressed;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() {return height; }

}
