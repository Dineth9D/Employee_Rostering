import java.time.LocalTime;

public class Interval {
    private LocalTime start;
    private LocalTime end;

    public Interval(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public boolean overlapsWith(Interval other) {
        return !(this.end.isBefore(other.start) || this.start.isAfter(other.end));
    }

    public Interval getInterval() {
        return null;
    }

    public String[] split(String string) {
        return null;
    }
}
