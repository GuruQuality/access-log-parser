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
            //String path = new Scanner(System.in).nextLine();
            String path = "/Users/kate/IdeaProjects_JAVA/access-log-parser/src/test/access.log";
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

                Integer allLine = 0;
                String yandexBot = "YandexBot";
                int quantityYandexBot = 0;
                String googleBot = "Googlebot";
                int quantityGooglebot = 0;
                Statistics statistics = new Statistics();
                while ((line = reader.readLine()) != null) {
                    LogEntry logEntry = null;
                    try {
                        logEntry = new LogEntry(line);
                    } catch (Exception e) {
                        System.out.println("Strange line(skip): " + line);
                        continue;
                    }
                    statistics.addEntry(logEntry);
                    int length = line.length();// Подсчет символов в строке


                    //Проверка фрагмента
                    if (logEntry.userAgent != null) {
                        if (logEntry.userAgent.nameBot != null && logEntry.userAgent.nameBot.equals(yandexBot)) {
                            quantityYandexBot++;
                        }

                        if (logEntry.userAgent.nameBot != null && logEntry.userAgent.nameBot.equals(googleBot)) {
                            quantityGooglebot++;
                        }
                    }
                    //System.out.println(nameBot);

                    allLine++;
                    if (length > 1024) {
                        throw new LimitLineLengthException("В строке более 1024 символов");
                    }
                }
//                System.out.println("Длина самой длинной строки в файле: " + maxLength);
//                System.out.println("Длина самой короткой строки в файле: " + minLength);
                System.out.println("Общее количество строк в файле: " + allLine);
                System.out.println();
                System.out.println("Количество запросов от YandexBot: " + quantityYandexBot);
                System.out.println("Количество запросов от GoogleBot: " + quantityGooglebot);
                System.out.println();
                System.out.println("Доля запросов от YandexBot: " + (float) quantityYandexBot / allLine * 100);
                System.out.println("Доля запросов от GoogleBot: " + (float) quantityGooglebot / allLine * 100);
                System.out.println();
                System.out.println("minTime: " + statistics.minTime);
                System.out.println("maxTime: " + statistics.maxTime);
                System.out.println("totalTraffic: " + statistics.totalTraffic);
                System.out.println("TrafficRate: " + statistics.getTrafficRate());

                break;
            } catch (IOException e) {// Ловим исключения ввода и вывода
                throw new RuntimeException(e);
            } catch (LimitLineLengthException e) {// Ловим исключения по длинне строки
                throw new RuntimeException(e);
            }
        }
    }
}