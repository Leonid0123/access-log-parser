import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        int count = 0;
        while (true) {
            System.out.println("Укажите путь к файлу:");
            String pathFile = new Scanner(System.in).nextLine();
            File file = new File(pathFile);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();
            if (!fileExists) {
                System.out.println("Указанный файл не существует!");
                continue;
            } else if (isDirectory) {
                System.out.println("Указанный путь является путём к папке!");
                continue;
            }
            System.out.println("Путь указан верно");
            count++;
            System.out.println("Это файл номер " + count);

            try {
                FileReader fileReader = new FileReader(pathFile);
                BufferedReader reader = new BufferedReader(fileReader);
                String line;
                int countLines = 0;
                int countGoogle = 0;
                int countYandex = 0;
                Statistics statistics = new Statistics();
                while ((line = reader.readLine()) != null) {
                    countLines++;
                    int length = line.length();
                    if (length > 1024) {
                        throw new LengthExceededException("В файле строка №" + countLines + " длиной более 1024 символов!");
                    }
                    LogEntry logEntry = new LogEntry(line);
                    UserAgent userAgent = logEntry.getUserAgent();
                    statistics.addEntry(logEntry);
                    //Задание №2. Сравнение объектов. Вывод доли запросов от YandexBot и Googlebot.
                    String googleBotPattern = ".Googlebot.";
                    String yandexBotPattern = ".YandexBot.";
                    Pattern googlePattern = Pattern.compile(googleBotPattern);
                    Pattern yandexPattern = Pattern.compile(yandexBotPattern);
                    Matcher googleMatcher = googlePattern.matcher(userAgent.getLine()); //Из фрагмента User-Agent успешно отделяется часть, которая в случае запросов от соответствующих ботов равна строкам "YandexBot" или "GoogleBot"
                    Matcher yandexMatcher = yandexPattern.matcher(userAgent.getLine());
                    if (googleMatcher.find())
                        countGoogle++; //подсчет количества запросов от GoogleBot
                    if (yandexMatcher.find())
                        countYandex++; //подсчет количества запросов от YandexBot
                }
                System.out.println("Общее количество строк в файле: " + statistics.getTotalEntries());
                System.out.println("Количество строк с Googlebot: " + countGoogle);
                System.out.println("Количество строк с YandexBot: " + countYandex);
                if (statistics.getTotalEntries() != 0) {
                    System.out.println("Доля запросов от GoogleBot: " + (double) countGoogle / (double) statistics.getTotalEntries());
                    System.out.println("Доля запросов от YandexBot: " + (double) countYandex / (double) statistics.getTotalEntries());
                } else throw new IllegalArgumentException("Значение элемента countLines = 0, деление на 0 невозможно!");
                reader.close();
                fileReader.close();
                System.out.println("Средний объем трафика сайта за час: " + statistics.getTrafficRate() + " bytes");
                System.out.println("Все существующие страницы: " + statistics.getPages());
                System.out.println("Статистика операционных систем пользователей сайта: " + statistics.getOsStatistics());
                System.out.println("Все несуществующие сайты: " + statistics.getNotFoundPages());
                System.out.println("Статистика используемых браузеров: " + statistics.getBrowserStatistics());
                System.out.println("Среднее количество посещений сайта за час (без учета ботов): " + statistics.computeAverageVisitsPerHour());
                System.out.println("Среднее количество ошибочных запросов в час: " + statistics.computeAverageErrorsPerHour());
                System.out.println("Средняя посещаемость одним пользователем: " + statistics.computeAverageOneUserVisitsPerHour());
                System.out.println("Пиковая посещаемость сайта (в секунду): " + statistics.calculatePeakVisitsPerSecond());
                System.out.println("Список сайтов, со страниц которых есть ссылки на текущий сайт: " + statistics.getDomains());
                System.out.println("Расчет максимальной посещаемости одним пользователем: " + statistics.calculateMaxUserVisits());
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                System.out.println("Файл не найден, ошибка: " + ex.getMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("Ошибка чтения файла: " + ex.getMessage());
            }
        }
    }
}
