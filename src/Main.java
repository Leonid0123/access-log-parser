import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        //Задание по теме "Циклы"
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
                String logPattern =
                        "(\\d+\\.\\d+\\.\\d+\\.\\d+)" + // IP-адрес
                                " (\\S*) (\\S*) " + // Два пропущенных свойства
                                "\\[(.+?)\\] " + // Дата и время запроса
                                "\"(\\w+ .+? HTTP/\\d\\.\\d\") " + // Метод и путь запроса
                                "(\\d{3}) " + // Код HTTP-ответа
                                "(\\d+) " + // Размер отданных данных
                                "\"([^\"]*)\" " + // Referer
                                "\"([^\"]*)\""; // User-Agent
                Pattern pattern = Pattern.compile(logPattern);
                String line;
                int countLines = 0;
                int countGoogle = 0;
                int countYandex = 0;
                while ((line = reader.readLine()) != null) {
                    countLines++;
                    int length = line.length();
                    if (length > 1024) {
                        throw new LengthExceededException("В файле строка №" + countLines + " длиной более 1024 символов!");
                    }
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        String ip = matcher.group(1);
                        String prop1 = matcher.group(2);
                        String prop2 = matcher.group(3);
                        String dateTime = matcher.group(4);
                        String path = matcher.group(5);
                        String httpCode = matcher.group(6);
                        String dataSize = matcher.group(7);
                        String referer = matcher.group(8);
                        String userAgent = matcher.group(9); //В приложении выделяется фрагмент User-Agent

                        //userAgent
                        String googleBotPattern = ".Googlebot.";
                        String yandexBotPattern = ".YandexBot.";
                        Pattern googlePattern = Pattern.compile(googleBotPattern);
                        Pattern yandexPattern = Pattern.compile(yandexBotPattern);
                        Matcher googleMatcher = googlePattern.matcher(userAgent); //Из фрагмента User-Agent успешно отделяется часть, которая в случае запросов от соответствующих ботов равна строкам "YandexBot" или "GoogleBot"
                        Matcher yandexMatcher = yandexPattern.matcher(userAgent);
                        if (googleMatcher.find())
                            countGoogle++; //Программа корректно подсчитывает долю запросов от GoogleBot
                        if (yandexMatcher.find())
                            countYandex++; //Программа корректно подсчитывает долю запросов от YandexBot
                    } else
                        System.out.println("Не найдено совпадений в строке: " + line);
                }
                System.out.println("Общее количество строк в файле: " + countLines);
                System.out.println("Количество строк с Googlebot: " + countGoogle);
                System.out.println("Количество строк с YandexBot: " + countYandex);
                if (countLines != 0) {
                    System.out.println("Доля запросов от GoogleBot: " + (double) countGoogle / (double) countLines);
                    System.out.println("Доля запросов от YandexBot: " + (double) countYandex / (double) countLines);
                } else throw new IllegalArgumentException("Значение элемента countLines = 0, деление на 0 невозможно!");
                reader.close();
                fileReader.close();
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
