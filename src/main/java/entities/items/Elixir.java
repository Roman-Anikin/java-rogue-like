package entities.items;

public class Elixir extends Item {

    public Elixir() {
    }

    public Elixir(int subType, int value) {
        super(ItemType.ELIXIR, ItemSubtype.getSubtype(subType), value);
    }

    @Override
    public String toString() {
        return "Elixir " + getValue() + " " + getSubType();
    }
}
