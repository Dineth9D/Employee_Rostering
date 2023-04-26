import java.time.LocalTime;

public class Shift {
    private String name;
    private Interval interval;

    public Shift(String name, LocalTime startTime, LocalTime endTime) {
        this.name = name;
        this.interval = new Interval(startTime, endTime);
    }

    public String getName() {
        return name;
    }

    public Interval getInterval() {
        return interval;
    }

    public boolean contains(LocalTime time) {
        return interval.contains(time);
    }
}
