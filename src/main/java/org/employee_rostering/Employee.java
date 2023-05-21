package org.employee_rostering;

import java.util.List;

public class Employee {
    private final String name;
    private final List<Shift> availability;

    public Employee(String name, List<Shift> availability) {
        this.name = name;
        this.availability = availability;
    }

    // Retrieve the name of an employee
    public String getName() {
        return name;
    }

    // Access the list of shifts an employee is available
    public List<Shift> getAvailability() {
        return availability;
    }
}