package org.example;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;

public class Statistics {
    LocalDateTime minTime;
    LocalDateTime maxTime;
    Long totalTraffic = 0L;
    //адреса страниц сайта с кодом ответа 200
    HashSet<String> hashSetExistPages = new HashSet<>();
    //статистика операционных систем пользователей сайта
    HashMap<String, Integer> hashMapOsStatistics = new HashMap<>();
    //список всех несуществующих страниц сайта
    HashSet<String> hashSetNotExistPages = new HashSet<>();
    // Статистика частоты браузеров
    HashMap<String, Integer> hashMapBrowserStatistics = new HashMap<>();
    Statistics() {

    }

    public void addEntry(LogEntry logEntry) {
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
            result.put(key,(double) value / allBrowsers );
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
            result.put(key,(double) value / allOs );
            //System.out.println(key + " = " + value);
            //System.out.println(key + " = " + (double) value / allOs * 100);
        }
        return result;
    }

    public Long getTrafficRate() {
        Duration duration = Duration.between(minTime, maxTime);
        Long divHours = duration.toHours();
        Long trafficRate = totalTraffic / divHours;
        return trafficRate;
    }

    public HashSet<String> getUniqueUrl() {
        return hashSetExistPages;
    }

    public HashSet<String> getNotExistPages(){
        return hashSetNotExistPages;
    }
}
