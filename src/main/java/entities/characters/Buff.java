package entities.characters;

import entities.items.ItemSubtype;

public class Buff {

    public Buff() {
    }

    private ItemSubtype type;
    private int value;
    private int duration;

    public Buff(ItemSubtype type, int value, int duration) {
        this.type = type;
        this.value = value;
        this.duration = duration;
    }

    public ItemSubtype getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public int getDuration() {
        return duration;
    }

    public void decreaseDuration() {
        duration--;
    }

    @Override
    public String toString() {
        return "Buff{" +
                "type=" + type +
                ", value=" + value +
                ", duration=" + duration +
                '}';
    }
}
