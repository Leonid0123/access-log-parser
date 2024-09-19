import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

public class Statistics {
    private long totalTraffic;
    private ZonedDateTime minTime;
    private ZonedDateTime maxTime;
    private final HashSet<String> pages = new HashSet<>();
    private final HashMap<String, Integer> allOS = new HashMap<>();
    private int totalEntries = 0;
    private final HashSet<String> notFoundPages = new HashSet<>();
    private final HashMap<String, Integer> allBrowser = new HashMap<>();

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
        if (log.getHttpCode().equals("200")) {
            pages.add(log.getIp());
        }
        String os = log.getUserAgent().getTypeOS();
        if (allOS.containsKey(os)) {
            int temp = allOS.get(os);
            allOS.put(os, temp + 1);
        } else allOS.put(os, 1);
        if (log.getHttpCode().equals("404"))
            notFoundPages.add(log.getIp());
        String browser = log.getUserAgent().getBrowser();
        if (allBrowser.containsKey(browser)) {
            int temp = allBrowser.get(browser);
            allBrowser.put(browser, temp + 1);
        } else allBrowser.put(browser, 1);
        totalEntries++;
    }

    public int getTrafficRate() {
        double difTime = (double) Duration.between(minTime, maxTime).toMinutes() / 60;
        return (int) Math.round(totalTraffic / difTime);
    }

    public HashMap<String, Double> getOsStatistics() {
        HashMap<String, Double> res = new HashMap<>();
        for (Map.Entry<String, Integer> entry : allOS.entrySet()) {
            String os = entry.getKey();
            int count = entry.getValue();
            double ratio = (double) count / totalEntries;
            res.put(os, ratio);
        }
        return res;
    }

    public HashMap<String, Double> getBrowserStatistics() {
        HashMap<String, Double> res = new HashMap<>();
        for (Map.Entry<String, Integer> entry : allBrowser.entrySet()) {
            String browser = entry.getKey();
            int count = entry.getValue();
            double ratio = (double) count / totalEntries;
            res.put(browser, ratio);
        }
        return res;
    }

    public HashSet<String> getPages() {
        return pages;
    }

    public long getTotalTraffic() {
        return totalTraffic;
    }

    public int getTotalEntries() {
        return totalEntries;
    }

    public HashSet<String> getNotFoundPages() {
        return notFoundPages;
    }
}
