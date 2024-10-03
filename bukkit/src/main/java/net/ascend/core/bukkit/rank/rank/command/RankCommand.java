package net.ascend.core.bukkit.rank.rank.command;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.rank.rank.command.impl.*;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.core.bukkit.util.command.sub.SubCommandHandler;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Arrays;
import java.util.Collections;

public class RankCommand extends SubCommandHandler {

    public RankCommand() {
        super("rank", Collections.singletonList("ranks"));

        this.addSubCommand(new RankListCommand());
        this.addSubCommand(new RankCreateCommand());
        this.addSubCommand(new RankDeleteCommand());
        this.addSubCommand(new RankInheritancesCommand());
        this.addSubCommand(new RankRenameCommand());
        this.addSubCommand(new RankBungeePermissionsCommand());
        this.addSubCommand(new RankPermissionsCommand());
        this.addSubCommand(new RankSetPrefixCommand());
        this.addSubCommand(new RankSetSuffixCommand());
        this.addSubCommand(new RankInfoCommand());
        this.addSubCommand(new RankSetWeightCommand());
        this.addSubCommand(new RankSetTypeCommand());
        this.addSubCommand(new RankImportCommand());
        this.addSubCommand(new RankExportCommand());
        this.addSubCommand(new RankPromoteCommand());
        this.addSubCommand(new RankDemoteCommand());
        this.addSubCommand(new RankSetColorCommand());
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            if (sender.hasPermission("xenon.command.rank")) {
                for (String msg : XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getStringList("commands.usage")) {
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
