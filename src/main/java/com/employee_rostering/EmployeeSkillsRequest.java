package com.employee_rostering;

import java.util.List;

public class EmployeeSkillsRequest {
    private String employeeName;
    private List<String> skills;

    public String getEmployeeName() {
        return employeeName;
    }

// --Commented out by Inspection START (5/24/2023 12:50 AM):
//    public void setEmployeeName(String employeeName) {
//        this.employeeName = employeeName;
//    }
// --Commented out by Inspection STOP (5/24/2023 12:50 AM)
    public List<String> getSkills() {
        return skills;
    }
}