package net.ascend.core.bukkit.rank.rank.command.impl;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.type.RankGrant;
import net.ascend.core.bukkit.rank.rank.Rank;
import net.ascend.core.bukkit.util.command.sub.SubCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.UUID;

public class RankPromoteCommand extends SubCommand {

    public RankPromoteCommand() {
        super("promote", Collections.singletonList("rankup"), "xenon.rank.promote");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.promote.usage")));
            return;
        }

        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(args[0]);
        if (profile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
            return;
        }

        Rank newGrantedRank = XenonBukkit.getInstance().getRankManager().getByWeight(profile.getActiveRank().getWeight() + 1);
        if(newGrantedRank == null) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.promote.invalid-rank")));
            return;
        }

        if(sender instanceof Player) {
            Profile senderProfile = XenonBukkit.getInstance().getProfileManager().getProfile(args[0]);

            if ((senderProfile.getActiveRank().getWeight() <= newGrantedRank.getWeight())) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.promote.cannot-give")));
                return;
            }
        }

        RankGrant oldGrant = profile.getActiveRankGrant();

        RankGrant grant = new RankGrant(UUID.randomUUID(), newGrantedRank.getName());
        grant.setAddedReason("Grant expiration.");
        grant.setAddedAt(System.currentTimeMillis());
        grant.setDuration(Integer.MAX_VALUE);
        grant.setAddedReason(args[1]);
        grant.setAddedAt(System.currentTimeMillis());

        profile.getRankGrants().remove(oldGrant);
        profile.getRankGrants().add(grant);
        profile.save();
        profile.updateGrants();

        sender.sendMessage(CC.translate(XenonBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.promote.promoted")
                .replace("%player%", profile.getName())
                .replace("%name%", newGrantedRank.getName())));
    }
}

