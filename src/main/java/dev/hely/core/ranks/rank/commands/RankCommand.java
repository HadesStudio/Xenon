package dev.hely.core.ranks.rank.commands;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.ranks.rank.commands
//   Date: Wednesday, July 05, 2023 - 2:58 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.Oxygen;
import dev.hely.core.ranks.rank.commands.subcommands.RankCreateCommand;
import dev.hely.core.ranks.rank.commands.subcommands.RankListCommand;
import dev.hely.core.utils.command.sub.SubCommand;
import dev.hely.core.utils.command.sub.SubCommandHandler;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class RankCommand extends SubCommandHandler {
    public RankCommand() {
        super("rank", Collections.singletonList("ranks"));

        this.addSubCommand(new RankListCommand());
        this.addSubCommand(new RankCreateCommand());
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length >= 1) {
            SubCommand subCommand = getSubCommand(args[0]);
            if (subCommand != null) {
                subCommand.execute(sender, args);
            } else {
                for (String msg : Oxygen.getInstance().getRankSettingsConfig().getConfiguration().getStringList("commands.help.message")) {
                    sender.sendMessage(CC.translate(msg));
                }
            }
        } else {
            for (String msg : Oxygen.getInstance().getRankSettingsConfig().getConfiguration().getStringList("commands.help.message")) {
                sender.sendMessage(CC.translate(msg));
            }
        }
        return false;
    }
}
