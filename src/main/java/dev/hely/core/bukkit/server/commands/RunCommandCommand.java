package dev.hely.core.bukkit.server.commands;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.server.Server;
import dev.hely.core.bukkit.server.ServerState;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import dev.hely.lib.util.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class RunCommandCommand extends BaseCommand {
    public RunCommandCommand(){
        super("runcmd", Collections.singletonList("runcommand"), "oxygen.command.runcmd");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("run-command.usage")));
            return;
        }
        String serverString = args[0];
        if (OxygenBukkit.getInstance().getServerManager().getServer(serverString) == null) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("run-command.invalid-server")));
            return;
        }
        Server server = OxygenBukkit.getInstance().getServerManager().getServer(serverString);
        if (server.getState() != ServerState.ONLINE) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("run-command.not-online")));
            return;
        }

    }
}
