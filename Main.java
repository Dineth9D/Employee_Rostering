import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of employees: ");
        int numEmployees = scanner.nextInt();
        scanner.nextLine();

        List<Employee> employees = new ArrayList<>();

        try {
            File file = new File("employees.csv");
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                String name = parts[0];
                String availabilityString = parts[1];
                Employee employee = new Employee(name, availabilityString);
                employees.add(employee);
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }

        List<Shift> shiftList = new ArrayList<>();
        shiftList.add(new Shift("Morning", LocalTime.parse("08:00 AM"), LocalTime.parse("16:00 PM")));
        shiftList.add(new Shift("Afternoon", LocalTime.parse("16:00 PM"), LocalTime.parse("00:00 AM")));
        shiftList.add(new Shift("Midnight", LocalTime.parse("00:00 AM"), LocalTime.parse("08:00 AM")));

        for (Shift shift : shiftList) {
            System.out.println("=== " + shift.getName() + " Shift ===");
            for (Employee employee : employees) {
                if (employee.isAvailableForShift(shift)) {
                    System.out.println(shift.getName() + " shift: " + employee.getName());
                }
            }
        }
        scanner.close();
    }
}
