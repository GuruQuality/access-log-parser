package org.example;

import java.time.Duration;
import java.time.LocalDateTime;

public class Statistics {
    LocalDateTime minTime;
    LocalDateTime maxTime;
    Long totalTraffic = 0L;

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
    }

    public Long getTrafficRate() {
        Duration duration = Duration.between(minTime, maxTime);
        Long divHours = duration.toHours();
        Long trafficRate = totalTraffic/divHours;
        return trafficRate;
    }
}
