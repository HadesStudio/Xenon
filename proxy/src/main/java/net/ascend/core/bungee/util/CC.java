package net.ascend.core.bungee.util;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bungee.utils
//   Date: Sunday, August 13, 2023 - 12:28 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

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
