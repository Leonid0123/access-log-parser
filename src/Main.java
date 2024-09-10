import java.io.File;
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
