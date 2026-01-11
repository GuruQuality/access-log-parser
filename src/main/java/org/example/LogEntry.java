package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

public class LogEntry {
    final private String ip;

    public String getIp() {
        return ip;
    }

    final LocalDateTime dataTime;

    public LocalDateTime getDataTime() {
        return dataTime;
    }

    final String method;

    public String getMethod() {
        return method;
    }

    final String path;

    public String getPath() {
        return path;
    }

    final String httpVersion;

    public String getHttpVersion() {
        return httpVersion;
    }

    Integer httpCode;

    public Integer getHttpCode() {
        return httpCode;
    }

    final Integer size;

    public Integer getSize() {
        return size;
    }

    final String referer;

    public String getReferer() {
        return referer;
    }

    UserAgent userAgent;

    LogEntry(String line) {
        int inexIpEnd = line.indexOf(' ');
        ip = line.substring(0, inexIpEnd);

        int startIndexDataTime = line.indexOf('[');
        int endIndexDataTime = line.indexOf(']');
        String dataTimeText = line.substring(startIndexDataTime + 1, endIndexDataTime);

        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()  // игнорировать регистр
                .appendPattern("dd/MMM/yyyy:HH:mm:ss Z")  // Z для смещения часового пояса
                .toFormatter(Locale.ENGLISH);  // месяц на английском

        dataTime = LocalDateTime.parse(dataTimeText,formatter);

        int startIndexMethodAndPath = line.indexOf('"');
        int endIndexMethodAndPath = line.indexOf('"', startIndexMethodAndPath + 1);
        String methodAndPathAndVersion = line.substring(startIndexMethodAndPath + 1, endIndexMethodAndPath);
        String[] parts = methodAndPathAndVersion.split(" ");
        method = parts[0];
        path = parts[1];
        httpVersion = parts[2];
        String httpCodeText = line.substring(endIndexMethodAndPath + 2, endIndexMethodAndPath + 5);
        httpCode = Integer.parseInt(httpCodeText);

        int startIndexSize = endIndexMethodAndPath + 6;
        int endIndexSize = line.indexOf(" ", startIndexSize);
        String sizeText = line.substring(startIndexSize, endIndexSize);
        size = Integer.parseInt(sizeText);

        int startIndexRefer = endIndexSize + 2;
        int endIndexRefer = line.indexOf('"', startIndexRefer);
        referer = line.substring(startIndexRefer, endIndexRefer);

        String userAgentText = line.substring(endIndexRefer + 3, line.length() - 1);
        if (!userAgentText.equals("-")) {
            userAgent = new UserAgent(userAgentText);
        }else {
            userAgent = new UserAgent();
        }
    }
}
