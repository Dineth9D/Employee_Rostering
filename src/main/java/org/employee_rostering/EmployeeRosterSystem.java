package org.employee_rostering;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@SpringBootApplication
@RestController
public class EmployeeRosterSystem implements CommandLineRunner {

    @Autowired
    private MongoTemplate mongoTemplate;

    public static void main(String[] args) {
        SpringApplication.run(EmployeeRosterSystem.class, args);
    }

    @Override
    public void run(String... args) {
        // Nothing to do here
    }

    public void loadEmployeeData(List<Employee> employees) {
        List<Document> documents = new ArrayList<>();
        for (Employee employee : employees) {
            Document doc = new Document("name", employee.getName())
                    .append("availability", employee.getAvailability())
                    .append("shiftCount", 0);
            documents.add(doc);
        }
        mongoTemplate.insert(documents, "employees");
    }

    public void generateRoster() {
        // Get the distinct days from the collection
        List<String> distinctDays = mongoTemplate.getCollection("employees")
                .distinct("availability.day", String.class).into(new ArrayList<>());

        // Iterate over each distinct day
        for (String day : distinctDays) {
            System.out.println("Day: " + day);

            // Query the collection to find employees assigned to the current day
            List<Document> employees = mongoTemplate.getCollection("employees")
                    .find(new Document("availability.day", day)).into(new ArrayList<>());

            // Keep track of assigned employees for each shift
            Map<String, Set<String>> shiftEmployeesMap = new HashMap<>();

            // Iterate over the assigned employees
            for (Document employee : employees) {
                String name = employee.getString("name");
                List<Document> availability = employee.getList("availability", Document.class);
                if (availability != null && !availability.isEmpty()) {
                    Document shift = availability.get(0);
                    String shiftTime = shift.getString("shift");

                    // Add the employee to the corresponding shift in the map
                    shiftEmployeesMap.computeIfAbsent(shiftTime, k -> new HashSet<>()).add(name);
                }
            }

            // Print the assigned employees for each shift
            for (Map.Entry<String, Set<String>> entry : shiftEmployeesMap.entrySet()) {
                String shiftTime = entry.getKey();
                Set<String> employeesSet = entry.getValue();

                System.out.println("  Shift: " + shiftTime);
                for (String employee : employeesSet) {
                    System.out.println("  Employee: " + employee);
                }
                System.out.println();
            }
        }
    }

    @PostMapping("/generate-roster")
    public ResponseEntity<String> generateRosterFromRequest(@RequestBody List<Employee> employees) {
        if (employees == null || employees.isEmpty()) {
            return ResponseEntity.badRequest().body("No employee data provided.");
        }

        // Load employee data into MongoDB
        loadEmployeeData(employees);

        // Generate roster
        generateRoster();

        return ResponseEntity.ok("Roster generated successfully.");
    }
}