package org.employee_rostering;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/employee-roster")
public class EmployeeDetailsController {

    private final EmployeeRosterSystem employeeRosterSystem;

    public EmployeeDetailsController(EmployeeRosterSystem employeeRosterSystem) {
        this.employeeRosterSystem = employeeRosterSystem;
    }

    @GetMapping
    public Map<String, Object> employeeDetailsRequest(@RequestParam("employees") List<Employee> employees) {
        employeeRosterSystem.loadEmployeeData(employees);
        employeeRosterSystem.generateRoster();
        // Return the desired response
        // You can modify the return type and content as per your requirements
        return Map.of("message", "Employee rostering completed successfully.");
    }
}
