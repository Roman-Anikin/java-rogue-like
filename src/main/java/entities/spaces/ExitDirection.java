package entities.spaces;

public enum ExitDirection {

    NORTH(0),
    EAST(1),
    SOUTH(2),
    WEST(3);

    private final int value;

    ExitDirection(int direction) {
        this.value = direction;
    }

    public int getValue() {
        return value;
    }

    public static ExitDirection getDirection(int n) {
        return switch (n) {
            case 0 -> NORTH;
            case 1 -> EAST;
            case 2 -> SOUTH;
            case 3 -> WEST;
            default -> null;
        };
    }
}
