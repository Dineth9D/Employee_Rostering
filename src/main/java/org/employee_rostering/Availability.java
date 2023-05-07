package org.employee_rostering;

public class Availability {
    private String day;
    private String shift;

    public Availability(String day, String shift) {
        this.day = day;
        this.shift = shift;
    }

    public String getDay() {
        return day;
    }

    public String getShift() {
        return shift;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    @Override
    public String toString() {
        return "Availability{" +
                "day='" + day + '\'' +
                ", shift='" + shift + '\'' +
                '}';
    }
}
