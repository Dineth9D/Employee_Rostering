public class Shift {
    private String name;
    private String startTime;
    private String endTime;

    public Shift(String name, String startTime, String endTime) {
        this.name = name; // name of the shift
        this.startTime = startTime; // start time of the shift
        this.endTime = endTime; // end time of the shift
    }

    public String getName() {
        return name;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}
