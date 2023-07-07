package dev.hely.core.utils;

import dev.hely.lib.CC;

import java.util.Arrays;

/**
 * Created By LeandroSSJ
 * Created on 06/07/2023
 * Protect Name Oxygen
 */
public class ChatUtil {

    public static String joinArray(String[] array, String delimiter, int start) {
        if(array.length < start) return "";

        return String.join(CC.translate(delimiter), Arrays.copyOfRange(array, start - 1, array.length));
    }
}
