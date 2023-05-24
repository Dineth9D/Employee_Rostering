package com.employee_rostering;

import java.util.List;

public class Shift {
    private String day;
    private String shift;
    private List<String> requiredSkills;

    public Shift(String day, String shift, List<String> requiredSkills) {
        this.day = day;
        this.shift = shift;
        this.requiredSkills = requiredSkills;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public List<String> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(List<String> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }
}