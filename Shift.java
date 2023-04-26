import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Shift {
  private String name;
  private LocalDateTime start;
  private LocalDateTime end;

  public Shift(String name, LocalDateTime start, LocalDateTime end) {
    this.name = name;
    this.start = start;
    this.end = end;
  }

  public Shift(String name, LocalTime startTime, LocalTime endTime) {
    this.name = name;
    this.start = LocalDateTime.of(LocalDate.now(), startTime);
    this.end = LocalDateTime.of(LocalDate.now(), endTime);
  }

  public LocalDateTime getStart() {
    return start;
  }

  public LocalDateTime getEnd() {
    return end;
  }

  public boolean overlapsWith(Shift other) {
    return this.start.isBefore(other.getEnd()) && this.end.isAfter(other.getStart());
  }

  public String getName() {
    return name;
  }
}