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
//                Integer maxLength = 0;
//                Integer minLength = 1024;
                Integer allLine = 0;
                String yandexBot = "YandexBot";
                int quantityYandexBot = 0;
                String googleBot = "Googlebot";
                int quantityGooglebot = 0;
                while ((line = reader.readLine()) != null) {
                    int length = line.length();// Подсчет символов в строке

                    // 1. Ищем содержимое в первых скобках
                    int startIndex = line.lastIndexOf('(');
                    int endIndex = line.lastIndexOf(')');
                    if (startIndex == -1 || endIndex == -1) {
                        continue;
                    }
                    if (startIndex > endIndex) {
                        System.out.println(line);
                        break;
                    }
//                    //System.out.println(length);
//                    maxLength = Math.max(maxLength, length);
//                    minLength = Math.min(minLength, length);

                    // 1.1 Извлекаем содержимое первых скобок
                    String firstBrackets = line.substring(startIndex, endIndex);

                    // 2. Разделение по точке с запятой
                    String[] parts = firstBrackets.split(";");

                    // 3. Очищаем все фрагменты от пробелов
                    for (int i = 0; i < parts.length; i++) {
                        parts[i] = parts[i].trim(); // Удаляем пробелы в начале и конце
                    }

                    //4. Берем второй фрагмент;
                    if (parts.length >= 2) {
                        String fragment = parts[1];

                        //5. Отделим в этом фрагменте часть до слэша
                        int slashIndex = fragment.indexOf('/');
                        String nameBot = fragment;
                        if (slashIndex != -1) {
                            nameBot = fragment.substring(0, slashIndex);//фрагмент до слеша
                        }

                        //Проверка фрагмента
                        if (nameBot.equals(yandexBot)) {
                            quantityYandexBot++;
                        }

                        if (nameBot.equals(googleBot)) {
                            quantityGooglebot++;
                        }
                        //System.out.println(nameBot);
                    }

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

                break;
            } catch (IOException e) {// Ловим исключения ввода и вывода
                throw new RuntimeException(e);
            } catch (LimitLineLengthException e) {// Ловим исключения по длинне строки
                throw new RuntimeException(e);
            }
        }
    }
}