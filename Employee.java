import java.util.List;

public class Employee {
  private String employeeName;
  private List<String> availability;

  // Create a class constructor
  public Employee(String name, List<String> avail) {
    this.employeeName = name; // the name of the employee
    this.availability = avail; // the times the employee is available
  }

  public String getName() {
    return employeeName;
  }

  public List<String> getAvailability() {
    return availability;
  }
}
