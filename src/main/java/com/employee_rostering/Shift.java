package com.employee_rostering;

import java.time.LocalTime;
import java.util.List;

public class Shift {
    private String day;
    private String shift;
    private String requiredSkill;
    private String startTime;
    private String endTime;
    private List<String> requiredSkills;


    public Shift(String startTime, String endTime, List<String> requiredSkills) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.requiredSkills = requiredSkills;
    }

    // Retrieve the day of a shift
    public String getDay() {
        return day;
    }

    // Retrieve the time of a shift
    public String getShift() {
        return shift;
    }

    // Retrieve the required skill for the shift
    public List<String> getRequiredSkills() {
        return requiredSkills;
    }
}