package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        int count = 1;
        String singlePath = null;//На случай передачи адреса файла через командную строку
        if (args.length == 1) {
            singlePath = args[0];
        }
        while (true) {
            String path = singlePath;
            if (singlePath == null) {
                System.out.println("Введите путь к файлу");
                path = new Scanner(System.in).nextLine();
            }
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
                BufferedReader reader = new BufferedReader(fileReader);
                String line;

                Statistics statistics = new Statistics();
                while ((line = reader.readLine()) != null) {

                    if (line.length() > 1024) {
                        throw new LimitLineLengthException("В строке более 1024 символов");
                    }
                    LogEntry logEntry = null;
                    try {
                        logEntry = new LogEntry(line);
                    } catch (Exception e) {
                        System.out.println("Strange line(skip): " + line);
                        continue;
                    }
                    statistics.addEntry(logEntry);

                }
                report(statistics);

                if (singlePath != null) {//Активен режим обработки единичного файла
                    break;
                }
            } catch (IOException e) {// Ловим исключения ввода и вывода
                throw new RuntimeException(e);
            } catch (LimitLineLengthException e) {// Ловим исключения по длинне строки
                throw new RuntimeException(e);
            }
        }
    }

    public static void report(Statistics statistics) {
        //System.out.println("Длина самой длинной строки в файле: " + maxLength);
        //System.out.println("Длина самой короткой строки в файле: " + minLength);
        System.out.println("Общее количество строк в файле: " + statistics.allLine);
        System.out.println();
        System.out.println("Количество запросов от YandexBot: " + statistics.quantityYandexBot);
        System.out.println("Количество запросов от GoogleBot: " + statistics.quantityGooglebot);
        System.out.println();
        System.out.println("Доля запросов от YandexBot: " + (float) statistics.quantityYandexBot / statistics.allLine * 100);
        System.out.println("Доля запросов от GoogleBot: " + (float) statistics.quantityGooglebot / statistics.allLine * 100);
        System.out.println();
        System.out.println("minTime: " + statistics.minTime);
        System.out.println("maxTime: " + statistics.maxTime);
        System.out.println();
        System.out.println("totalTraffic: " + statistics.totalTraffic);
        System.out.println("trafficRate: " + statistics.getTrafficRate());
        System.out.println();
        System.out.println("Список всех существующих страниц сайта (getUniqueUrl): " + statistics.getExistPages());
        System.out.println("Частота встречаемости каждой операционной системы: " + statistics.getOsStatistics());
        HashMap<String, Double> shareOfOperatingSystems = statistics.getShareOfOperatingSystems();
        System.out.println("Доля каждой операционной системы (share of operating systems): " + shareOfOperatingSystems);
        Double sum = shareOfOperatingSystems.values().stream().reduce(Double::sum).orElse(0.0);
        System.out.println("Доля каждой операционной системы (проверка): " + String.format("%.1f", sum));
        //System.out.println();
        //System.out.println("Возрат не существуюших страниц: " + statistics.getNotExistPages());
        System.out.println("Частота встречаемости каждого браузера: " + statistics.getBrowserStatistics());
        HashMap<String, Double> shareOfBrowsers = statistics.getShareOfBrowsers();
        System.out.println("Доля каждого браузера (share of browsers): " + shareOfBrowsers);
        Double sum2 = shareOfBrowsers.values().stream().reduce(Double::sum).orElse(0.0);
        System.out.println("Доля каждого браузера (проверка): " + String.format("%.1f", sum2));
        //System.out.println("Подсчёт среднего количества посещений сайта за час (только пользователи): " + statistics.getAverageUserPerHour());
        //System.out.println("Подсчёта среднего количества ошибочных запросов в час: " + statistics.getAverageUserPerErrorHour());
        //System.out.println("Метод расчёта средней посещаемости одним пользователем: " + statistics.getAverageOneUser());
        //System.out.println();
        //System.out.println("Метод расчёта пиковой посещаемости сайта: " + statistics.getPeakVisitPersecond());
        //System.out.println("Метод, возвращающий список сайтов, со страниц которых есть ссылки на текущий сайт: " + statistics.getRefererDomen());
        //System.out.println("Метод расчёта максимальной посещаемости одним пользователе: " + statistics.getTopViewsOnUser());
    }
}