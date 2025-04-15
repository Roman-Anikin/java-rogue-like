package entities.characters;

import java.util.Random;

public class Snake extends Monster {

    public Snake() {
    }

    public Snake(int health, int dex, int str, int hostility, int speed) {
        super(health, dex, str, MonsterType.SNAKE, hostility, speed);
    }

    public boolean tryPutToSleep() {
        return new Random().nextInt(100) < 31;
    }

    @Override
    public String toString() {
        return "Snake{" +
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
