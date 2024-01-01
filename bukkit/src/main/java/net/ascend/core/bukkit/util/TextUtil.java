package net.ascend.core.bukkit.util;

import com.google.common.collect.Lists;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang.WordUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class TextUtil {

    private List<String> list = Arrays.asList(
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "N", "M", "O",
            "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
    );

    public String genenrateRandomString(int size) {
        String generated = "";

        for (int i = 0; i < size; i++) {
            String picked = list.get(ThreadLocalRandom.current().nextInt(0, list.size()));

            generated += picked;
        }

        return generated;
    }

    public List<String> split(int length, List<String> lines, String linePrefix, String wordSuffix) {
        StringBuilder builder = new StringBuilder();

        for (String line : lines) {
            builder.append(line.trim());
            builder.append(" ");
        }

        return split(length, builder.substring(0, builder.length() - 1), linePrefix, wordSuffix);
    }

    public List<String> split(int length, String text, String linePrefix, String wordSuffix) {
        if (text.length() <= length) {
            return Collections.singletonList(linePrefix + text);
        }

        List<String> lines = Lists.newArrayList();
        String[] split = text.split(" ");
        StringBuilder builder = new StringBuilder(linePrefix);

        for (int i = 0; i < split.length; ++i) {
            if (builder.length() + split[i].length() >= length) {
                lines.add(builder.toString());
                builder = new StringBuilder(linePrefix);
            }

            builder.append(split[i]);
            builder.append(wordSuffix);

            if (i == split.length - 1) {
                builder.replace(builder.length() - wordSuffix.length(), builder.length(), "");
            }
        }

        if (builder.length() != 0) {
            lines.add(builder.toString());
        }

        return lines;
    }

    public String substring(String string, int limit) {
        if(string.length() > limit) {
            return string.substring(0, limit);
        }

        return string;
    }

    public String getPrettyName(String input) {
        input = input.replace("_", " ");
        input = input.replace("-", " ");

        return WordUtils.capitalizeFully(input);
    }

    public String formatNumber(int number) {
        NumberFormat format = NumberFormat.getInstance();

        return format.format(number);
    }

    /**
     * Returns a formatted String representation of a double, rounded to 2
     * decimal places
     *
     * @param value The double to be formatted
     * @return A formatted string of the double
     */
    public String formatDouble(double value) {
        return new DecimalFormat("0.0#").format(value);
    }

    /**
     * Implode a String array and combine into a string separated by a space
     *
     * @param toImplode A string array to implode
     * @return An imploded string joined by spaces
     */
    public String implode(String[] toImplode) {
        return implode(0, toImplode);
    }

    /**
     * Implodes a String array and combines into a single string seprated by a space
     *
     * @param startIndex The start index of the String array to implode
     * @param toImplode The String array to implode
     * @return An imploded string joined by spaces
     */
    public String implode(int startIndex, String[] toImplode) {
        return implode(startIndex, toImplode, " ");
    }

    /**
     * Implode a string array from a starting index and combine into a string separated by a space
     *
     * @param startIndex The start index of the String array to implode
     * @param toImplode A string array to implode
     * @param spacer The string to seperate words
     * @return An imploded string joined by the spacer
     */
    public String implode(int startIndex, String[] toImplode, String spacer) {
        StringBuilder ret = new StringBuilder();

        for (int i = startIndex ; i < toImplode.length ; i++) {
            if (toImplode[i] != null) {
                ret.append(toImplode[i]);
            }
            if (i < toImplode.length - 1) {
                ret.append(spacer);
            }
        }

        return ret.toString().trim();
    }

    public boolean isNull(String input) {

        if(input == null) {
            return true;
        }

        if(input.length() == 0) {
            return true;
        }

        return false;
    }
}

