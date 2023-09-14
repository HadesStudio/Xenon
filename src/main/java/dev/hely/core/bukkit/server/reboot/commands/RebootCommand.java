package dev.hely.core.bukkit.server.reboot.commands;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.server.reboot.commands
//   Date: Tuesday, September 12, 2023 - 3:57 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.packet.implement.StaffRawMessagePacket;
import dev.hely.core.bukkit.utils.TimeUtil;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class RebootCommand  extends BaseCommand {
    public RebootCommand(){
        super("reboot", Arrays.asList("stop", "shutdown", "stopserver", "rebootserver"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) { // if they just type "reboot" then:
            if (sender instanceof Player) { // if a player typed "reboot":
                Player senderPlayer = (Player) sender;
                if (senderPlayer.hasPermission("oxygen.command.reboot")) { // then we want to start a reboot
                    sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("reboot.started").replace("%time%", TimeUtil.getMoreDetailedTime(60000))));
                    OxygenBukkit.getInstance().getRedisModule().sendPacket(new StaffRawMessagePacket("oxygen.admin", OxygenBukkit.getInstance().getServerConfig().getConfiguration().getString("server-name"), OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("reboot.staff-broadcast").replace("%player%", senderPlayer.getName()).replace("%time%", TimeUtil.getMoreDetailedTime(60))));
                    OxygenBukkit.getInstance().getRebootManager().reboot();
                } else {
                    if (OxygenBukkit.getInstance().getRebootManager().isRebooting()) {
                        for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("reboot.rebooting")) {
                            String formattedMessage = message.replace("%time%", TimeUtil.getMoreDetailedTime(OxygenBukkit.getInstance().getRebootManager().getTask().getTimeBeforeReboot()));
                            sender.sendMessage(CC.translate(formattedMessage));
                        }
                    } else {
                        sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("reboot.not-rebooting")));
                    }
                }
            } else { // if console typed "reboot":
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("reboot.started").replace("%time%", TimeUtil.getMoreDetailedTime(60000))));
                OxygenBukkit.getInstance().getRedisModule().sendPacket(new StaffRawMessagePacket("oxygen.admin", OxygenBukkit.getInstance().getServerConfig().getConfiguration().getString("server-name"), OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("reboot.staff-broadcast").replace("%player%", sender.getName()).replace("%time%", TimeUtil.getMoreDetailedTime(60000))));
                OxygenBukkit.getInstance().getRebootManager().reboot();
            }
        } else if (args[0].equalsIgnoreCase("cancel")) {
            if (sender.hasPermission("oxygen.command.reboot")) {
                if (OxygenBukkit.getInstance().getRebootManager().isRebooting()) {
                    OxygenBukkit.getInstance().getRebootManager().cancel();
                } else {
                    sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("reboot.not-rebooting")));
                }
            } else {
                if (OxygenBukkit.getInstance().getRebootManager().isRebooting()) {
                    for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("reboot.rebooting")) {
                        String formattedMessage = message.replace("%time%", TimeUtil.getMoreDetailedTime(OxygenBukkit.getInstance().getRebootManager().getTask().getTimeBeforeReboot()));
                        sender.sendMessage(CC.translate(formattedMessage));
                    }
                } else {
                    sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("reboot.not-rebooting")));
                }
            }
        } else {
            if (sender.hasPermission("oxygen.command.reboot")) {
                long duration = TimeUtil.parseTime(args[0]);
                if(duration == -1) {
                    sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("reboot.invalid-time")));
                    return;
                }

                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("reboot.started").replace("%time%", TimeUtil.getMoreDetailedTime(duration))));
                OxygenBukkit.getInstance().getRedisModule().sendPacket(new StaffRawMessagePacket("oxygen.admin", OxygenBukkit.getInstance().getServerConfig().getConfiguration().getString("server-name"), OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("reboot.staff-broadcast").replace("%player%", sender.getName()).replace("%time%", TimeUtil.getMoreDetailedTime(duration))));
                OxygenBukkit.getInstance().getRebootManager().reboot(duration);
            } else {
                if (OxygenBukkit.getInstance().getRebootManager().isRebooting()) {
                    for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("reboot.rebooting")) {
                        String formattedMessage = message.replace("%time%", TimeUtil.getMoreDetailedTime(OxygenBukkit.getInstance().getRebootManager().getTask().getTimeBeforeReboot()));
                        sender.sendMessage(CC.translate(formattedMessage));
                    }
                } else {
                    sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("reboot.not-rebooting")));
                }
            }
        }
    }
}
