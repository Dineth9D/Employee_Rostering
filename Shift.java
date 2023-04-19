public class Shift {
    private String shiftName;
    private String startTime;
    private String endTime;

    public Shift(String name, String start, String end) {
        this.shiftName = name;
        this.startTime = start;
        this.endTime = end;
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
