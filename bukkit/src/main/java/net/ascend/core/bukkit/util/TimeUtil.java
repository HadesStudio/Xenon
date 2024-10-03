package net.ascend.core.bukkit.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtil {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm aaa");

    public static String formatIntoCalendarString(Date date) {
        return (dateFormat.format(date));
    }

    public static long parse(String input) {
        if (input == null || input.isEmpty()) {
            return -1L;
        }

        long result = 0L;
        StringBuilder number = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (Character.isDigit(c)) {
                number.append(c);
            } else {
                String str;

                if (Character.isLetter(c) && !(str = number.toString()).isEmpty()) {
                    result += convert(Integer.parseInt(str), c);
                    number = new StringBuilder();
                }
            }
        }

        return result;
    }

    private static long convert(int value, char unit) {
        switch (unit) {
            case 'y':
                return value * TimeUnit.DAYS.toMillis(365L);
            case 'M':
                return value * TimeUnit.DAYS.toMillis(30L);
            case 'd':
                return value * TimeUnit.DAYS.toMillis(1L);
            case 'h':
                return value * TimeUnit.HOURS.toMillis(1L);
            case 'm':
                return value * TimeUnit.MINUTES.toMillis(1L);
            case 's':
                return value * TimeUnit.SECONDS.toMillis(1L);
        }

        return -1L;
    }

    public static String formatTimeMillis(long millis) {
        long seconds = millis / 1000L;

        if (seconds <= 0) {
            return "0 seconds";
        }

        long minutes = seconds / 60;
        seconds = seconds % 60;
        long hours = minutes / 60;
        minutes = minutes % 60;
        long day = hours / 24;
        hours = hours % 24;
        long years = day / 365;
        day = day % 365;

        StringBuilder time = new StringBuilder();

        if (years != 0) {
            time.append(years).append(years == 1 ? " year " : " years ");
        }

        if (day != 0) {
            time.append(day).append(day == 1 ? " day " : " days ");
        }

        if (hours != 0) {
            time.append(hours).append(hours == 1 ? " hour " : " hours ");
        }

        if (minutes != 0) {
            time.append(minutes).append(minutes == 1 ? " minute " : " minutes ");
        }

        if (seconds != 0) {
            time.append(seconds).append(seconds == 1 ? " second " : " seconds ");
        }

        return time.toString().trim();
    }


    public static String getMoreDetailedTime(long millis) {
        long seconds = millis / 1000L;

        if (seconds <= 0) {
            return "0 seconds";
        }

        long minutes = seconds / 60;
        seconds = seconds % 60;
        long hours = minutes / 60;
        minutes = minutes % 60;
        long day = hours / 24;
        hours = hours % 24;
        long years = day / 365;
        day = day % 365;

        StringBuilder time = new StringBuilder();

        if (years != 0) {
            time.append(years).append(years == 1 ? " year" : " years").append(day == 0 ? "" : ", ");
        }

        if (day != 0) {
            time.append(day).append(day == 1 ? " day" : " days").append(hours == 0 ? "" : ", ");
        }

        if (hours != 0) {
            time.append(hours).append(hours == 1 ? " hour" : " hours").append(minutes == 0 ? "" : ", ");
        }

        if (minutes != 0) {
            time.append(minutes).append(minutes == 1 ? " minute" : " minutes").append(seconds == 0 ? "" : ", ");
        }

        if (seconds != 0) {
            time.append(seconds).append(seconds == 1 ? " second" : " seconds");
        }

        return time.toString().trim();
    }

    public static String getDetailedTime(long millis) {
        long seconds = millis / 1000L;

        if (seconds <= 0) {
            return "0 seconds";
        }

        long minutes = seconds / 60;
        seconds = seconds % 60;
        long hours = minutes / 60;
        minutes = minutes % 60;
        long day = hours / 24;
        hours = hours % 24;
        long years = day / 365;
        day = day % 365;

        StringBuilder time = new StringBuilder();

        if (years != 0) {
            time.append(years).append(years == 1 ? "y" : "y").append(day == 0 ? "" : ",");
        }

        if (day != 0) {
            time.append(day).append(day == 1 ? "d" : "d").append(hours == 0 ? "" : ",");
        }

        if (hours != 0) {
            time.append(hours).append(hours == 1 ? "h" : "h").append(minutes == 0 ? "" : ",");
        }

        if (minutes != 0) {
            time.append(minutes).append(minutes == 1 ? "m" : "m").append(seconds == 0 ? "" : ",");
        }

        if (seconds != 0) {
            time.append(seconds).append(seconds == 1 ? "s" : "s");
        }

        return time.toString().trim();
    }

}
