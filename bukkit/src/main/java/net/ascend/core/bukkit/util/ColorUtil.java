package net.ascend.core.bukkit.util;

import com.google.common.collect.ImmutableMap;
import org.bukkit.*;

import java.util.*;

public class ColorUtil {

    public static Map<ChatColor, String> colorValues = new HashMap<>();
    private static Map<ChatColor, ColorSet<Integer, Integer, Integer>> colorMap = new HashMap<>();

    public static final ImmutableMap<ChatColor,DyeColor> COLOR_MAP;
    public static final ImmutableMap<Character, ChatColor> CHAR_COLOR_MAP;

    private static final List<ChatColor> WOOL_COLORS = Arrays.asList(
            ChatColor.WHITE, ChatColor.GOLD,
            ChatColor.LIGHT_PURPLE, ChatColor.AQUA,
            ChatColor.YELLOW, ChatColor.GREEN,
            ChatColor.LIGHT_PURPLE, ChatColor.DARK_GRAY,
            ChatColor.GRAY, ChatColor.DARK_AQUA,
            ChatColor.DARK_PURPLE, ChatColor.BLUE,
            ChatColor.BLACK, ChatColor.DARK_GREEN,
            ChatColor.RED
    );

    static {
        colorValues.put(ChatColor.BLACK, "BLACK");
        colorValues.put(ChatColor.DARK_BLUE, "DARK_BLUE");
        colorValues.put(ChatColor.DARK_GREEN, "DARK_GREEN");
        colorValues.put(ChatColor.DARK_AQUA, "DARK_AQUA");
        colorValues.put(ChatColor.DARK_RED, "DARK_RED");
        colorValues.put(ChatColor.DARK_PURPLE, "DARK_PURPLE");
        colorValues.put(ChatColor.GOLD, "GOLD");
        colorValues.put(ChatColor.GRAY, "GRAY");
        colorValues.put(ChatColor.DARK_GRAY, "DARK_GRAY");
        colorValues.put(ChatColor.BLUE, "BLUE");
        colorValues.put(ChatColor.GREEN, "GREEN");
        colorValues.put(ChatColor.AQUA, "AQUA");
        colorValues.put(ChatColor.RED, "RED");
        colorValues.put(ChatColor.LIGHT_PURPLE, "LIGHT_PURPLE");
        colorValues.put(ChatColor.YELLOW, "YELLOW");
        colorValues.put(ChatColor.WHITE, "WHITE");
        colorValues.put(ChatColor.RESET, "RESET");
        colorValues.put(ChatColor.ITALIC, "ITALIC");
        colorValues.put(ChatColor.UNDERLINE, "UNDERLINE");
        colorValues.put(ChatColor.STRIKETHROUGH, "STRIKETHROUGH");
        colorValues.put(ChatColor.MAGIC, "MAGIC");
        colorValues.put(ChatColor.BOLD, "BOLD");

        colorMap.put(ChatColor.BLACK, new ColorSet<>(0, 0, 0));
        colorMap.put(ChatColor.DARK_BLUE, new ColorSet<>(0, 0, 170));
        colorMap.put(ChatColor.DARK_GREEN, new ColorSet<>(0, 170, 0));
        colorMap.put(ChatColor.DARK_AQUA, new ColorSet<>(0, 170, 170));
        colorMap.put(ChatColor.DARK_RED, new ColorSet<>(170, 0, 0));
        colorMap.put(ChatColor.DARK_PURPLE, new ColorSet<>(170, 0, 170));
        colorMap.put(ChatColor.GOLD, new ColorSet<>(255, 170, 0));
        colorMap.put(ChatColor.GRAY, new ColorSet<>(170, 170, 170));
        colorMap.put(ChatColor.DARK_GRAY, new ColorSet<>(85, 85, 85));
        colorMap.put(ChatColor.BLUE, new ColorSet<>(85, 85, 255));
        colorMap.put(ChatColor.GREEN, new ColorSet<>(85, 255, 85));
        colorMap.put(ChatColor.AQUA, new ColorSet<>(85, 255, 255));
        colorMap.put(ChatColor.RED, new ColorSet<>(255, 85, 85));
        colorMap.put(ChatColor.LIGHT_PURPLE, new ColorSet<>(255, 85, 255));
        colorMap.put(ChatColor.YELLOW, new ColorSet<>(255, 255, 85));
        colorMap.put(ChatColor.WHITE, new ColorSet<>(255, 255, 255));

        COLOR_MAP = /*TODO:Maps.immutableEnumMap*/(ImmutableMap.<ChatColor, DyeColor>builder().
                put(ChatColor.AQUA, DyeColor.LIGHT_BLUE).
                put(ChatColor.BLACK, DyeColor.BLACK).
                put(ChatColor.BLUE, DyeColor.LIGHT_BLUE).
                put(ChatColor.DARK_AQUA, DyeColor.CYAN).
                put(ChatColor.DARK_BLUE, DyeColor.BLUE).
                put(ChatColor.DARK_GRAY, DyeColor.GRAY).
                put(ChatColor.DARK_GREEN, DyeColor.GREEN).
                put(ChatColor.DARK_PURPLE, DyeColor.PURPLE).
                put(ChatColor.DARK_RED, DyeColor.RED).
                put(ChatColor.GOLD, DyeColor.ORANGE).
                put(ChatColor.GREEN, DyeColor.LIME).
                put(ChatColor.LIGHT_PURPLE, DyeColor.MAGENTA).
                put(ChatColor.RED, DyeColor.RED).
                put(ChatColor.WHITE, DyeColor.WHITE).
                put(ChatColor.YELLOW, DyeColor.YELLOW).build());
        CHAR_COLOR_MAP = (ImmutableMap.<Character, ChatColor>builder()
                .put('0',ChatColor.BLACK)
                .put('1',ChatColor.DARK_BLUE)
                .put('2',ChatColor.DARK_GREEN)
                .put('3',ChatColor.DARK_AQUA)
                .put('4',ChatColor.DARK_RED)
                .put('5',ChatColor.DARK_PURPLE)
                .put('6',ChatColor.GOLD)
                .put('7',ChatColor.GRAY)
                .put('8',ChatColor.DARK_GRAY)
                .put('9',ChatColor.BLUE)
                .put('a',ChatColor.GREEN)
                .put('b',ChatColor.AQUA)
                .put('c',ChatColor.RED)
                .put('d',ChatColor.LIGHT_PURPLE)
                .put('e',ChatColor.YELLOW)
                .put('f',ChatColor.WHITE)
                .put('k',ChatColor.MAGIC)
                .put('l',ChatColor.BOLD)
                .put('m',ChatColor.STRIKETHROUGH)
                .put('n',ChatColor.UNDERLINE)
                .put('o',ChatColor.ITALIC)
                .put('r',ChatColor.RESET)
                .build());
    }

    public static String convertChatColor(ChatColor color) {
        return colorValues.get(color);
    }

    public static String convertChatColor(ChatColor color, boolean fixed) {
        if (!fixed) return convertChatColor(color);

        String name = convertChatColor(color).toLowerCase();
        if (!name.contains("_")) {
            return StringUtils.convertFirstUpperCase(name);
        }
        StringBuilder builder = new StringBuilder();
        String[] attributes = name.split("_");
        for (String attribute : attributes) {
            builder.append(StringUtils.convertFirstUpperCase(attribute));
            builder.append(" ");
        }
        builder.setLength(builder.length() - 1);
        return builder.toString();
    }

    public static ChatColor getOr(String source, ChatColor or) {
        try {
            return ChatColor.valueOf(source);
        } catch (Exception e) {
            return or;
        }
    }

    public static ChatColor fromHex(int hex) {
        int r = (hex & 0xFF0000) >> 16;
        int g = (hex & 0xFF00) >> 8;
        int b = (hex & 0xFF);
        return fromRGB(r, g, b);
    }

    public static ChatColor fromRGB(int r, int g, int b) {
        TreeMap<Integer, ChatColor> closest = new TreeMap<>();
        colorMap.forEach((color, set) -> {
            int red = Math.abs(r - set.getRed());
            int green = Math.abs(g - set.getGreen());
            int blue = Math.abs(b - set.getBlue());
            closest.put(red + green + blue, color);
        });

        return closest.firstEntry().getValue();
    }

    private static class ColorSet<R, G, B> {
        R red = null;
        G green = null;
        B blue = null;

        ColorSet(R red, G green, B blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        public R getRed() {
            return red;
        }

        public G getGreen() {
            return green;
        }

        public B getBlue() {
            return blue;
        }

    }

    public static int getWoolColorFromChatColor(ChatColor color) {
        if(color == ChatColor.DARK_RED) {
            color = ChatColor.RED;
        }

        if(color == ChatColor.DARK_BLUE) {
            color = ChatColor.BLUE;
        }

        return WOOL_COLORS.indexOf(color);
    }

    public static Color getColorFromChatColor(ChatColor chatColor) {
        switch (chatColor) {
            case BLACK:
                return Color.BLACK;
            case DARK_BLUE:
                return Color.NAVY;
            case DARK_GREEN:
                return Color.GREEN;
            case DARK_AQUA:
                return Color.TEAL;
            case DARK_RED:
                return Color.MAROON;
            case DARK_PURPLE:
                return Color.PURPLE;
            case GOLD:
                return Color.ORANGE;
            case GRAY:
                return Color.SILVER;
            case DARK_GRAY:
                return Color.GRAY;
            case BLUE:
                return Color.BLUE;
            case GREEN:
                return Color.LIME;
            case AQUA:
                return Color.AQUA;
            case RED:
                return Color.RED;
            case LIGHT_PURPLE:
                return Color.FUCHSIA;
            case YELLOW:
                return Color.YELLOW;
            case WHITE:
                return Color.WHITE;
            default:
                return Color.WHITE;
        }
    }

    private static final String COLOR_CODE_REGEX = "&[0-9A-FK-ORa-fk-or]";

    public static String removeColorCodes(String input) {
        return input.replaceAll(COLOR_CODE_REGEX, "");
    }
}