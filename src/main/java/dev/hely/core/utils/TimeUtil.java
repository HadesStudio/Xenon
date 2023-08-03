package dev.hely.core.utils;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created By LeandroSSJ
 * Created on 14/07/2023
 * Protect Name OxygenGlobal
 */
public class TimeUtil {

    public static int parseTime(String time) {

        if (time.equals("0") || time.equals("")) {
            return (0);
        }

        String[] lifeMatch = new String[]{"w", "d", "h", "m", "s"};
        int[] lifeInterval = new int[]{604800, 86400, 3600, 60, 1};
        int seconds = 0;

        for (int i = 0; i < lifeMatch.length; i++) {

            final Matcher matcher = Pattern.compile("([0-9]*)" + lifeMatch[i]).matcher(time);

            while (matcher.find()) {
                seconds += Integer.parseInt(matcher.group(1)) * lifeInterval[i];
            }

        }

        return (seconds);
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

    public static long convert(int value, char unit) {
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


    public static String formatMillis(long millis) {
        String value = "";

        int days = (int) (millis / 86400000L);
        if(days > 0) {
            millis -= (long) days * 86400000L;
            value += days + "d ";
        }

        int hours = (int) (millis / 3600000L);
        if(hours > 0) {
            millis -= (long) hours * 3600000L;
            value += hours + "h ";
        }

        int minutes = (int) (millis / 60000L);
        if(minutes > 0) {
            millis -= (long) minutes * 60000L;
            value += minutes + "m ";
        }

        int seconds = (int) (millis / 1000L);
        if(seconds > 0) {
            value += seconds + "s";
        }

        return value.trim();
    }
}
