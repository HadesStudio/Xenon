package net.ascend.core.bukkit.staff.request.commands;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.staff.request.packet.StaffRequestReportPacket;
import net.ascend.core.bukkit.util.ChatUtil;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportCommand extends BaseCommand {

    public ReportCommand(){
        super("report", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(args.length < 2) {
            player.sendMessage(CC.translate(Locale.REPORT_USAGE));
            return;
        }

        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(args[0]);
        if (profile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
            return;
        }

        if (profile.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(CC.translate(Locale.REPORT_SELF));
            return;
        }

        if (profile.getCurrentServer().equalsIgnoreCase("None")) {
            player.sendMessage(CC.translate(Locale.REPORT_NOT_ONLINE));
            return;
        }

        String reason = ChatUtil.joinArray(args, " ", 2);

        XenonBukkit.getInstance().getRedisModule().sendPacket(new StaffRequestReportPacket(
                player.getName(),
                XenonBukkit.getInstance().getServerManager().getCurrentServerName(),
                reason,
                args[0]));

        XenonBukkit.getInstance().getRequestHandler().createNewReport(player.getName(), profile.getName(), reason);

        player.sendMessage(CC.translate(Locale.REPORT_CREATED));
    }
}
