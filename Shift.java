public class Shift {
    private String shiftName;
    private String startTime;
    private String endTime;

    public Shift(String name, String start, String end) {
        this.shiftName = name; // name of the shift
        this.startTime = start; // start time of the shift
        this.endTime = end; // end time of the shift
    }

    public String getName() {
        return shiftName;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}
