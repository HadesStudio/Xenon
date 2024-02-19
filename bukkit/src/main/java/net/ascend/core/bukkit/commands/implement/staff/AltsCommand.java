package net.ascend.core.bukkit.commands.implement.staff;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.commands.implement.staff
//   Date: Friday, August 25, 2023 - 5:59 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.punishment.Punishment;
import net.ascend.core.bukkit.util.TimeUtil;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.core.bukkit.lib.CC;
import net.md_5.bungee.api.chat.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class AltsCommand extends BaseCommand {

    public AltsCommand(){
        super("alts", Collections.singletonList("alternateaccounts"),"xenon.staff.alts", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("alts-command.usage")));
            return;
        }

        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(args[0]);
        if (profile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
            return;
        }

        if (profile.getAlts().isEmpty()){
            profile.setupAlts();
        }


        if (profile.getAlts().isEmpty()) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("alts-command.no-alts").replace("%player%", args[0])));
            return;
        }

        List<Profile> altProfile = new ArrayList<>();
        profile.getAlts().forEach(uuid -> altProfile.add(XenonBukkit.getInstance().getProfileManager().getProfile(uuid)));


        sendAltsMessage((Player)sender, profile, altProfile);
    }

    public void sendAltsMessage(Player sender, Profile profile, List<Profile> alts) {
        TextComponent mainComponent = new TextComponent(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("alts-command.first-chat-message").replace("%profile_name%", profile.getName()).replace("%alt_size%", String.valueOf(alts.size()))));

        AtomicBoolean first = new AtomicBoolean(true);
        alts.forEach(alt -> {
            if (first.get()) {
                first.set(false);
            } else {
                mainComponent.addExtra(CC.translate("&7, "));
            }
            Punishment blacklist = alt.getActiveBlacklist();
            Punishment ban = alt.getActiveBan();
            Punishment mute = alt.getActiveMute();

            ChatColor color = ChatColor.DARK_GREEN;

            List<String> hoverText = new ArrayList<>();

            for (String hoverLine : XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("alts-command.hover")) {
                hoverText.add(CC.translate(hoverLine)
                        .replace("%playtime%", TimeUtil.getMoreDetailedTime(alt.getTotalPlayTime()))
                        .replace("%last_seen%", TimeUtil.getMoreDetailedTime(alt.getLastSeen()))
                        .replace("%ban_status%", ban.getRemaining())
                        .replace("%blacklist_status%", blacklist.getRemaining())
                        .replace("%muted_status%", mute.getRemaining())
                );
            }

            if (blacklist != null && blacklist.isActive()) {
                color = ChatColor.DARK_RED;
            } else if (ban != null && ban.isActive()) {
                color = ChatColor.RED;
            } else if (alt.getSession().isRunning())
                color = ChatColor.GREEN;
            else {
                color = ChatColor.GRAY;
            }
            BaseComponent baseComponent = new TextComponent(color + alt.getName());
            baseComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/history " + alt.getName()));
            baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(CC.translate(String.join("\n", hoverText))).create()));
            mainComponent.addExtra(baseComponent);


        });
        sender.spigot().sendMessage(mainComponent);
    }
}

