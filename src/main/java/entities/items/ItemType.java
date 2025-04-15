package entities.items;

import static util.GameUtils.*;

public enum ItemType {

    GOLD(0),
    FOOD(1),
    ELIXIR(2),
    SCROLL(3),
    WEAPON(4);

    private final int value;

    ItemType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ItemType getType(int n) {
        return switch (n) {
            case 0 -> GOLD;
            case 1 -> FOOD;
            case 2 -> ELIXIR;
            case 3 -> SCROLL;
            case 4 -> WEAPON;
            default -> null;
        };
    }

    public static ItemType getType(char ch) {
        return switch (ch) {
            case USE_WEAPON -> WEAPON;
            case USE_FOOD -> FOOD;
            case USE_ELIXIR -> ELIXIR;
            case USE_SCROLL -> SCROLL;
            default -> null;
        };
    }
}
