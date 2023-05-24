package com.employee_rostering;

import java.util.List;

public class EmployeeSkillsRequest {
    private String employeeName;
    private List<String> skills;

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }
}