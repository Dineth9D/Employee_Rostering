
public class Employee {
  private String employeeName;
  private String[] availability;

  // Create a class constructor
  public Employee(String name, String[] availability) {
    this.employeeName = name; // the name of the employee
    this.availability = availability; // the times the employee is available
  }

  public String getName() {
    return employeeName;
  }

  public String[] getAvailability() {
    return availability;
  }
}
