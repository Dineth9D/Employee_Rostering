import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of employees: ");
        int numEmployees = scanner.nextInt();

        List<Employee> employeeList = new ArrayList<>();
        for (int i = 1; i <= numEmployees; i++) {
            System.out.print("Enter name for employee " + i + ": ");
            String name = scanner.next();
            System.out.print("Enter availability (comma-separated list of times) for employee " + i + ": ");
            String avail = scanner.next();
            Employee employee = new Employee(name, avail.split(","));
            employeeList.add(employee);
        }

        List<Shift> shiftList = new ArrayList<>();
        shiftList.add(new Shift("Morning", "8:00am", "12:00pm"));
        shiftList.add(new Shift("Afternoon", "1:00pm", "5:00pm"));

        Roster roster = new Roster(shiftList, employeeList);
        roster.assignShifts();

        scanner.close();
    }
}