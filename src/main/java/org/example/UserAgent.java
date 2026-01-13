package org.example;

public class UserAgent {
    String op;
    String browser;
    String nameBot = "";

    UserAgent(String line) {
        ///
        /// Парсинг наименования браузера
        ///
        String[] parts = line.split("/");
        // User-Agent будет в последнем элементе массива
        browser = parts[0];

        ///
        /// Парсинг наименования операционной системы
        ///

        Integer startOp = line.indexOf("(");//ищем индкекс нашего символа в строке
        Integer endOp = line.indexOf(";");
        if (endOp == -1) {
            endOp = line.indexOf(")");
        }
        if (endOp == -1 || startOp > endOp) {
            //System.out.println("Strange User-Agent: " + line);
        } else {
            op = line.substring(startOp + 1, endOp);
        }
        ///
        /// Парсинг наименования Бота
        ///
        int length = line.length();// Подсчет символов в строке

        // 1. Ищем содержимое в первых скобках
        int startIndex = line.lastIndexOf('(');
        int endIndex = line.lastIndexOf(')');
        if (startIndex == -1 || endIndex == -1) {
            return;
        }
        if (startIndex > endIndex) {
            System.out.println(line);
            return;
        }

        // 1.1 Извлекаем содержимое первых скобок
        String firstBrackets = line.substring(startIndex, endIndex);

        // 2. Разделение по точке с запятой
        parts = firstBrackets.split(";");

        // 3. Очищаем все фрагменты от пробелов
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim(); // Удаляем пробелы в начале и конце
        }

        //4. Берем второй фрагмент;
        if (parts.length >= 2) {
            String fragment = parts[1];

            //5. Отделим в этом фрагменте часть до слэша
            int slashIndex = fragment.indexOf('/');
            nameBot = fragment;
            if (slashIndex != -1) {
                nameBot = fragment.substring(0, slashIndex);//фрагмент до слеша
            }
        }

    }

    /**
     *     Ой бот
     */
    public boolean isBot(){
        return browser != null && browser.contains("bot");
    }

    UserAgent() {
        op = null;
        browser = null;
        nameBot = null;
    }

}
