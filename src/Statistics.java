import java.net.URI;
import java.net.URISyntaxException;
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
    private int totalNotBotEntries = 0;
    private int totalFailedResponses = 0;
    private final HashSet<String> notFoundPages = new HashSet<>();
    private final HashMap<String, Integer> allBrowser = new HashMap<>();
    private final HashSet<String> uniqueUsers = new HashSet<>();
    private final HashMap<String, Integer> visitsPerSecond = new HashMap<>();
    private final HashSet<String> domains = new HashSet<>();
    private final HashMap<String, Integer> uniqueUserVisits = new HashMap<>();

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
        if ((log.getHttpCode().charAt(0) == '4') || (log.getHttpCode().charAt(0) == '5'))
            totalFailedResponses++;
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
        if (!log.getUserAgent().isBot()) {
            totalNotBotEntries++;
            String userIP = log.getIp();
            uniqueUsers.add(userIP);
            if (uniqueUserVisits.containsKey(userIP)) {
                int temp = uniqueUserVisits.get(userIP);
                uniqueUserVisits.put(userIP, temp + 1);
            } else uniqueUserVisits.put(userIP, 1);
            String dataTime = log.getDateTime();
            if (visitsPerSecond.containsKey(dataTime)) {
                int temp = visitsPerSecond.get(dataTime);
                visitsPerSecond.put(dataTime, temp + 1);
            } else visitsPerSecond.put(dataTime, 1);
        }
        if (!log.getReferer().equals("-")) {
            try {
                URI uri = new URI(log.getReferer());
                String host = uri.getHost();
                if (host != null)
                    domains.add(host.startsWith("www.") ? host.substring(4) : host);
            } catch (URISyntaxException e) {
                System.out.println("Invalid URL: " + log.getReferer());
            }
        }
    }

    public int getTrafficRate() {
        return (int) Math.round(totalTraffic / getDurationTimeInHour());
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

    public double computeAverageVisitsPerHour() { //Метод подсчёта среднего количества посещений сайта за час
        return totalNotBotEntries / getDurationTimeInHour();
    }

    public double computeAverageErrorsPerHour() { //Метод подсчёта среднего количества ошибочных запросов в час
        return totalFailedResponses / getDurationTimeInHour();
    }

    public double computeAverageOneUserVisitsPerHour() {
        return (double) totalNotBotEntries / uniqueUsers.size();
    }

    public int calculatePeakVisitsPerSecond() {
        return visitsPerSecond.values()
                .stream()
                .max((i1, i2) -> i1.compareTo(i2))
                .orElse(0);
    }

    public int calculateMaxUserVisits() {
        return uniqueUserVisits.values()
                .stream()
                .max((i1, i2) -> i1.compareTo(i2))
                .orElse(0);
    }

    public double getDurationTimeInHour() {
        return (double) Duration.between(minTime, maxTime).toMinutes() / 60;
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

    public HashSet<String> getDomains() {
        return domains;
    }
}
