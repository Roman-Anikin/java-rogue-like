package entities.items;

public class Food extends Item {

    public Food() {
    }

    public Food(int value) {
        super(ItemType.FOOD, ItemSubtype.HP, value);
    }

    @Override
    public String toString() {
        return "Food " + getValue() + " " + getSubType();
    }
}
