package net.ascend.core.bukkit.rank.grant.command;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.log.category.LogCategories;
import net.ascend.core.bukkit.profile.log.LogType;
import net.ascend.core.bukkit.rank.grant.menu.grant.GrantMenu;
import net.ascend.core.bukkit.rank.grant.type.RankGrant;
import net.ascend.core.bukkit.rank.grant.type.TagGrant;
import net.ascend.core.bukkit.rank.rank.Rank;
import net.ascend.core.bukkit.rank.rank.RankManager;
import net.ascend.core.bukkit.tag.Tag;
import net.ascend.core.bukkit.tag.TagManager;
import net.ascend.core.bukkit.util.TimeUtil;
import net.ascend.core.bukkit.util.command.BaseCommand;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

public class GrantCommand extends BaseCommand {

    public GrantCommand() {
        super("grant", "xenon.grants");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                String playerName = args[0];
                Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(playerName);
                if (profile == null) {
                    player.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", playerName));
                    return;
                }
                new GrantMenu(profile).openMenu(player);
                return;
            } else if (args.length != 5) {
                player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("grant.usage")));
                return;
            }
        } else if (sender instanceof ConsoleCommandSender && args.length != 5) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("grant.usage")));
            return;
        }

        String playerName = args[0];
        String type = args[1];
        String name = args[2];
        String durationStr = args[3];
        String reason = args[4];

        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(playerName);
        if (profile == null) {
            sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", playerName));
            return;
        }

        long duration = TimeUtil.parse(durationStr);
        boolean permanent = durationStr.equalsIgnoreCase("perm") || durationStr.equalsIgnoreCase("permanent");
        if (duration == -1 && !permanent) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("grant.invalid-time")));
            return;
        }

        if (type.equalsIgnoreCase("rank")) {
            RankManager rankManager = XenonBukkit.getInstance().getRankManager();
            Rank rank = rankManager.getRank(name);
            if (rank == null) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("grant.invalid-rank")));
                return;
            }

            RankGrant rankGrant = new RankGrant(UUID.randomUUID(), rank.getName());
            rankGrant.setAddedAt(System.currentTimeMillis());
            rankGrant.setAddedReason(reason);
            rankGrant.setDuration(permanent ? Integer.MAX_VALUE : duration);

            profile.addGrant(rankGrant);
            profile.activateNextRankGrant();
            profile.save();

            if (sender instanceof Player) {
                Player player = (Player) sender;
                XenonBukkit.getInstance().getLogManager().logPlayerAction(player.getUniqueId(), player.getName(), Arrays.asList(
                        "&6Granted Player&7: &c" + profile.getName(),
                        "&6Reason&7: &c" + reason,
                        "&6Duration&7: &c" + rankGrant.getDurationText(false)
                ), LogType.GAVE_RANK, LogCategories.RANK_LOGS);


                XenonBukkit.getInstance().getLogManager().logPlayerAction(profile.getUniqueId(), profile.getName(), Arrays.asList(
                        "&6Staff Member&7: &c" + player.getName(),
                        "&6Rank&7: &c" + rank.getName(),
                        "&6Reason&7: &c" + reason,
                        "&6Duration&7: &c" + rankGrant.getDurationText(false)
                ), LogType.GRANT_GIVEN, LogCategories.RANK_LOGS);

            } else {
                XenonBukkit.getInstance().getLogManager().logPlayerAction(profile.getUniqueId(), profile.getName(), Arrays.asList(
                        "&6Staff Member&7: &4Console",
                        "&6Rank&7: &c" + rank.getName(),
                        "&6Reason&7: &c" + reason,
                        "&6Duration&7: &c" + rankGrant.getDurationText(false)
                ), LogType.GRANT_GIVEN, LogCategories.RANK_LOGS);
            }


            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("grant.given")
                    .replace("%player%", profile.getName())
                    .replace("%time%", durationStr)
                    .replace("%type%", "Rank")));
        } else if (type.equalsIgnoreCase("tag")) {

            TagManager tagManager = XenonBukkit.getInstance().getTagManager();
            Tag tag = tagManager.getTag(name);
            if (tag == null) {
                sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("grant.invalid-tag")));
                return;
            }

            TagGrant tagGrant = new TagGrant(UUID.randomUUID(), tag.getName());
            tagGrant.setAddedAt(System.currentTimeMillis());
            tagGrant.setAddedReason(reason);
            tagGrant.setDuration(permanent ? Integer.MAX_VALUE : duration);

            profile.addGrant(tagGrant);
            profile.save();

            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("grant.given")
                    .replace("%player%", profile.getName())
                    .replace("%time%", durationStr)
                    .replace("%type%", "Tag")));
        } else {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("grant.invalid-type")));
        }
    }
}