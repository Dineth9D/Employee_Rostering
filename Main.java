import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Shift shift1 = new Shift("Morning Shift", "9:00", "13:00");
        Shift shift2 = new Shift("Afternoon Shift", "14:00", "18:00");

        List<Shift> shifts = new ArrayList<Shift>();
        shifts.add(shift1);
        shifts.add(shift2);

        Employee emp1 = new Employee("Alice", List.of("9:00", "10:00", "11:00"));
        Employee emp2 = new Employee("Bob", List.of("14:00", "15:00", "16:00"));

        List<Employee> employees = new ArrayList<Employee>();
        employees.add(emp1);
        employees.add(emp2);

        Roster roster = new Roster(shifts, employees);
        roster.assignShifts();
    }

}
