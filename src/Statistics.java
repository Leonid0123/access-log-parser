import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

public class Statistics {
    private long totalTraffic;
    private ZonedDateTime minTime;
    private ZonedDateTime maxTime;


    public Statistics() {
        totalTraffic = 0;
        minTime = LocalDateTime.MAX.atZone(ZoneId.of("Europe/Moscow"));
        maxTime = LocalDateTime.MIN.atZone(ZoneId.of("Europe/Moscow"));
    }

    public void addEntry(LogEntry log) {
        totalTraffic += Integer.parseInt(log.getDataSize());
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("dd/MMM/yyyy:HH:mm:ss Z")
                .toFormatter(Locale.ENGLISH);
        ZonedDateTime dateTime = ZonedDateTime.parse(log.getDateTime(), formatter);
        if (dateTime.isBefore(minTime))
            minTime = dateTime;
        if (dateTime.isAfter(maxTime))
            maxTime = dateTime;
    }

    public int getTrafficRate() {
        double difTime = (double) Duration.between(minTime, maxTime).toMinutes() / 60;
        return (int) Math.round(totalTraffic / difTime);
    }

    public long getTotalTraffic() {
        return totalTraffic;
    }
}
