package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        int count = 1;
        while (true) {
            System.out.println("Введите путь к файлу");
            String path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean objectExists = file.exists();//Проверяем сущетсвет ли путь
            if (!objectExists) {
                System.out.println("Путь указан не верно");
                continue;
            }
            boolean isDirectory = file.isDirectory();//Проверяем что объект является папкой
            if (isDirectory) {
                System.out.println("Указанный путь является путём к папке, а не к файлу");
                continue;
            }
            System.out.println("Путь указан верно");
            System.out.println("Это файл номер " + count);

            count += 1;

            try {
                FileReader fileReader = new FileReader(path);
                BufferedReader reader =
                        new BufferedReader(fileReader);
                String line;
                Integer maxLength = 0;
                Integer minLength = 1024;
                Integer allLine = 0;
                while ((line = reader.readLine()) != null) {
                    int length = line.length();// Подсчет символов в строке
                    //System.out.println(length);
                    maxLength = Math.max(maxLength, length);
                    minLength = Math.min(minLength, length);
                    allLine++;
                    if (length > 1024) {
                        throw new LimitLineLengthException("В строке более 1024 символов");
                    }
                }
                System.out.println("Длина самой длинной строки в файле: " + maxLength);
                System.out.println("Длина самой короткой строки в файле: " + minLength);
                System.out.println("Общее количество строк в файле: " + allLine);
            } catch (IOException e) {// Ловим исключения ввода и вывода
                throw new RuntimeException(e);
            } catch (LimitLineLengthException e) {// Ловим исключения по длинне строки
               throw new RuntimeException(e);
            }
        }
    }
}