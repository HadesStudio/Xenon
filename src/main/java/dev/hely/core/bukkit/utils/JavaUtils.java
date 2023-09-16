package dev.hely.core.bukkit.utils;

import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import dev.hely.lib.util.CC;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.ListIterator;

/**
 * @author Leandro Figueroa (LeandroSSJ)
 * viernes, abril 02, 2021
 */
public class JavaUtils {

    private static final CharMatcher CHAR_MATCHER_ASCII;
    public static final String[] ENCHANTMENT_NAMES = new String[]{"AQUA_AFFINITY", "BANE_OF_ARTHROPODS", "BLAST_PROTECTION",
            "DURABILITY", "EFFICIENCY", "FEATHER_FALLING", "FIRE_ASPECT", "FIRE_PROTECTION", "FLAME", "FORTUNE", "INFINITY", "KNOCKBACK",
            "LOOTING", "LUCK", "LURE", "POWER", "PROJECTILE_PROTECTION", "PROTECTION", "PUNCH", "RESPIRATION", "SHARPNESS",
            "SILK_TOUCH", "SMITE", "THORNS", "UNBREAKING"};

    static {
        CHAR_MATCHER_ASCII = CharMatcher.inRange('0', '9').or(CharMatcher.inRange('a', 'z')).or(CharMatcher.inRange('A', 'Z')).or(CharMatcher.WHITESPACE).precomputed();
    }

    public static Integer tryParseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public static Double tryParseDouble(String string) {
        try {
            return Double.parseDouble(string);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public static boolean isAlphanumeric(String string) {
        return JavaUtils.CHAR_MATCHER_ASCII.matchesAllOf(string);
    }

    public static String format(Number number) {
        return format(number, 10);
    }

    public static String format(Number number, int decimalPlaces) {
        return format(number, decimalPlaces, RoundingMode.HALF_DOWN);
    }

    public static String format(Number number, int decimalPlaces, RoundingMode roundingMode) {
        Preconditions.checkNotNull(number, "The number cannot be null");
        return new BigDecimal(number.toString()).setScale(decimalPlaces, roundingMode).stripTrailingZeros().toPlainString();
    }


    public static boolean isInt(String sInt) {
        try {
            Integer.parseInt(sInt);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }


    private static int getRandomIndex(List<?> list) {
        return (int) (Math.random() * list.size());
    }

    public static String getRandomString(List<String> list) {
        if (list.isEmpty()) {
            return "";
        }
        return list.get(getRandomIndex(list));
    }

    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    public static void replace(List<String> list, String oldText, String newText) {
        ListIterator<String> it = list.listIterator();
        while (it.hasNext()) {
            it.set(CC.translate(it.next().replace(oldText, newText)));
        }
    }
}
