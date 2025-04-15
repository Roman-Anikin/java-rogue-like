package entities.items;

public class Gold extends Item {

    public Gold() {
    }

    public Gold(int value) {
        super(ItemType.GOLD, null, value);
    }

    public void addGold(int value) {
        this.value += value;
    }

    @Override
    public String toString() {
        return getValue() + " Gold";
    }

    @Override
    public String toPrint() {
        return getValue() + " " + getType();
    }
}
