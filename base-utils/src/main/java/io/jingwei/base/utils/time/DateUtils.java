package io.jingwei.base.utils.time;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {

    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    //获取当前时间(字符串格式)
    public static String getCurrentDateTime(String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.now().format(formatter);
    }

    //获取昨天时间格式
    public static String getYesterdayByFormat(String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.now().minusDays(1).format(formatter);
    }

    //获取从1970年1月1日到现在的秒数
    public static Long getSecond(String dateTime, String format) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, dtf);
        Long second = localDateTime.toEpochSecond(ZoneOffset.of("+8"));
        return second;
    }

    //LocalDateTime转String
    public static String getLocalDateTimeToStr(LocalDateTime localDateTime, String format) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
        return localDateTime.format(dtf);
    }

    //获取从1970年1月1日到现在的秒数转LocalDateTime
    public static LocalDateTime getSecondToLocalDateTime(Long second) {
        Instant instant = Instant.ofEpochSecond(second);
        ZoneId zoneId = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zoneId);
    }

    //获取从1970年1月1日到现在的秒数转字符串格式
    public static String getSecondToStr(long second, String format) {
        return getLocalDateTimeToStr(getSecondToLocalDateTime(second), format);
    }

    //获取从1970年1月1日到现在的毫秒数转LocalDateTime
    public static LocalDateTime getDateTimeOfTimestamp(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zoneId = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zoneId);
    }
}
