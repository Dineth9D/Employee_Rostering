package com.employee_rostering;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;

import java.util.List;

public class EmployeeRoster {
    @PlanningEntityCollectionProperty
    private List<EmployeeAssignment> employeeAssignments;

    public EmployeeRoster() {
    }

    public List<EmployeeAssignment> getEmployeeAssignments() {
        return employeeAssignments;
    }

    public void setEmployeeAssignments(List<EmployeeAssignment> employeeAssignments) {
        this.employeeAssignments = employeeAssignments;
    }
}