package dev.hely.core.bukkit.ranks.rank.commands.subcommands;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.ranks.rank.commands.subcommands
//   Date: Wednesday, August 02, 2023 - 5:39 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.lib.util.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class RankExportCommand extends SubCommand {
    public RankExportCommand() {
        super("export", Collections.singletonList("exportyml"), "oxygen.rank.export");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        OxygenBukkit.getInstance().getRankManager().saveRanksToConfig();
        sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.export.exported")));
    }
}
