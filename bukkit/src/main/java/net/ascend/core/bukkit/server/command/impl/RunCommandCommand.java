package net.ascend.core.bukkit.server.command.impl;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.server.Server;
import net.ascend.core.bukkit.server.packet.impl.ServerCommandExecutePacket;
import net.ascend.core.bukkit.util.ChatUtil;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class RunCommandCommand extends SubCommand {

    public RunCommandCommand(){
        super("runcmd", Collections.singletonList("runcommand"), "xenon.command.servermanager.argument.runcmd");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate(Locale.SERVER_MANAGER_RUNCMD_USAGE));
            return;
        }

        Server server = XenonBukkit.getInstance().getServerManager().getServer(args[0]);
        if (server == null) {
            sender.sendMessage(CC.translate(Locale.SERVER_MANAGER_DONT_EXISTS));
            return;
        }

        XenonBukkit.getInstance().getRedisModule().sendPacket(new ServerCommandExecutePacket(server.getName(), sender.getName(), ChatUtil.joinArray(args, " ", 1)));
        sender.sendMessage(CC.translate(Locale.SERVER_MANAGER_RUNCMD_EXECUTED));
    }
}
