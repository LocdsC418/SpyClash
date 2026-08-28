package src;

public abstract class WeaponItem extends Item {

    public WeaponItem(int x, int y) {
        super(x, y);
    }

    public abstract Weapon createWeapon();

    @Override
    public void applyTo(Fighter f) {
        f.setWeapon(createWeapon());
    }

    @Override
    public int getAmount() {
        return 0;
    }

    @Override
    public int getEffectDuration(){
        return 0;
    }
}
