package net.ascend.core.bukkit.server.commands;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.server.Server;
import net.ascend.core.bukkit.server.ServerState;
import net.ascend.core.bukkit.server.packet.ServerCommandExecutePacket;
import net.ascend.core.bukkit.util.ChatUtil;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class RunCommandCommand extends BaseCommand {

    public RunCommandCommand(){
        super("runcmd", Collections.singletonList("runcommand"), "xenon.command.runcmd");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("run-command.usage")));
            return;
        }
        String serverString = args[0];
        if (XenonBukkit.getInstance().getServerManager().getServer(serverString) == null) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("run-command.invalid-server")));
            return;
        }
        Server server = XenonBukkit.getInstance().getServerManager().getServer(serverString);
        if (server.getState() != ServerState.ONLINE) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("run-command.not-online")));
            return;
        }

        XenonBukkit.getInstance().getRedisModule().sendPacket(new ServerCommandExecutePacket(server.getName(), sender.getName(), ChatUtil.joinArray(args, " ", 1)));
        sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("run-command.executed")));
    }
}
