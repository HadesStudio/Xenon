package dev.hely.core.bukkit.ranks.newgrants.commands;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.ranks.newgrants.menu.grant.GrantMenu;
import dev.hely.core.bukkit.ranks.newgrants.types.PermissionGrant;
import dev.hely.core.bukkit.ranks.newgrants.types.RankGrant;
import dev.hely.core.bukkit.ranks.newgrants.types.TagGrant;
import dev.hely.core.bukkit.ranks.rank.Rank;
import dev.hely.core.bukkit.ranks.rank.RankManager;
import dev.hely.core.bukkit.tag.Tag;
import dev.hely.core.bukkit.tag.TagManager;
import dev.hely.core.bukkit.utils.TimeUtil;
import dev.hely.core.bukkit.utils.command.BaseCommand;
import dev.hely.lib.util.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GrantCommand extends BaseCommand {
    public GrantCommand() {
        super("grant", "oxygen.grants");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("grant.usage")));
                return;
            }
            if (args[0].equalsIgnoreCase("add")) {
                if (args.length != 6) {
                    player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("grant.add.usage")));
                    return;
                }
                Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(args[1]);
                if (profile == null) {
                    sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[1]));
                    return;
                }
                String reason = args[5];
                String name = args[3];
                long duration = TimeUtil.parseTime(args[4]);
                boolean permanent = args[4].equalsIgnoreCase("perm") || args[4].equalsIgnoreCase("permanent");
                if (duration == -1 && !permanent) {
                    sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("grant.add.invalid-time")));
                    return;
                }


                if (args[2].equalsIgnoreCase("rank")) {
                    RankManager rankManager = OxygenBukkit.getInstance().getRankManager();

                    Rank rank = rankManager.getRank(name);
                    if (rank == null) {
                        sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("grant.add.invalid-rank")));
                        return;
                    }

                    RankGrant rankGrant = new RankGrant(UUID.randomUUID(), rank.getName());
                    rankGrant.setAddedAt(System.currentTimeMillis());
                    rankGrant.setAddedReason(reason);
                    rankGrant.setDuration(duration);

                    profile.addGrant(rankGrant);

                    profile.activateNextRankGrant();
                    profile.save();
                    sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("grant.add.given").replace("%player%", profile.getName()).replace("%time%", String.valueOf(duration))));
                } else if (args[2].equalsIgnoreCase("bukkitpermission") || args[2].equalsIgnoreCase("bungeepermission")) {
                    PermissionGrant permissionGrant = new PermissionGrant(UUID.randomUUID(), name, (args[2].contains("bungeepermission")));
                    permissionGrant.setAddedAt(System.currentTimeMillis());
                    permissionGrant.setAddedReason(reason);
                    permissionGrant.setDuration(duration);

                    profile.addGrant(permissionGrant);
                    profile.save();

                    sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("grant.add.given").replace("%player%", profile.getName()).replace("%time%", String.valueOf(duration))));
                } else if (args[2].equalsIgnoreCase("tag")) {
                    TagManager tagManager = OxygenBukkit.getInstance().getTagManager();

                    Tag tag = tagManager.getTag(name);
                    if (tag == null) {
                        sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("grant.add.invalid-tag")));
                        return;
                    }

                    TagGrant tagGrant = new TagGrant(UUID.randomUUID(), tag.getName());

                    tagGrant.setAddedAt(System.currentTimeMillis());
                    tagGrant.setAddedReason(reason);
                    tagGrant.setDuration(duration);

                    profile.addGrant(tagGrant);
                    profile.save();

                    sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("grant.add.given").replace("%player%", profile.getName()).replace("%time%", String.valueOf(duration))));
                } else {
                    sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("grant.add.invalid-type")));

                }
            } else {
                if (sender != null) {
                    Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(args[0]);
                    if (profile == null) {
                        sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[0]));
                        return;
                    }

                    new GrantMenu(profile).openMenu(player);
                } else {
                    sender.sendMessage(CC.translate(Locale.COMMANDS_ONLY_PLAYER));
                }
            }
        } else {
            if (args.length == 0) {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("grant.usage")));
                return;
            }

            if (args[0].equalsIgnoreCase("add")) {
                if (args.length != 6) {
                    sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("grant.add.usage")));
                    return;
                }
                Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(args[1]);
                if (profile == null) {
                    sender.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", args[1]));
                    return;
                }
                String reason = args[5];
                String name = args[3];
                long duration = TimeUtil.parseTime(args[4]);
                boolean permanent = args[4].equalsIgnoreCase("perm") || args[4].equalsIgnoreCase("permanent");
                if (duration == -1) {
                    sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("grant.add.invalid-time")));
                    return;
                }


                if (args[2].equalsIgnoreCase("rank")) {
                    RankManager rankManager = OxygenBukkit.getInstance().getRankManager();

                    Rank rank = rankManager.getRank(name);
                    if (rank == null) {
                        sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("grant.add.invalid-rank")));
                        return;
                    }

                    RankGrant rankGrant = new RankGrant(UUID.randomUUID(), rank.getName());
                    rankGrant.setAddedAt(System.currentTimeMillis());
                    rankGrant.setAddedReason(reason);
                    rankGrant.setDuration(duration);

                    profile.addGrant(rankGrant);

                    profile.activateNextRankGrant();
                    profile.save();
                    sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("grant.add.given").replace("%player%", profile.getName()).replace("%time%", String.valueOf(duration))));
                } else if (args[2].equalsIgnoreCase("bukkitpermission") || args[2].equalsIgnoreCase("bungeepermission")) {
                    PermissionGrant permissionGrant = new PermissionGrant(UUID.randomUUID(), name, (args[2].contains("bungeepermission")));
                    permissionGrant.setAddedAt(System.currentTimeMillis());
                    permissionGrant.setAddedReason(reason);
                    permissionGrant.setDuration(duration);

                    profile.addGrant(permissionGrant);
                    profile.save();

                    sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("grant.add.given").replace("%player%", profile.getName()).replace("%time%", String.valueOf(duration))));
                } else if (args[2].equalsIgnoreCase("tag")) {
                    TagManager tagManager = OxygenBukkit.getInstance().getTagManager();

                    Tag tag = tagManager.getTag(name);
                    if (tag == null) {
                        sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("grant.add.invalid-tag")));
                        return;
                    }

                    TagGrant tagGrant = new TagGrant(UUID.randomUUID(), tag.getName());

                    tagGrant.setAddedAt(System.currentTimeMillis());
                    tagGrant.setAddedReason(reason);
                    tagGrant.setDuration(duration);

                    profile.addGrant(tagGrant);
                    profile.save();

                    sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("grant.add.given").replace("%player%", profile.getName()).replace("%time%", String.valueOf(duration))));
                } else {
                    sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("grant.add.invalid-type")));

                }
            } else {
                sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("grant.usage")));
            }
        }
    }
}
