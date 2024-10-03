package net.ascend.core.bukkit.profile.myprofile.commands;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.myprofile.menus.type.FriendsMenu;
import net.ascend.core.bukkit.util.command.BaseCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class FriendsCommand extends BaseCommand {

    public FriendsCommand(){
        super("friends", Collections.singletonList("friend"), true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (args.length == 0) {
            new FriendsMenu(profile).openMenu(player);
            return;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("add")) {
            String targetName = args[1];
            Player targetPlayer = Bukkit.getPlayer(targetName);

            if (targetPlayer == null) {
                player.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", targetName));
                return;
            }

            Profile targetProfile = XenonBukkit.getInstance().getProfileManager().getProfile(targetPlayer.getUniqueId());
            if (targetProfile == null) {
                player.sendMessage(CC.translate(Locale.COMMANDS_PLAYER_NEVER_PLAYED).replace("%player%", targetName));
                return;
            }

            if (player.getUniqueId().toString().equalsIgnoreCase(targetProfile.getUniqueId().toString())) {
                player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("friends.friend-add.friend-self")));
                return;
            }

            if (player.getName().equalsIgnoreCase(targetProfile.getName())) {
                player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfig().getString("friends.friend-add.friend-self")));
                return;
            }

            XenonBukkit.getInstance().getProfileManager().sendFriendRequest(player, profile, targetProfile);
        } else {
            sender.sendMessage(CC.translate("&cCorrect Usage: /friends add <player>"));
        }
    }
}
