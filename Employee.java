import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class Employee {
  private String name;
  private List<Availability> availabilityList;

  public Employee(String name, String availabilityString) {
    this.name = name;
    this.availabilityList = parseAvailabilityString(availabilityString);
  }

  public String getName() {
    return name;
  }

  public boolean isAvailableForShift(Shift shift) {
    for (Availability availability : availabilityList) {
      if (availability.includes(shift)) {
        return true;
      }
    }
    return false;
  }

  private List<Availability> parseAvailabilityString(String availabilityString) {
    List<Availability> availabilityList = new ArrayList<>();

    String[] parts = availabilityString.split(";");
    for (String part : parts) {
      String[] subparts = part.split(":");
      if (subparts.length == 2) {
        String dayString = subparts[0].trim();
        String timeString = subparts[1].trim();
        List<DayOfWeek> days = parseDayString(dayString);
        Interval interval = parseTimeInterval(timeString);
        if (days != null && interval != null) {
          for (DayOfWeek day : days) {
            availabilityList.add(new Availability(day, interval));
          }
        }
      }
    }

    return availabilityList;
  }

  private List<DayOfWeek> parseDayString(String dayString) {
    if (dayString.equalsIgnoreCase("daily")) {
      List<DayOfWeek> days = new ArrayList<>();
      days.add(DayOfWeek.MONDAY);
      days.add(DayOfWeek.TUESDAY);
      days.add(DayOfWeek.WEDNESDAY);
      days.add(DayOfWeek.THURSDAY);
      days.add(DayOfWeek.FRIDAY);
      days.add(DayOfWeek.SATURDAY);
      days.add(DayOfWeek.SUNDAY);
      return days;
    } else {
      String[] parts = dayString.split(",");
      if (parts.length > 0) {
        List<DayOfWeek> days = new ArrayList<>();
        for (String part : parts) {
          try {
            DayOfWeek day = DayOfWeek.valueOf(part.trim().toUpperCase());
            days.add(day);
          } catch (IllegalArgumentException e) {
            System.out.println("Invalid day of week: " + part.trim());
          }
        }
        return days;
      }
    }
    return null;
  }

  private Interval parseTimeInterval(String timeString) {
    try {
      String[] parts = timeString.split("-");
      if (parts.length == 2) {
        LocalTime start = LocalTime.parse(parts[0], DateTimeFormatter.ofPattern("hh:mm a"));
        LocalTime end = LocalTime.parse(parts[1], DateTimeFormatter.ofPattern("hh:mm a"));
        return new Interval(start, end);

      }
    } catch (DateTimeParseException e) {
      System.out.println("Invalid time interval: " + timeString);
    }
    return null;
  }
}
