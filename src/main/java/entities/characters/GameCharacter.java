package entities.characters;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Ghoul.class),
        @JsonSubTypes.Type(value = Ogre.class),
        @JsonSubTypes.Type(value = Snake.class),
        @JsonSubTypes.Type(value = Vampire.class),
        @JsonSubTypes.Type(value = Zombie.class),
        @JsonSubTypes.Type(value = Player.class)
})
public abstract class GameCharacter {

    private int x;
    private int y;
    private int health;
    private int maxHealth;
    private int dex;
    private int str;
    private int speed;

    public GameCharacter() {
    }

    public GameCharacter(int health, int dex, int str, int speed) {
        this.health = health;
        maxHealth = health;
        this.dex = dex;
        this.str = str;
        this.speed = speed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getDex() {
        return dex;
    }

    public void setDex(int dex) {
        this.dex = dex;
    }

    public int getStr() {
        return str;
    }

    public void setStr(int str) {
        this.str = str;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
