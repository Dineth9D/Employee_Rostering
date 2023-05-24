package com.employee_rostering;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
public class ShiftAssignment {
    @PlanningVariable(valueRangeProviderRefs = "shiftRange")
    private Shift shift;

    @PlanningVariable(valueRangeProviderRefs = "employeeRange")
    private Employee employee;

    // Other properties and methods

    public ShiftAssignment() {
        // Default constructor
    }

    public ShiftAssignment(Employee employee) {
        this.employee = employee;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}