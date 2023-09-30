package dev.hely.core.bukkit.ranks.rank.commands.subcommands;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.ranks.rank.commands.subcommands
//   Date: Thursday, September 07, 2023 - 3:45 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.ranks.newgrants.types.RankGrant;
import dev.hely.core.bukkit.ranks.rank.Rank;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.lib.util.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.UUID;

public class RankDemoteCommand extends SubCommand {
    public RankDemoteCommand() {
        super("demote", Collections.singletonList("lower"), "oxygen.rank.demote");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.demote.usage")));
            return;
        }
        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(args[1]);
        if (profile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[1]));
            return;
        }
        Rank newGrantedRank = OxygenBukkit.getInstance().getRankManager().getByWeight(profile.getActiveRank().getWeight() - 1);
        if(newGrantedRank == null) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.demote.invalid-rank")));
            return;
        }
        if(sender instanceof Player) {
            Profile senderProfile = OxygenBukkit.getInstance().getProfileManager().getProfile(args[1]);

            if ((senderProfile.getActiveRank().getWeight() <= newGrantedRank.getWeight())) {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.demote.cannot-give")));
                return;
            }
        }

        RankGrant oldGrant = profile.getActiveRankGrant();
        oldGrant.setRemoved(true);
        oldGrant.setRemovedAt(System.currentTimeMillis());
        oldGrant.setRemovedReason("Demoted.");

        RankGrant grant = new RankGrant(UUID.randomUUID(), newGrantedRank.getName());
        grant.setAddedReason("Demoted.");
        grant.setAddedAt(System.currentTimeMillis());
        grant.setDuration(Integer.MAX_VALUE);
        grant.setAddedReason(args[2]);
        grant.setAddedAt(System.currentTimeMillis());

        profile.getRankGrants().remove(oldGrant);
        profile.getRankGrants().add(grant);
        profile.save();
        profile.updateGrants();
        sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.demote.demoted").replace("%player%", profile.getName()).replace("%name%", newGrantedRank.getName())));
    }
}

