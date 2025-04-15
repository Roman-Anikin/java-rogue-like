package entities.characters;

public class Condition {

    private int duration;

    public Condition() {
    }

    public Condition(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public void decreaseDuration() {
        duration--;
    }

    @Override
    public String toString() {
        return "Condition{" +
                "duration=" + duration +
                '}';
    }
}
