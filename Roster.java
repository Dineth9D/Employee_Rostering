import java.util.ArrayList;
import java.util.List;

public class Roster {
  private List<Shift> shiftList;
  private List<Employee> employeeList;

  public Roster(List<Shift> shiftList, List<Employee> employeeList) {
    this.shiftList = shiftList;
    this.employeeList = employeeList;
  }

  public void assignShifts() {
    for (Shift shift : shiftList) {
      List<String> assignedEmployees = new ArrayList<>();
      for (Employee employee : employeeList) {
        String[] availability = employee.getAvailability();
        for (String avail : availability) {
          if (shift.getStartTime().equals(avail)) {
            assignedEmployees.add(employee.getName());
          }
        }
      }
      if (assignedEmployees.isEmpty()) {
        System.out.println("No employees available for " + shift.getName() + " shift");
      } else {
        System.out.println("Assigned employees for " + shift.getName() + " shift: " + assignedEmployees);
      }
    }
  }
}
