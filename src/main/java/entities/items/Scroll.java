package entities.items;

public class Scroll extends Item {

    public Scroll() {
    }

    public Scroll(int subType, int value) {
        super(ItemType.SCROLL, ItemSubtype.getSubtype(subType), value);
    }

    @Override
    public String toString() {
        return "Scroll " + getValue() + " " + getSubType();
    }
}
