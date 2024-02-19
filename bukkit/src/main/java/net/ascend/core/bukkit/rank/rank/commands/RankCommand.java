package net.ascend.core.bukkit.rank.rank.commands;

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

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.rank.rank.commands.subcommands.*;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.core.bukkit.util.command.sub.SubCommandHandler;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.command.CommandSender;

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
        this.addSubCommand(new RankPerksCommand());
        this.addSubCommand(new RankEditPerksCommand());

    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length >= 1) {
            SubCommand subCommand = getSubCommand(args[0]);
            if (subCommand != null) {
                subCommand.execute(sender, args);
            } else {
                for (String msg : XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getStringList("commands.usage")) {
                    sender.sendMessage(CC.translate(msg));
                }
            }
        } else {
            for (String msg : XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getStringList("commands.usage")) {
                sender.sendMessage(CC.translate(msg));
            }
        }
        return false;
    }
}
