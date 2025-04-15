package entities.characters;

public class Ghoul extends Monster {

    private boolean isInvisible;

    public Ghoul() {
    }

    public Ghoul(int health, int dex, int str, int hostility, int speed) {
        super(health, dex, str, MonsterType.GHOUL, hostility, speed);
        isInvisible = false;
    }

    public void setInvisible(boolean invisible) {
        isInvisible = invisible;
    }

    public boolean isInvisible() {
        return isInvisible;
    }

    @Override
    public String toString() {
        return "Ghoul{" +
                "x=" + getX() +
                ", y=" + getY() +
                ", type=" + getType() +
                ", health=" + getHealth() +
                ", maxHealth=" + getMaxHealth() +
                ", dex=" + getDex() +
                ", str=" + getStr() +
                ", hostility=" + getHostility() +
                ", speed=" + getSpeed() +
                ", isChasing=" + isChasing() +
                ", isInvisible=" + isInvisible +
                '}';
    }
}
