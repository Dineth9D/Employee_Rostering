import java.util.List;

public class Employee {
  private String employeeName;
  private List<String> availability;

  public Employee(String name, List<String> avail) {
    this.employeeName = name;
    this.availability = avail;
  }

  public String getName() {
    return employeeName;
  }

  public List<String> getAvailability() {
    return availability;
  }
}
