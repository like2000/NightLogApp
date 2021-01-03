package ch.li.k.nightlog;

public class StatsModel {

    private final String date;
    private final String stop;
    private final String start;
    private final String total;

    StatsModel(String date, String start, String stop, String total) {
        this.date = date;
        this.start = start;
        this.stop = stop;
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public String getStop() {
        return stop;
    }

    public String getStart() {
        return start;
    }

    public String getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "MonitorModel{" +
                "date='" + date + '\'' +
                ", stop='" + stop + '\'' +
                ", start='" + start + '\'' +
                ", total='" + total + '\'' +
                '}';
    }
}
