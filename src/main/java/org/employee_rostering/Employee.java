package org.employee_rostering;

import java.util.List;

public class Employee {
    private String name;
    private List<Availability> availability;

    public Employee(String name, List<Availability> availability) {
        this.name = name;
        this.availability = availability;
    }

    public String getName() {
        return name;
    }

    public List<Availability> getAvailability() {
        return availability;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvailability(List<Availability> availability) {
        this.availability = availability;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", availability=" + availability +
                '}';
    }
}

