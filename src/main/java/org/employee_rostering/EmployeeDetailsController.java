package org.employee_rostering;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Type;

@RestController
public class EmployeeDetailsController {

    @Autowired
    private EmployeeRosterSystem employeeRosterSystem;

    @GetMapping("/api/v1/employee-roster")
    public Map<String, Object> getEmployeeRoster(@RequestParam("content") String content) {
        List<Employee> employees = convertJSONToEmployeeList(content);
        employeeRosterSystem.loadEmployeeData(employees);
        employeeRosterSystem.generateRoster();
        // Return the roster or any other desired response
        // You can modify the return type and response as per your requirements
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Roster generated successfully");
        return response;
    }

    private List<Employee> convertJSONToEmployeeList(String jsonContent) {
        Gson gson = new Gson();
        Type employeeListType = new TypeToken<List<Employee>>() {}.getType();
        return gson.fromJson(jsonContent, employeeListType);
    }
}
