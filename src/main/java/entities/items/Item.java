package entities.items;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Elixir.class, name = "ELIXIR"),
        @JsonSubTypes.Type(value = Food.class, name = "FOOD"),
        @JsonSubTypes.Type(value = Gold.class, name = "GOLD"),
        @JsonSubTypes.Type(value = Scroll.class, name = "SCROLL"),
        @JsonSubTypes.Type(value = Weapon.class, name = "WEAPON")
})
public abstract class Item {

    private int x;
    private int y;
    private ItemType type;
    private ItemSubtype subType;
    protected int value;

    public Item() {
    }

    public Item(ItemType type, ItemSubtype subType, int value) {
        this.type = type;
        this.subType = subType;
        this.value = value;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public ItemType getType() {
        return type;
    }

    public ItemSubtype getSubType() {
        return subType;
    }

    public int getValue() {
        return value;
    }

    public void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toPrint() {
        return getValue() + " " + getSubType();
    }
}
