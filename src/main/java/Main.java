import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        System.out.println("Введите первое число");
        int x = new Scanner(System.in).nextInt();
        System.out.println("Введите второе число");
        int y = new Scanner(System.in).nextInt();
        //Расчет результатов
        System.out.println("Cумма чисел: " + (x+y));
        System.out.println("Разность чисел: " + (x-y));
        System.out.println("Произведение чисел: "+ (x*y));
        // Для частного исользуем тип double
        if (y!=0) {
            double quotient = (double) x / y;
            System.out.println("Частное чисел: " + quotient);
        } else {
            System.out.println("Частное: деление на ноль невозможно");
        }
    }
}