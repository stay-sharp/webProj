package com.ruiyang.du.demo;


import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;


public class LocalDateDemo {

    public static void main(String[] args) {

        duration();
    }

    private static void localDate(){
        //初始化
        LocalDate now = LocalDate.now();
        System.out.println(now);
        LocalDate theDay = LocalDate.of(2020,9,15);
        System.out.println(theDay);
    }

    private static void localDateTime(){

        LocalDateTime now = LocalDateTime.now();
        //格式化
        String formatedNow = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println(formatedNow);

        LocalDateTime time = LocalDateTime.of(2020, 9, 15, 12, 30, 50);
        System.out.println(time);

        //减一天，再加一小时
        LocalDateTime minusDays = time.minusDays(1).plus(1, ChronoUnit.HOURS);
        System.out.println(minusDays);

        //转换成Date: 先指定时区，然后转成秒数，然后转成Date
        Date from = Date.from(minusDays.atZone(ZoneId.systemDefault()).toInstant());
        System.out.println(from);

    }

    private static void duration(){
        //时间间隔
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime graduate = LocalDateTime.of(2015, 7, 1, 8, 0, 0);
        Duration between = Duration.between(graduate, now);
        System.out.println(between.toDays());
        System.out.println(between.toHours());
        System.out.println(between.toMinutes());
    }


}
