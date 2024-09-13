import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogEntry {
    private final String ip;
    private final String prop1;
    private final String prop2;
    private final String dateTime;
    private final HttpMethods method;
    private final String path;
    private final String httpCode;
    private final String dataSize;
    private final String referer;
    private final String userAgent;

    public LogEntry(String line) {
        String logPattern =
                "(\\d+\\.\\d+\\.\\d+\\.\\d+)" + // IP-адрес
                        " (\\S*) (\\S*) " + // Два пропущенных свойства
                        "\\[(.+?)\\] " + // Дата и время запроса
                        "\"(\\w+) (.+? HTTP/\\d\\.\\d\") " + // Метод и путь запроса
                        "(\\d{3}) " + // Код HTTP-ответа
                        "(\\d+) " + // Размер отданных данных
                        "\"([^\"]*)\" " + // Referer
                        "\"([^\"]*)\""; // User-Agent
        Pattern pattern = Pattern.compile(logPattern);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            this.ip = matcher.group(1);
            this.prop1 = matcher.group(2);
            this.prop2 = matcher.group(3);
            this.dateTime = matcher.group(4);
            this.method = HttpMethods.valueOf(matcher.group(5));
            this.path = matcher.group(6);
            this.httpCode = matcher.group(7);
            this.dataSize = matcher.group(8);
            this.referer = matcher.group(9);
            this.userAgent = matcher.group(10);
        } else
            throw new RuntimeException("Не найдено совпадений в строке: " + line);
    }

    public String getIp() {
        return ip;
    }

    public String getProp1() {
        return prop1;
    }

    public String getProp2() {
        return prop2;
    }

    public String getDateTime() {
        return dateTime;
    }

    public HttpMethods getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getHttpCode() {
        return httpCode;
    }

    public String getDataSize() {
        return dataSize;
    }

    public String getReferer() {
        return referer;
    }

    public String getUserAgent() {
        return userAgent;
    }
}
