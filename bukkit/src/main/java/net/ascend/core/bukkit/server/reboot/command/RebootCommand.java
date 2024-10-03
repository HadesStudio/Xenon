package net.ascend.core.bukkit.server.reboot.command;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.packet.implement.staff.StaffRawMessagePacket;
import net.ascend.core.bukkit.util.TimeUtil;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.util.time.DurationFormatter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class RebootCommand  extends BaseCommand {

    public RebootCommand(){
        super("reboot", Collections.singletonList("rebootserver"), "xenon.command.reboot");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            for (String message : Locale.REBOOT_USAGE) {
                sender.sendMessage(CC.translate(message));
            }
            return;
        }

        String subCommand = args[0].toLowerCase();
        switch (subCommand) {
            case "start":
                handleStart(sender, args);
                break;
            case "cancel":
                handleCancel(sender);
                break;
            case "info":
                handleInfo(sender);
                break;
            default:
                for (String message : Locale.REBOOT_USAGE) {
                    sender.sendMessage(CC.translate(message));
                }
        }
    }

    private void handleStart(CommandSender sender, String[] args) {
        if (XenonBukkit.getInstance().getRebootManager().isRebooting()) {
            sendRebootingMessage(sender);
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(CC.translate(Locale.REBOOT_START_USAGE));
            return;
        }

        long duration = TimeUtil.parse(args[1]);
        if (duration == -1) {
            sender.sendMessage(ChatColor.RED + "Invalid duration, use the correct format: 10m 1s");
            return;
        }

        sender.sendMessage(CC.translate(Locale.REBOOT_START_STARTED).replace("%time%", DurationFormatter.getRemaining(duration, true)));

        XenonBukkit.getInstance().getRedisModule().sendPacket(new StaffRawMessagePacket("xenon.command.reboot",
                XenonBukkit.getInstance().getServerManager().getCurrentServerName(),
                Locale.REBOOT_START_STAFF_ALERT
                        .replace("%sender%", sender.getName())
                        .replace("%time%", DurationFormatter.getRemaining(duration, true))));
        XenonBukkit.getInstance().getRebootManager().reboot(duration);
    }

    private void handleCancel(CommandSender sender) {
        if (XenonBukkit.getInstance().getRebootManager().isRebooting()) {
            XenonBukkit.getInstance().getRebootManager().cancel();

            sender.sendMessage(CC.translate(Locale.REBOOT_CANCEL_CANCELLED));
            XenonBukkit.getInstance().getRedisModule().sendPacket(new StaffRawMessagePacket("xenon.command.reboot",
                    XenonBukkit.getInstance().getServerManager().getCurrentServerName(),
                    Locale.REBOOT_CANCEL_STAFF_ALERT.replace("%sender%", sender.getName())));
        } else {
            sender.sendMessage(CC.translate(Locale.REBOOT_CANCEL_NOT_REBOOTING));
        }
    }

    private void handleInfo(CommandSender sender) {
        if (XenonBukkit.getInstance().getRebootManager().isRebooting()) {
            for (String message : Locale.REBOOT_INFO_MESSAGE) {
                String formattedMessage = message.replace("%time%", TimeUtil.getMoreDetailedTime(XenonBukkit.getInstance().getRebootManager().getTask().getTimeBeforeReboot() * 1000L));

                sender.sendMessage(CC.translate(formattedMessage));
            }
        } else {
            sender.sendMessage(CC.translate(Locale.REBOOT_CANCEL_NOT_REBOOTING));
        }
    }

    private void sendRebootingMessage(CommandSender sender) {
        for (String message : Locale.REBOOT_INFO_MESSAGE) {
            String formattedMessage = message.replace("%time%", TimeUtil.getMoreDetailedTime(XenonBukkit.getInstance().getRebootManager().getTask().getTimeBeforeReboot() * 1000L));

            sender.sendMessage(CC.translate(formattedMessage));
        }
    }
}
