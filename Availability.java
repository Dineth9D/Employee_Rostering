import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public class Availability {
    private Set<DayOfWeek> daysOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private DayOfWeek day;
    private Interval interval;

    public Availability(Set<DayOfWeek> daysOfWeek, LocalTime startTime, LocalTime endTime) {
        this.daysOfWeek = daysOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Availability(DayOfWeek day, Interval interval) {
        this.day = day;
        this.interval = interval;
    }

    public boolean includes(Shift shift) {
        LocalDateTime shiftStart = shift.getStart();
        LocalDateTime shiftEnd = shift.getEnd();
        LocalDateTime intervalStart = startDateTime;
        LocalDateTime intervalEnd = endDateTime;

        // Check if shift starts and ends within the interval
        if (shiftStart.isEqual(intervalStart) || shiftStart.isAfter(intervalStart)) {
            if (shiftEnd.isEqual(intervalEnd) || shiftEnd.isBefore(intervalEnd)) {
                return true;
            }
        }
        return false;
    }

    public Set<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public boolean overlaps(Availability other) {
        for (DayOfWeek dayOfWeek : daysOfWeek) {
            if (other.getDaysOfWeek().contains(dayOfWeek)) {
                if (startTime.isBefore(other.getEndTime()) && endTime.isAfter(other.getStartTime())) {
                    return true;
                }
            }
        }
        return false;
    }
}
