package entities.characters;

public class Vampire extends Monster {

    private boolean isMissable;

    public Vampire() {
    }

    public Vampire(int health, int dex, int str, int hostility, int speed) {
        super(health, dex, str, MonsterType.VAMPIRE, hostility, speed);
        isMissable = true;
    }

    public boolean isMissable() {
        return isMissable;
    }

    public void loseEvasion() {
        isMissable = false;
    }

    public int lifeSteal() {
        return 2;
    }

    @Override
    public String toString() {
        return "Vampire{" +
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
                ", isMissable=" + isMissable +
                '}';
    }
}
