package net.ascend.core.bukkit.staff.request.commands;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.staff.request.packet.StaffRequestPacket;
import net.ascend.core.bukkit.util.ChatUtil;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class HelpopCommand extends BaseCommand {

    public HelpopCommand(){
        super("helpop", Arrays.asList("request", "helpstaff", "stafftp"), true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(CC.translate(Locale.HELPOP_USAGE));
            return;
        }

        XenonBukkit.getInstance().getRedisModule().sendPacket(new StaffRequestPacket(
                player.getName(),
                XenonBukkit.getInstance().getServerManager().getCurrentServerName(),
                ChatUtil.joinArray(args, " ", 1)));

        XenonBukkit.getInstance().getRequestHandler().createNewRequest(player.getName(), ChatUtil.joinArray(args, " ", 1));


        player.sendMessage(CC.translate(Locale.HELPOP_CREATED));
    }
}
