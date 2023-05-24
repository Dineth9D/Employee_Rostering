package com.employee_rostering;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeRosterService {

    @Autowired
    private MongoTemplate mongoTemplate;

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

    public void saveRoster(EmployeeRoster roster) {
        // Implementation to save the roster to MongoDB or any other storage
        // You need to provide the actual implementation based on your application
    }
}