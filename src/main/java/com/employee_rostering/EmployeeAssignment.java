package com.employee_rostering;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
public class EmployeeAssignment {

    @PlanningVariable(valueRangeProviderRefs = "employeeRange")
    private Employee employee;

    @PlanningVariable(valueRangeProviderRefs = "shiftRange")
    private Shift shift;

    // Default constructor (required by OptaPlanner)
    public EmployeeAssignment() {
    }

    // Parameterized constructor
    public EmployeeAssignment(Employee employee, Shift shift) {
        this.employee = employee;
        this.shift = shift;
    }

    // Getters and setters
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }
}