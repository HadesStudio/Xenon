package dev.hely.core.utils;

import dev.hely.lib.CC;

import java.util.Arrays;
import java.util.List;

/**
 * Created By LeandroSSJ
 * Created on 06/07/2023
 * Protect Name OxygenGlobal
 */
public class ChatUtil {

    public static String buildDescription(List<String> from) {
        String back = from.toString();
        return back.substring(1, back.length() - 1).replace(", ", "\n");
    }

    public static String joinArray(String[] array, String delimiter, int start) {
        if(array.length < start) return "";

        return String.join(CC.translate(delimiter), Arrays.copyOfRange(array, start - 1, array.length));
    }
}
