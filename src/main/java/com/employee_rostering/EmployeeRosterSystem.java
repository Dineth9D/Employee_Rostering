package com.employee_rostering;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
    }

    public void loadEmployeeData(List<Employee> employees) {
        for (Employee employee : employees) {
            String name = employee.getName();

            // Check if the employee with the same name already exists in the database
            Document existingEmployee = mongoTemplate.getCollection("employees")
                    .find(new Document("name", name))
                    .first();

            if (existingEmployee == null) {
                Document doc = new Document("name", name)
                        .append("availability", employee.getAvailability())
                        .append("shiftCount", 0);
                mongoTemplate.insert(doc, "employees");
            }
        }
    }

    private List<Employee> getAvailableEmployeesForShift(Shift shift) {
        // Retrieve all employees from the database or data store
        List<Employee> allEmployees = mongoTemplate.findAll(Employee.class);

        // Filter employees based on availability criteria for the shift
        List<Employee> availableEmployees = new ArrayList<>();
        for (Employee employee : allEmployees) {
            // Check if the employee is available for the shift based on your availability criteria
            if (isEmployeeAvailableForShift(employee, shift)) {
                availableEmployees.add(employee);
            }
        }

        return availableEmployees;
    }

    private boolean isEmployeeAvailableForShift(Employee employee, Shift shift) {
        // Implement logic to determine if the employee is available for the shift
        // This could involve checking the employee's availability schedule, preferences, etc.
        // Return true if the employee is available, false otherwise

        // Example logic: Check if the shift day and time fall within the employee's availability
        for (Shift availability : employee.getAvailability()) {
            if (availability.getDay().equalsIgnoreCase(shift.getDay()) &&
                    availability.getShift().equalsIgnoreCase(shift.getShift())) {
                return true;
            }
        }
        return false;
    }

    private List<Employee> filterEmployeesBySkills(List<Employee> employees, List<String> requiredSkills) {
        List<Employee> employeesWithRequiredSkills = new ArrayList<>();

        for (Employee employee : employees) {
            List<String> employeeSkills = employee.getSkills();

            // Check if the employee possesses all the required skills for the shift
            boolean hasRequiredSkills = employeeSkills.containsAll(requiredSkills);

            if (hasRequiredSkills) {
                employeesWithRequiredSkills.add(employee);
            }
        }

        return employeesWithRequiredSkills;
    }


    public void generateRoster() {
        // Get the distinct days from the collection and sort them
        List<String> distinctDays = mongoTemplate.getCollection("employees")
                .distinct("availability.day", String.class)
                .into(new ArrayList<>());
        Collections.sort(distinctDays);

        // Create a list of EmployeeAssignment objects
        List<EmployeeAssignment> employeeAssignments = new ArrayList<>();

        List<Shift> shifts = new ArrayList<>();
        // Add shifts to the list
        shifts.add(new Shift("05:00:00", "13:00:00", Collections.singletonList("Java")));
        shifts.add(new Shift("13:00:00", "21:00:00", Collections.singletonList("Python")));
        shifts.add(new Shift("21:00:00", "05:00:00", Collections.singletonList("Ruby")));

        // Iterate over shifts and available employees
        for (Shift shift : shifts) {
            List<Employee> availableEmployees = getAvailableEmployeesForShift(shift);

            // Filter employees based on skills
            List<Employee> employeesWithRequiredSkills = filterEmployeesBySkills(availableEmployees, shift.getRequiredSkills());

            // Sort employees based on their skills, with the best skills first
            employeesWithRequiredSkills.sort((e1, e2) -> {
                int index1 = e1.getSkills().indexOf(shift.getRequiredSkills().get(0));
                int index2 = e2.getSkills().indexOf(shift.getRequiredSkills().get(0));
                return Integer.compare(index1, index2);
            });

            // Create EmployeeAssignment objects and add to the list
            for (Employee employee : employeesWithRequiredSkills) {
                EmployeeAssignment assignment = new EmployeeAssignment(employee, shift);
                employeeAssignments.add(assignment);
            }
        }

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
                    for (Document shift : availability) {
                        String shiftDayString = shift.getString("day");
                        if (shiftDayString.equalsIgnoreCase(dayOfWeekStr)) {
                            LocalTime shiftTime = LocalTime.parse(shift.getString("shift"), DateTimeFormatter.ISO_LOCAL_TIME);
                            shiftEmployeeQueueMap.get(shiftTime).offer(employee);
                        }
                    }
                }
            }

            // Assign employees to shifts using round-robin scheduling algorithm
            int numShifts = shiftEmployeeQueueMap.size();
            int minShiftSize = employees.size() / numShifts;
            int remainder = employees.size() % numShifts;

            // Assign employees to shifts
            for (Map.Entry<LocalTime, Queue<Document>> entry : shiftEmployeeQueueMap.entrySet()) {
                LocalTime shiftTime = entry.getKey();
                Queue<Document> employeeQueue = entry.getValue();
                int shiftSize = minShiftSize + (remainder > 0 ? 1 : 0);
                remainder--;

                System.out.println("Shift Time: " + shiftTime);
                System.out.println("Shift Size: " + shiftSize);

                for (int i = 0; i < shiftSize; i++) {
                    Document employee = employeeQueue.poll();
                    if (employee != null) {
                        System.out.println("Assigned Employee: " + employee.getString("name"));
                        // You can save the employee assignment or perform any other desired operations here
                    }
                }
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

    @PostMapping("/employee-skills")
    public ResponseEntity<String> updateEmployeeSkills(@RequestBody EmployeeSkillsRequest request) {
        // Extract the employee name and skills from the request
        String employeeName = request.getEmployeeName();
        List<String> skills = request.getSkills();

        // Check if the employee exists
        Query query = new Query(Criteria.where("name").is(employeeName));
        Employee existingEmployee = mongoTemplate.findOne(query, Employee.class);
        if (existingEmployee == null) {
            return ResponseEntity.notFound().build();
        }

        // Update the employee's skills in the database
        existingEmployee.setSkills(skills);
        mongoTemplate.save(existingEmployee);

        return ResponseEntity.ok("Employee skills updated successfully.");
    }
}