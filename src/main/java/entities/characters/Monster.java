package entities.characters;

public abstract class Monster extends GameCharacter {

    private MonsterType type;
    private int hostility;
    private int speed;
    private boolean isChasing;

    public Monster() {
    }

    public Monster(int health, int dex, int str, MonsterType type, int hostility, int speed) {
        super(health, dex, str, speed);
        this.type = type;
        this.hostility = hostility;
        this.speed = speed;
    }

    public MonsterType getType() {
        return type;
    }

    public int getHostility() {
        return hostility;
    }

    public int getSpeed() {
        return speed;
    }

    @Override
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isChasing() {
        return isChasing;
    }

    public void setChasing() {
        isChasing = true;
    }
}
