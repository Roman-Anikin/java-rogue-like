package entities.characters;

import entities.items.Backpack;
import entities.items.Item;

public class Player extends GameCharacter {

    private String name;
    private Backpack backpack;
    private Item weapon;
    private Buff buff;
    private Condition sleep;

    public Player() {
        super();
    }

    public Player(int health, int dex, int str, int speed) {
        super(health, dex, str, speed);
        backpack = new Backpack();
        sleep = new Condition(0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Backpack getBackpack() {
        return backpack;
    }

    public Item getWeapon() {
        return weapon;
    }

    public void setWeapon(Item weapon) {
        this.weapon = weapon;
    }

    public Buff getBuff() {
        return buff;
    }

    public void setBuff(Buff buff) {
        this.buff = buff;
    }

    public Condition getSleep() {
        return sleep;
    }

    public void setSleep(Condition sleep) {
        this.sleep = sleep;
    }

    public boolean isSleeping() {
        return sleep.getDuration() > 0;
    }

    @Override
    public String toString() {
        return "Player{" +
                "x=" + getX() +
                ", y=" + getY() +
                ", name=" + name +
                ", health=" + getHealth() +
                ", maxHealth=" + getMaxHealth() +
                ", dex=" + getDex() +
                ", str=" + getStr() +
                ", speed=" + getSpeed() +
                ", backpack=" + backpack +
                ", weapon=" + weapon +
                ", buff=" + buff +
                ", sleep=" + sleep +
                '}';
    }
}
