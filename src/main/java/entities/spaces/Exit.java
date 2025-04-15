package entities.spaces;

public class Exit {

    private int x;
    private int y;
    private ExitDirection direction;

    public Exit() {
    }

    public Exit(ExitDirection direction) {
        this.direction = direction;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public ExitDirection getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return "Exit{" +
                "x=" + x +
                ", y=" + y +
                ", direction=" + direction +
                '}';
    }
}
