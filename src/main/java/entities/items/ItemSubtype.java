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
        switch (n) {
            case 0:
                return STR;
            case 1:
                return DEX;
            case 2:
                return HP;
            default:
                return null;
        }
    }
}
