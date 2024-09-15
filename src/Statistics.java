import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

public class Statistics {
    private int totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;


    public Statistics() {
        totalTraffic = 0;
        minTime = LocalDateTime.now();
        maxTime = LocalDateTime.now();
    }

    public void addEntry(LogEntry log) {
        totalTraffic = Integer.parseInt(log.getDataSize());
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()//протестировать ОТ
                .appendPattern("dd/MMM/yyyy:HH:mm:ss")
                .appendPattern(" Z") // пробел перед "Z" учитывает пробел перед временной зоной в исходной строке
                .toFormatter(Locale.ENGLISH);
        LocalDateTime dateTime = LocalDateTime.parse(log.getDateTime(),formatter);
        if (dateTime.isBefore(minTime))
            minTime = dateTime;
        if (dateTime.isAfter(maxTime))
            maxTime = dateTime;//протестировать ДО
    }

    public void getTrafficRate() {

    }
}
