package entities.items;

public enum ItemSubtype {

    STR(0),
    DEX(1),
    HP(2);

    private final int value;

    ItemSubtype(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ItemSubtype getSubtype(int n) {
        return switch (n) {
            case 0 -> STR;
            case 1 -> DEX;
            case 2 -> HP;
            default -> null;
        };
    }
}
