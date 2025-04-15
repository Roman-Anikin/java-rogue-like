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
        switch (n) {
            case 0:
                return GOLD;
            case 1:
                return FOOD;
            case 2:
                return ELIXIR;
            case 3:
                return SCROLL;
            case 4:
                return WEAPON;
            default:
                return null;
        }
    }

    public static ItemType getType(char ch) {
        switch (ch) {
            case USE_WEAPON:
                return WEAPON;
            case USE_FOOD:
                return FOOD;
            case USE_ELIXIR:
                return ELIXIR;
            case USE_SCROLL:
                return SCROLL;
            default:
                return null;
        }
    }
}
