package net.ascend.core.bungee.util;

import net.md_5.bungee.api.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public class CC {
    public static String translate(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> translate(List<String> input) {
        return input.stream().map(CC::translate).collect(Collectors.toList());
    }

    public static String buildDescription(List<String> from) {
        String back = from.toString();
        return back.substring(1, back.length() - 1).replace(", ", "\n");
    }
}
