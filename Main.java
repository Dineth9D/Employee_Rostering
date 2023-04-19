import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Shift shift1 = new Shift("Morning Shift", "9:00", "17:00");
        Shift shift2 = new Shift("Afternoon Shift", "17:00", "01:00");
        Shift shift3 = new Shift("Midnight Shift", "01:00", "09:00");

        List<Shift> shifts = new ArrayList<Shift>();
        shifts.add(shift1);
        shifts.add(shift2);
        shifts.add(shift3);

        Employee emp1 = new Employee("Alice", List.of("9:00", "10:00", "11:00"));
        Employee emp2 = new Employee("Bob", List.of("17:00", "18:00", "19:00"));
        Employee emp3 = new Employee("Toronto", List.of("01:00", "02:00", "03:00"));

        List<Employee> employees = new ArrayList<Employee>();
        employees.add(emp1);
        employees.add(emp2);
        employees.add(emp3);

        Roster roster = new Roster(shifts, employees);
        roster.assignShifts();
    }

}
