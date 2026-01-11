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
    HashSet<String> hashSet = new HashSet<>();
    //статистика операционных систем пользователей сайта
    HashMap<String, Integer> hashMap = new HashMap<>();

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
        if (logEntry.httpCode == 200) {
            hashSet.add(logEntry.path);
        }
        //Подсчет количества op
        if (logEntry.userAgent.op != null) {//проеврка наличия инфы об оп
            if (hashMap.containsKey(logEntry.userAgent.op)) {//проеврка в hashMap наличия ключа с нашим оп
                int countOp = hashMap.get(logEntry.userAgent.op);//из hashMap получили наше значение по ключу оп
                hashMap.put(logEntry.userAgent.op, countOp + 1);//увеличили значение на 1
            } else {
                hashMap.put(logEntry.userAgent.op, 1);//создали запись по ключу оп со значением - 1
            }
        }
    }

    public HashMap<String, Double> getShareOfOperatingSystems() {
        HashMap<String, Double> result = new HashMap<>();
        Long allOs = 0l;
        for (Integer value : hashMap.values()) {
            allOs += value;
        }
        //System.out.println("allOs " + allOs);
        for (String key : hashMap.keySet()) {
            Integer value = hashMap.get(key);
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
        return hashSet;
    }
}
