package entities.spaces;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Room {

    private int x;
    private int y;
    private int width;
    private int height;
    private List<Exit> exits;
    private boolean isStarting;
    private Point transition;
    private boolean isVisited;
    private boolean isCurrent;

    public Room() {
    }

    public Room(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        exits = new ArrayList<>();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setExits(List<Exit> exits) {
        this.exits = exits;
    }

    public List<Exit> getExits() {
        return exits;
    }

    public boolean isStarting() {
        return isStarting;
    }

    public void setStarting(boolean starting) {
        isStarting = starting;
    }

    public Point getTransition() {
        return transition;
    }

    public void setTransition(Point point) {
        transition = point;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }

    public boolean contains(int x, int y) {
        return (x >= this.x && x < this.x + getWidth())
                && (y >= this.y && y < this.y + getHeight());
    }

    @Override
    public String toString() {
        return "Room{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", exits=" + exits +
                ", isStarting=" + isStarting +
                ", transition=" + transition +
                ", isVisited=" + isVisited +
                ", isCurrent=" + isCurrent +
                '}';
    }
}
