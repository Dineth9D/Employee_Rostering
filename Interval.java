import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Interval {
    private LocalDateTime start;
    private LocalDateTime end;

    public Interval(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    public Interval(LocalTime startTime, LocalTime endTime) {
        this(LocalDateTime.of(LocalDate.now(), startTime), LocalDateTime.of(LocalDate.now(), endTime));
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public boolean overlapsWith(Interval other) {
        return !(this.end.isBefore(other.start) || this.start.isAfter(other.end));
    }

    public Interval withStart(LocalDateTime newStart) {
        return new Interval(newStart, this.end);
    }

    public Interval withEnd(LocalDateTime newEnd) {
        return new Interval(this.start, newEnd);
    }
}
