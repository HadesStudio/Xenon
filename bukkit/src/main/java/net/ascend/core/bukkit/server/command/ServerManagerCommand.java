package net.ascend.core.bukkit.server.command;

import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.server.command.impl.InfoCommandCommand;
import net.ascend.core.bukkit.server.command.impl.ListCommandCommand;
import net.ascend.core.bukkit.server.command.impl.RunCommandCommand;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.core.bukkit.util.command.sub.SubCommandHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Arrays;
import java.util.Collections;

public class ServerManagerCommand extends SubCommandHandler {

    public ServerManagerCommand() {
        super("servermanager", Collections.singletonList("servermonitor"));

        this.addSubCommand(new InfoCommandCommand());
        this.addSubCommand(new ListCommandCommand());
        this.addSubCommand(new RunCommandCommand());
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            if (sender.hasPermission("xenon.command.servermanager")) {
                for (String msg : Locale.SERVER_MANAGER_USAGE) {
                    sender.sendMessage(CC.translate(msg));
                }
            } else {
                sender.sendMessage(CC.translate(Locale.COMMANDS_NO_PERMISSION));
                return true;
            }
            return true;
        }

        SubCommand sub = this.getSubCommand(args[0]);

        if (sub == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_ARGUMENT_NOT_FOUND
                    .replace("%argument%", args[0])));
            return true;
        }

        if (sub.isPlayerOnly() && sender instanceof ConsoleCommandSender) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_ONLY_PLAYER));
            return true;
        }

        if (sub.getPermission() != null && !sender.hasPermission(sub.getPermission())) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_NO_PERMISSION));
            return true;
        }

        sub.execute(sender, Arrays.copyOfRange(args, 1, args.length));
        return true;
    }
}