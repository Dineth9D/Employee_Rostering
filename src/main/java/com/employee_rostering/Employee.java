package com.employee_rostering;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "employees")
public class Employee {
    @Id
    private String id;

    private String name;
    private List<String> skills;
    private List<Shift> availability;

    public Employee() {
        // Default constructor for MongoDB mapping
    }

    public Employee(String name, List<Shift> availability, List<String> skills) {
        this.name = name;
        this.availability = availability;
        this.skills = skills;
    }

    // Retrieve the name of an employee
    public String getName() {
        return name;
    }


    // Retrieve the availability shifts of an employee
    public List<Shift> getAvailability() {
        return availability;
    }


    // Retrieve the skills of an employee
    public List<String> getSkills() {
        return skills;
    }

    // Set the skills of an employee
    public void setSkills(List<String> skills) {
        this.skills = skills;
    }
}