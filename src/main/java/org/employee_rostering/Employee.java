package org.employee_rostering;

import java.util.List;

public class Employee {
    private String name;
    private List<Shift> availability;

    public Employee(String name, List<Shift> availability) {
        this.name = name;
        this.availability = availability;
    }

    public String getName() {
        return name;
    }

    public List<Shift> getAvailability() {
        return availability;
    }
}