package entities.items;

import java.util.ArrayList;
import java.util.List;

public class Backpack {

    private final List<Item> items;

    public Backpack() {
        items = new ArrayList<>();
    }

    public List<Item> getItems() {
        return items;
    }

    public int getGold() {
        return items.stream()
                .filter(item -> item.getType() == ItemType.GOLD)
                .findFirst()
                .map(Item::getValue)
                .orElse(0);
    }

    @Override
    public String toString() {
        return items.toString();
    }
}
