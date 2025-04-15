package entities.spaces;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Hallway {

    private boolean isVisited;
    private ExitDirection direction;
    private Exit start;
    private Exit end;
    private final List<Point> coordinates;

    public Hallway() {
        coordinates = new ArrayList<>();
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    public ExitDirection getDirection() {
        return direction;
    }

    public void setDirection(ExitDirection direction) {
        this.direction = direction;
    }

    public Exit getStart() {
        return start;
    }

    public void setStart(Exit start) {
        this.start = start;
    }

    public Exit getEnd() {
        return end;
    }

    public void setEnd(Exit end) {
        this.end = end;
    }

    public List<Point> getCoordinates() {
        return coordinates;
    }

    public boolean contains(int x, int y) {
        return coordinates.stream()
                .anyMatch(point -> point.getX() == x && point.getY() == y);
    }

    @Override
    public String toString() {
        return "Hallway{" +
                "isVisited=" + isVisited +
                ", direction=" + direction +
                ", start=" + start +
                ", end=" + end +
                ", coordinates=" + coordinates +
                '}';
    }
}
