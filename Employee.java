import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Employee {
  private String name;
  private List<Interval> availability;

  public Employee(String name, String availabilityString) {
    this.name = name;
    this.availability = parseAvailabilityString(availabilityString);
  }

  public String getName() {
    return name;
  }

  public List<Interval> getAvailability() {
    return availability;
  }

  public boolean isAvailableForShift(Shift shift) {
    for (Interval interval : availability) {
      if (interval.overlapsWith(shift.getInterval())) {
        return true;
      }
    }
    return false;
  }

  private List<Interval> parseAvailabilityString(String availabilityString) {
    List<Interval> intervals = new ArrayList<Interval>();
    String[] parts = availabilityString.split(";");
    for (String part : parts) {
      String[] times = part.split("-");
      LocalTime startTime = LocalTime.parse(times[0], DateTimeFormatter.ofPattern("hh:mma"));
      LocalTime endTime = LocalTime.parse(times[1], DateTimeFormatter.ofPattern("hh:mma"));
      intervals.add(new Interval(startTime, endTime));
    }
    return intervals;
  }
}
