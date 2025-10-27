package org.example;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        System.out.println("“Введите текст и нажмите <Enter>: ”");
        //читаем из консоли переданную строку
        String text = new Scanner(System.in).nextLine();
        //выводим длину переданного текста
        System.out.print("Длина текста: " + text.length());
    }
}