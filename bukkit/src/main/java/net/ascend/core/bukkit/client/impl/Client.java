package net.ascend.core.bukkit.client.impl;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.client.impl
//   Date: Saturday, July 15, 2023 - 7:55 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;

@AllArgsConstructor
@Getter
public enum Client {
    VANILLA("Vanilla", null, null,ChatColor.WHITE + "Vanilla", XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("advanced-client-version-data.vanilla"), ""),
    FORGE("Forge", "FML", null,ChatColor.YELLOW + "Forge", XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("advanced-client-version-data.forge"), ""),
    LUNAR_CLIENT("Lunar-Client", "Lunar-Client",null, ChatColor.AQUA + "Lunar Client", XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("advanced-client-version-data.lunar-client"), "Lunar-Client"),
    CHEAT_BREAKER("Cheat-Breaker", "CB-Client", null,ChatColor.RED + "Cheat" + ChatColor.WHITE + "Breaker", XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("advanced-client-version-data.cheat-breaker"), "CB-Client"),
    COSMIC_CLIENT("Cosmic-Client", "CC", null,ChatColor.LIGHT_PURPLE + "Cosmic Client", XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("advanced-client-version-data.cosmic-client"), ""),



    // Payload is what detects the clients:
    HACKED_CLIENT_A("Hacked-Client (Type A)","LOLIMAHCKER",null,ChatColor.DARK_RED.toString() + "Hacked-Client (Type A)", XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("advanced-client-version-data.hacked-client"),""), // CRACKED VAPE
    HACKED_CLIENT_B("Hacked-Client (Type B)","cock",null,ChatColor.DARK_RED.toString() + "Hacked-Client (Type B)", XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("advanced-client-version-data.hacked-client"),""), // Cracked merge client wtf bitch
    HACKED_CLIENT_C("Hacked-Client (Type C)","customGuiOpenBspkrs",null,ChatColor.DARK_RED.toString() + "Hacked-Client (Type C)", XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("advanced-client-version-data.hacked-client"),""), // MODIFIED BspkrsCore WTF?
    HACKED_CLIENT_D("Hacked-Client (Type D)","0SO1Lk2KASxzsd",null,ChatColor.DARK_RED.toString() + "Hacked-Client (Type D)", XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("advanced-client-version-data.hacked-client"),""), // MODIFIED BspkrsCore WTF?
    HACKED_CLIENT_E("Hacked-Client (Type E)","mincraftpvphcker",null,ChatColor.DARK_RED.toString() + "Hacked-Client (Type E)", XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("advanced-client-version-data.hacked-client"),""), // MODIFIED BspkrsCore WTF?
    HACKED_CLIENT_F("Hacked-Client (Type F)","lmaohax",null,ChatColor.DARK_RED.toString() + "Hacked-Client (Type F)", XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("advanced-client-version-data.hacked-client"),""), // CRACKED INCOGNITO CLIENT WTF!!
    HACKED_CLIENT_G("Hacked-Client (Type G)","MCnetHandler",null,ChatColor.DARK_RED.toString() + "Hacked-Client (Type G)", XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("advanced-client-version-data.hacked-client"),""), // Modified Time Changer JAR
    HACKED_CLIENT_H("Hacked-Client (Type H)","L0LIMAHCKER",null,ChatColor.DARK_RED.toString() + "Hacked-Client (Type H)", XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("advanced-client-version-data.hacked-client"),""), // VAPE STRING :O
    HACKED_CLIENT_I("Hacked-Client (Type I)","mergeclient",null,ChatColor.DARK_RED.toString() + "Hacked-Client (Type H)", XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("advanced-client-version-data.hacked-client"),""), // VAPE STRING :O
    HACKED_CLIENT_J("Hacked-Client (Type J)","wigger",null,ChatColor.DARK_RED.toString() + "Hacked-Client (Type H)", XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("advanced-client-version-data.hacked-client"),""); // VAPE STRING :O


    String name;
    String payload;
    String brand;
    String display;
    String symbol;
    String channel;

    public boolean usesLunarPackets() {
        return this == CHEAT_BREAKER || this == LUNAR_CLIENT;
    }
}
