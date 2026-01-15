package org.example;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Stream;

public class Statistics {
    LocalDateTime minTime;
    LocalDateTime maxTime;
    Long totalTraffic = 0L;
    /**
     * Адреса страниц сайта с кодом ответа 200
     */
    HashSet<String> hashSetExistPages = new HashSet<>();
    /**
     * Cтатистика операционных систем пользователей сайта
     */
    HashMap<String, Integer> hashMapOsStatistics = new HashMap<>();
    /**
     * Cписок всех несуществующих страниц сайта
     */
    HashSet<String> hashSetNotExistPages = new HashSet<>();
    /**
     * Статистика частоты браузеров
     */
    HashMap<String, Integer> hashMapBrowserStatistics = new HashMap<>();
    /**
     * Общее количество запросов от пользователей
     */
    Integer requestFromUser = 0;
    HashSet<String> uniqueIp = new HashSet<>();
    //
    String yandexBot = "YandexBot";
    int quantityYandexBot = 0;
    String googleBot = "Googlebot";
    int quantityGooglebot = 0;

    Integer allLine = 0;

    List<LogEntry> listLogEntry = new ArrayList<>();
    // Статистика посещений сайта в секунду, ключи - секунды
    HashMap<Long, Integer> hashMapVisitPersecond = new HashMap<>();
    // Спсиок сайтов
    HashSet<String> hashSetDomen = new HashSet<>();
    // Статистика посещений сайта по IP
    HashMap<String, Integer> hashMapVisitByIp = new HashMap<>();

    Statistics() {

    }

    public void addEntry(LogEntry logEntry) {
        listLogEntry.add(logEntry);
        totalTraffic += logEntry.size;
        if (minTime == null || logEntry.dataTime.compareTo(minTime) < 0) {
            minTime = logEntry.dataTime;
        }
        if (maxTime == null || logEntry.dataTime.compareTo(maxTime) > 0) {
            maxTime = logEntry.dataTime;
        }
        // Создание hashSetExistPages
        if (logEntry.httpCode == 200) {
            hashSetExistPages.add(logEntry.path);
        }
        //Подсчет количества op
        if (logEntry.userAgent.op != null) {//проеврка наличия инфы об оп
            if (hashMapOsStatistics.containsKey(logEntry.userAgent.op)) {//проеврка в hashMap наличия ключа с нашим оп
                int countOp = hashMapOsStatistics.get(logEntry.userAgent.op);//из hashMap получили наше значение по ключу оп
                hashMapOsStatistics.put(logEntry.userAgent.op, countOp + 1);//увеличили значение на 1
            } else {
                hashMapOsStatistics.put(logEntry.userAgent.op, 1);//создали запись по ключу оп со значением - 1
            }
        }

        // Создание hashSetNotExistPages
        if (logEntry.httpCode == 404) {
            hashSetNotExistPages.add(logEntry.path);
        }

        //Подсчет количества браузеров
        if (logEntry.userAgent.browser != null) {//проеврка наличия инфы об оп
            if (hashMapBrowserStatistics.containsKey(logEntry.userAgent.browser)) {//проеврка в hashMap наличия ключа с нашим оп
                int countBrowser = hashMapBrowserStatistics.get(logEntry.userAgent.browser);//из hashMap получили наше значение по ключу оп
                hashMapBrowserStatistics.put(logEntry.userAgent.browser, countBrowser + 1);//увеличили значение на 1
            } else {
                hashMapBrowserStatistics.put(logEntry.userAgent.browser, 1);//создали запись по ключу оп со значением - 1
            }
        }
        // Подсчет кол-ва пользователей
        uniqueIp.add(logEntry.getIp());
        if (!logEntry.userAgent.isBot()) {
            requestFromUser++;
        }
        //uniqueIp.size();//кол-во пользователей

        //Проверка фрагмента
        if (logEntry.userAgent != null) {
            if (logEntry.userAgent.nameBot != null && logEntry.userAgent.nameBot.equals(yandexBot)) {
                quantityYandexBot++;
            }

            if (logEntry.userAgent.nameBot != null && logEntry.userAgent.nameBot.equals(googleBot)) {
                quantityGooglebot++;
            }
        }
        allLine++;
        // Cтатистика посещаемости за секунду
        long second = logEntry.dataTime.toEpochSecond(ZoneOffset.UTC);
        if (!logEntry.userAgent.isBot()) {
            if (hashMapVisitPersecond.containsKey(second)) {//проеврка в hashMap наличия ключа с нашим HashMap
                int countVisite = hashMapVisitPersecond.get(second);//из hashMap получили наше значение по ключу
                hashMapVisitPersecond.put(second, countVisite + 1);//увеличили значение на 1
            } else {
                hashMapVisitPersecond.put(second, 1);//создали запись по ключу оп со значением - 1
            }
        }
        // Список доменов
        String referer = logEntry.referer;
        try {
            URL url = new URL(referer);
            String domain = url.getHost();
            hashSetDomen.add(domain);
            //System.out.println("Домен: " + domain);// nova-news.ru
        } catch (MalformedURLException e) {
            //System.out.println(referer);
        }
        // Cтатистика посещаемости по IP
        String ip = logEntry.getIp();
        if (!logEntry.userAgent.isBot() && !ip.isEmpty()) {
            if (hashMapVisitByIp.containsKey(ip)) {//проеврка в hashMap наличия ключа с нашим HashMap
                int countVisite = hashMapVisitByIp.get(ip);//из hashMap получили наше значение по ключу
                hashMapVisitByIp.put(ip, countVisite + 1);//увеличили значение на 1
            } else {
                hashMapVisitByIp.put(ip, 1);//создали запись по ключу оп со значением - 1
            }
        }
    }

    public HashMap<String, Double> getShareOfBrowsers() {
        HashMap<String, Double> result = new HashMap<>();
        Long allBrowsers = 0l;
        for (Integer value : hashMapBrowserStatistics.values()) {
            allBrowsers += value;
        }
        //System.out.println("allBrowsers " + allBrowsers);
        for (String key : hashMapBrowserStatistics.keySet()) {
            Integer value = hashMapBrowserStatistics.get(key);
            result.put(key, (double) value / allBrowsers);
        }
        return result;
    }

    public HashMap<String, Double> getShareOfOperatingSystems() {
        HashMap<String, Double> result = new HashMap<>();
        Long allOs = 0l;
        for (Integer value : hashMapOsStatistics.values()) {
            allOs += value;
        }
        //System.out.println("allOs " + allOs);
        for (String key : hashMapOsStatistics.keySet()) {
            Integer value = hashMapOsStatistics.get(key);
            result.put(key, (double) value / allOs);
            //System.out.println(key + " = " + value);
            //System.out.println(key + " = " + (double) value / allOs * 100);
        }
        return result;
    }

    public Long getTrafficRate() {
        Long trafficRate = totalTraffic / getDivHours();
        return trafficRate;
    }

    public Long getDivHours() {
        Duration duration = Duration.between(minTime, maxTime);
        Long divHours = duration.toHours();
        return divHours;
    }

    public HashSet<String> getExistPages() {
        return hashSetExistPages;
    }

    public HashMap<String, Integer> getOsStatistics(){
        return hashMapOsStatistics;
    }

    public HashSet<String> getNotExistPages() {
        return hashSetNotExistPages;
    }

    //Метод подсчёта среднего количества посещений сайта за час
    public Long getAverageUserPerHour() {
        Stream<LogEntry> stream =
                listLogEntry
                        .stream()
                        .filter((logEntry) -> (!logEntry.userAgent.isBot()));
        //.count();
        Long result = stream.count();
        return result / getDivHours();
    }

    //Метод подсчёта среднего количества ошибочных запросов в час.
    public Long getAverageUserPerErrorHour() {
        Stream<LogEntry> stream =
                listLogEntry
                        .stream()
                        .filter((logEntry) -> (logEntry.httpCode >= 400 && logEntry.httpCode < 600));
        //.count();
        Long result = stream.count();
        return result / getDivHours();
    }

    //Метод расчёта средней посещаемости одним пользователем.
    public Long getAverageOneUser() {
        Stream<LogEntry> stream =
                listLogEntry
                        .stream()
                        .filter((logEntry) -> (!logEntry.userAgent.isBot()));
        //.count();
        Long totalUsers = stream.count();

        Stream<String> stream2 =
                listLogEntry
                        .stream()
                        .filter((logEntry) -> (!logEntry.userAgent.isBot()))
                        .map((logEntry) -> logEntry.getIp())
                        .distinct();
        //.count();
        Long uniqueUsers = stream2.count();
        return totalUsers / uniqueUsers;
    }

    // Метод расчёта пиковой посещаемости сайта
    public Integer getPeakVisitPersecond() {
        //System.out.println(hashMapVisitPersecond.entrySet().stream().sorted(Map.Entry.comparingByValue()).toList());//Вывод посещаемости сайта
        Optional<Integer> result = hashMapVisitPersecond.values().stream().max(Integer::compareTo);
        if (result.isEmpty()) {
            return 0;
        } else {
            return result.get();
        }
    }

    //Метод, возвращающий список сайтов, со страниц которых есть ссылки на текущий сайт
    public HashSet<String> getRefererDomen() {
        return hashSetDomen;
    }

    //Метод расчёта максимальной посещаемости одним пользователем
    public Integer getTopViewsOnUser() {
        //System.out.println(hashMapVisitByIp);
        Optional<Integer> result = hashMapVisitByIp.values().stream().max(Integer::compareTo);
        if (result.isEmpty()) {
            return 0;
        } else {
            return result.get();
        }
    }
}
