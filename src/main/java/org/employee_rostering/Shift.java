package org.employee_rostering;

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
}