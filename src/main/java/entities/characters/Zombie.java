package entities.characters;

public class Zombie extends Monster {

    public Zombie() {
    }

    public Zombie(int health, int dex, int str, int hostility, int speed) {
        super(health, dex, str, MonsterType.ZOMBIE, hostility, speed);
    }

    @Override
    public String toString() {
        return "Zombie{" +
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
                '}';
    }
}
