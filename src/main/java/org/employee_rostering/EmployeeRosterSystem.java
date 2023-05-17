package org.employee_rostering;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
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
                .distinct("availability.day", String.class)
                .into(new ArrayList<>());

        // Iterate over each distinct day
        for (String dayOfWeekStr : distinctDays) {
            DayOfWeek dayOfWeek = DayOfWeek.valueOf(dayOfWeekStr.toUpperCase(Locale.ENGLISH));
            LocalDate day = LocalDate.now().with(TemporalAdjusters.nextOrSame(dayOfWeek));
            System.out.println("Day: " + day);

            // Query the collection to find employees assigned to the current day
            List<Document> employees = mongoTemplate.getCollection("employees")
                    .find(new Document("availability.day", dayOfWeekStr)).into(new ArrayList<>());

            // Create map of shifts with a queue of employees that are available for that shift
            Map<LocalTime, Queue<Document>> shiftEmployeeQueueMap = new HashMap<>();
            for (LocalTime time : Arrays.asList(LocalTime.parse("05:00:00"), LocalTime.parse("13:00:00"), LocalTime.parse("21:00:00"))) {
                shiftEmployeeQueueMap.put(time, new LinkedList<>());
            }
            for (Document employee : employees) {
                List<Document> availability = employee.getList("availability", Document.class);
                if (availability != null && !availability.isEmpty()) {
                    Document shift = availability.get(0);
                    String shiftDayString = shift.getString("day");
                    if (shiftDayString.equalsIgnoreCase(dayOfWeekStr)) {
                        LocalTime shiftTime = LocalTime.parse(shift.getString("shift"), DateTimeFormatter.ISO_LOCAL_TIME);
                        shiftEmployeeQueueMap.get(shiftTime).offer(employee);
                    }
                }
            }

            // Assign employees to shifts using round-robin scheduling algorithm
            int numShifts = shiftEmployeeQueueMap.size();
            int[] shiftIndices = new int[numShifts];
            int minShiftSize = employees.size() / numShifts;
            int remainder = employees.size() % numShifts;
            for (int i = 0; i < numShifts; i++) {
                shiftIndices[i] = minShiftSize;
                if (remainder > 0) {
                    shiftIndices[i]++;
                    remainder--;
                }
            }
            for (int i = 0; i < numShifts; i++) {
                LocalTime shiftTime = (LocalTime) shiftEmployeeQueueMap.keySet().toArray()[i];
                Queue<Document> employeeQueue = shiftEmployeeQueueMap.get(shiftTime);
                int numEmployeesToAssign = shiftIndices[i];
                for (int j = 0; j < numEmployeesToAssign; j++) {
                    Document employee = employeeQueue.poll();
                    if (employee != null) {
                        String name = employee.getString("name");
                        mongoTemplate.getCollection("employees").updateOne(new Document("name", name),
                                new Document("$inc", new Document("shiftCount", 1))
                                        .append("$push", new Document("assignedShifts",
                                                new Document("day", day).append("shift", shiftTime.format(DateTimeFormatter.ISO_LOCAL_TIME)))));
                        System.out.println("  Shift: " + shiftTime);
                        System.out.println("  Employee: " + name);
                    }
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