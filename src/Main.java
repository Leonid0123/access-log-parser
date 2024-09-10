import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //Задание по теме "Циклы"
        int count = 0;
        while (true) {
            System.out.println("Укажите путь к файлу:");
            String path = new Scanner(System.in).nextLine();
            File file = new File(path);
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

            //Обработка исключений
            try {
                FileReader fileReader = new FileReader(path);
                BufferedReader reader = new BufferedReader(fileReader);
                String line;
                int countLines = 0;
                int minLength = 2147483647; //присваиваем максимальное значение в диапазоне int
                int maxLength = 0;
                while ((line = reader.readLine()) != null) {
                    countLines++;
                    int length = line.length();
                    if (length > 1024) {
//                        throw new RuntimeException("В файле строка №" + countLines + " длиной более 1024 символов!"); //1 способ. Исключение через объект класса RuntimeException.
                        throw new LengthExceededException("В файле строка №" + countLines + " длиной более 1024 символов!"); //2 способ. Создан отдельный класс исключения.
                    }
                    if (length < minLength)
                        minLength = length;
                    if (length > maxLength)
                        maxLength = length;
                }
                System.out.println("Общее количество строк в файле: " + countLines);
                System.out.println("Длина самой длинной строки в файле: " + maxLength);
                System.out.println("Длина самой короткой строки в файле: " + minLength);
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
        //Задание по теме ""Синтаксис языка" и “Базовые типы данных”"
//        System.out.println("Введите первое число: ");
//        int firstNumber = new Scanner(System.in).nextInt();
//        System.out.println("Введите второе число: ");
//        int secondNumber = new Scanner(System.in).nextInt();
//        int sum = firstNumber + secondNumber;
//        int diff = firstNumber - secondNumber;
//        int product = firstNumber * secondNumber;
//        double quotient = (double) firstNumber / secondNumber;
//        System.out.println("Сумма: " + sum);
//        System.out.println("Разность: " + diff);
//        System.out.println("Произведение: " + product);
//        System.out.println("Частное: " + quotient);
    }
}
