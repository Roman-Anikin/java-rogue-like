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
        switch (n) {
            case 0:
                return NORTH;
            case 1:
                return EAST;
            case 2:
                return SOUTH;
            case 3:
                return WEST;
            default:
                return null;
        }
    }
}
