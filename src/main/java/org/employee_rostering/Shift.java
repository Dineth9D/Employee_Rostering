package org.employee_rostering;

import java.time.LocalTime;
import java.util.Date;

public class Shift {
    private String day;
    private String shift;

    public Shift() {
        // Default constructor for MongoDB mapping
    }

    public Shift(String day, String shift) {
        this.day = day;
        this.shift = shift;
    }
    // Retrieve the day of a shift
    public String getDay() {
        return day;
    }
    // Set the day of a shift
    public void setDay(String day) {
        this.day = day;
    }
    // Retrieve the time of a shift
    public String getShift() {
        return shift;
    }
    // Set the time of a shift
    public void setShift(String shift) {
        this.shift = shift;
    }
}