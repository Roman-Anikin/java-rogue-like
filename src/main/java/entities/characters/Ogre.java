package entities.characters;

public class Ogre extends Monster {

    private Condition rest;
    private boolean isCounterAttacking;

    public Ogre() {
    }

    public Ogre(int health, int dex, int str, int hostility, int speed) {
        super(health, dex, str, MonsterType.OGRE, hostility, speed);
        rest = new Condition(0);
        isCounterAttacking = false;
    }

    public Condition getRest() {
        return rest;
    }

    public void setRest(Condition rest) {
        this.rest = rest;
    }

    public boolean isResting() {
        return rest.getDuration() > 0;
    }

    public int counterAttack() {
        return getStr() / 2;
    }

    public boolean isCounterAttacking() {
        return isCounterAttacking;
    }

    public void setCounterAttacking(boolean counterAttacking) {
        isCounterAttacking = counterAttacking;
    }

    @Override
    public String toString() {
        return "Ogre{" +
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
                ", rest=" + rest +
                ", setCounterAttacking=" + isCounterAttacking +
                '}';
    }
}
