package net.ascend.core.bukkit.rank.rank.command.impl;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class RankImportCommand extends SubCommand {
    public RankImportCommand() {
        super("import", Collections.singletonList("importyml"), "xenon.rank.import");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        XenonBukkit.getInstance().getRankManager().importRanks();
        sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.import.imported")));
    }
}
