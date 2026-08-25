package src;

public interface Attackable {
    void attack();
    void takeDamage(int damage);
    boolean isAlive();
}
