import java.util.ArrayList;
import java.util.List;

public class Roster {
  private List<Shift> shifts;
  private List<Employee> employees;

  public Roster(List<Shift> shiftList, List<Employee> empList) {
    this.shifts = shiftList;
    this.employees = empList;
  }

  // assign employees to shifts based on their availability.
  public void assignShifts() {
    for (Shift shift : shifts) {
      System.out.println("\nShift: " + shift.getName());

      List<String> assignedEmployees = new ArrayList<String>();
      for (Employee emp : employees) {
        if (emp.getAvailability().contains(shift.getStartTime())) {
          assignedEmployees.add(emp.getName());
        }
      }

      if (assignedEmployees.size() > 0) {
        System.out.println("Assigned employees: " + assignedEmployees);
      } else {
        System.out.println("No available employees for this shift");
      }
    }
  }
}
