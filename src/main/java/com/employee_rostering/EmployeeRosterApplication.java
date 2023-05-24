package com.employee_rostering;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@SpringBootApplication
@EntityScan(basePackages = "com.employee_rostering")
@RestController
public class EmployeeRosterApplication {

    private List<Employee> employeeList;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private OptaPlannerSolver optaPlannerSolver;

    public static void main(String[] args) {
        SpringApplication.run(EmployeeRosterApplication.class, args);
    }

    public void loadEmployeeData(List<Employee> employees) {
        for (Employee employee : employees) {
            String name = employee.getName();

            // Check if the employee with the same name already exists in the database
            Query query = new Query(Criteria.where("name").is(name));
            boolean employeeExists = mongoTemplate.exists(query, Employee.class);

            if (!employeeExists) {
                mongoTemplate.insert(employee);
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
    // Reminder
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
            boolean hasRequiredSkills = true;
            for (String requiredSkill : requiredSkills) {
                if (!employeeSkills.contains(requiredSkill)) {
                    hasRequiredSkills = false;
                    break;
                }
            }

            if (hasRequiredSkills) {
                employeesWithRequiredSkills.add(employee);
            }
        }

        return employeesWithRequiredSkills;
    }


    private List<EmployeeAssignment> generateEmployeeAssignments(List<Employee> employees, List<Shift> shifts) {
        List<EmployeeAssignment> employeeAssignments = new ArrayList<>();

        for (Shift shift : shifts) {
            List<Employee> availableEmployees = getAvailableEmployeesForShift(shift);
            List<Employee> employeesWithRequiredSkills = filterEmployeesBySkills(availableEmployees, shift.getRequiredSkills());

            employeesWithRequiredSkills.sort((e1, e2) -> {
                int index1 = e1.getSkills().indexOf(shift.getRequiredSkills().get(0));
                int index2 = e2.getSkills().indexOf(shift.getRequiredSkills().get(0));
                return Integer.compare(index1, index2);
            });

            for (Employee employee : employeesWithRequiredSkills) {
                EmployeeAssignment assignment = new EmployeeAssignment(employee, shift);
                employeeAssignments.add(assignment);
            }
        }

        return employeeAssignments;
    }


    public void generateRoster() {
        List<String> distinctDays = mongoTemplate.getCollection("employees")
                .distinct("availability.day", String.class)
                .into(new ArrayList<>());
        Collections.sort(distinctDays);

        List<Shift> shifts = new ArrayList<>();
        shifts.add(new Shift("05:00:00", "13:00:00", Collections.singletonList("Java")));
        shifts.add(new Shift("13:00:00", "21:00:00", Collections.singletonList("Python")));
        shifts.add(new Shift("21:00:00", "05:00:00", Collections.singletonList("Ruby")));

        List<EmployeeAssignment> employeeAssignments = generateEmployeeAssignments(employeeList, shifts);

        for (String dayOfWeekStr : distinctDays) {
            DayOfWeek dayOfWeek = DayOfWeek.valueOf(dayOfWeekStr.toUpperCase(Locale.ENGLISH));
            LocalDate day = LocalDate.now().with(TemporalAdjusters.nextOrSame(dayOfWeek));
            System.out.println("Day: " + day);

            List<Document> employees = mongoTemplate.getCollection("employees")
                    .find(new Document("availability.day", dayOfWeekStr)).into(new ArrayList<>());

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

            for (Map.Entry<LocalTime, Queue<Document>> entry : shiftEmployeeQueueMap.entrySet()) {
                LocalTime shiftTime = entry.getKey();
                Queue<Document> employeeQueue = entry.getValue();

                int shiftSize = employeeQueue.size();
                System.out.println("Shift Time: " + shiftTime);
                System.out.println("Shift Size: " + shiftSize);

                for (int i = 0; i < shiftSize; i++) {
                    Document employee = employeeQueue.poll();
                    if (employee != null) {
                        System.out.println("Assigned Employee: " + employee.getString("name"));
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

        setEmployeeList(employees);

        loadEmployeeData(employees);

        generateRoster();

        return ResponseEntity.ok("Roster generated successfully.");
    }

    @PostMapping("/employee-skills")
    public ResponseEntity<String> updateEmployeeSkills(@RequestBody EmployeeSkillsRequest request) {
        String employeeName = request.getEmployeeName();
        List<String> skills = request.getSkills();

        Query query = new Query(Criteria.where("name").is(employeeName));
        Employee existingEmployee = mongoTemplate.findOne(query, Employee.class);
        if (existingEmployee == null) {
            return ResponseEntity.notFound().build();
        }

        existingEmployee.setSkills(skills);
        mongoTemplate.save(existingEmployee);

        return ResponseEntity.ok("Employee skills updated successfully.");
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }
}