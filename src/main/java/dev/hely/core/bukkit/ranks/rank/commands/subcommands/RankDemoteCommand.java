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
import dev.hely.core.bukkit.ranks.grants.Grant;
import dev.hely.core.bukkit.ranks.rank.Rank;
import dev.hely.core.bukkit.utils.command.sub.SubCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

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
        Rank newGrantedRank = OxygenBukkit.getInstance().getRankManager().getByWeight(profile.getActiveHighestRank().getWeight() - 1);
        if(newGrantedRank == null) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.demote.invalid-rank")));
            return;
        }
        if(sender instanceof Player) {
            Profile senderProfile = OxygenBukkit.getInstance().getProfileManager().getProfile(args[1]);

            if ((senderProfile.getActiveHighestRank().getWeight() <= newGrantedRank.getWeight())) {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.demote.cannot-give")));
                return;
            }
        }
        Grant oldGrant = profile.getHighestGrant();
        Grant grant = new Grant();
        grant.setActive(true);
        grant.setAdder(sender.getName());
        grant.setPermanent(true);
        grant.setRankName(newGrantedRank.getName());
        grant.setAddedTime(System.currentTimeMillis());
        grant.setReason(args[2]);

        profile.getGrants().remove(oldGrant); // we dont want them to keep the old rank..
        profile.getGrants().add(grant);
        profile.save();
        sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getRankSettingsConfig().getConfiguration().getString("commands.demote.demoted").replace("%player%", profile.getName()).replace("%name%", newGrantedRank.getName())));
    }
}

