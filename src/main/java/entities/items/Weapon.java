package entities.items;

public class Weapon extends Item {

    public Weapon() {
    }

    public Weapon(int value) {
        super(ItemType.WEAPON, ItemSubtype.STR, value);
    }

    @Override
    public String toString() {
        return "Weapon " + getValue() + " " + getSubType();
    }
}
