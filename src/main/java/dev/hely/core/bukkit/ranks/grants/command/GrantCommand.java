package dev.hely.core.bukkit.ranks.grants.command;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.ranks.grants.command
//   Date: Friday, September 15, 2023 - 7:53 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.profile.menu.submenus.GrantsMenu;
import dev.hely.core.bukkit.ranks.events.GrantGivenEvent;
import dev.hely.core.bukkit.ranks.grants.Grant;
import dev.hely.core.bukkit.ranks.grants.menu.GrantSelectionMenu;
import dev.hely.core.bukkit.ranks.rank.Rank;
import dev.hely.core.bukkit.utils.TimeUtil;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import dev.hely.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GrantCommand extends BaseCommand {
    public GrantCommand(){
        super("grant", "oxygen.grants");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("grant.usage")));
            return;
        }
        if (args[0].equalsIgnoreCase("add")) {
            if (args.length != 5) {
                player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("grant.add.usage")));
                return;
            }
            Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(args[1]);
            if (profile == null) {
                sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
                return;
            }
            Rank rank = OxygenBukkit.getInstance().getRankManager().getRank(args[2]);
            if (rank == null) {
                player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("grant.add.invalid-rank")));
                return;
            }
            long duration = TimeUtil.parseTime(args[3]);
            boolean permanent = args[3].equalsIgnoreCase("perm") || args[3].equalsIgnoreCase("permanent"); // By default, we set it to false, probably, their is a way better way of doing this, but hey, I will do it like this. :)
            if (duration == -1) {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("grant.add.invalid-time")));
                return;
            }
            String reason = args[4];

            Grant grant = new Grant();
            grant.setActive(true);
            grant.setAdder(sender.getName());
            grant.setDuration(duration);
            grant.setPermanent(permanent);
            grant.setRankName(rank.getName());
            grant.setAddedTime(System.currentTimeMillis());
            grant.setReason(reason);
            profile.getGrants().add(grant);
            profile.save();

            new GrantGivenEvent(grant, profile, player);
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("grant.add.given").replace("%player%", profile.getName()).replace("%rank%", rank.getName()).replace("%time%", String.valueOf(duration))));
        } else {
            if (sender != null) {
                Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(args[0]);
                if (profile == null) {
                    sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
                    return;
                }

                new GrantSelectionMenu(profile).openMenu(player);
            } else {
                sender.sendMessage(CC.translate(Locale.COMMANDS_ONLY_PLAYER));
            }
        }
    }
}
