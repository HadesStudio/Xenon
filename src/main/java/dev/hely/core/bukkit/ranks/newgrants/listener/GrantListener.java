package dev.hely.core.bukkit.ranks.newgrants.listener;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.ranks.newgrants.Grant;
import dev.hely.core.bukkit.ranks.newgrants.events.GrantExpiredEvent;
import dev.hely.core.bukkit.ranks.newgrants.events.GrantGivenEvent;
import dev.hely.core.bukkit.ranks.newgrants.types.PermissionGrant;
import dev.hely.core.bukkit.ranks.newgrants.types.RankGrant;
import dev.hely.core.bukkit.ranks.newgrants.types.TagGrant;
import dev.hely.lib.util.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class GrantListener implements Listener {
    @EventHandler
    public void onApplied(GrantGivenEvent event) {
        Player player = event.getPlayer();
        Grant grant = event.getGrant();

        String duration = (grant.isPermanent() ? "Forever" : grant.getDurationText(false));
        String type = "Unknown";

        if (grant instanceof RankGrant) {
            RankGrant rankGrant = (RankGrant) grant;

            type = OxygenBukkit.getInstance().getRankManager().getRank(rankGrant.getRankName()).getName();
        } else if (grant instanceof PermissionGrant) {
            PermissionGrant permissionGrant = (PermissionGrant) grant;

            type = permissionGrant.getPermission();
        } else if (grant instanceof TagGrant) {
            TagGrant tagGrant = (TagGrant) grant;

            type = OxygenBukkit.getInstance().getTagManager().getTag(tagGrant.getTagName()).getName();
        }

        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if (profile != null) {
            profile.setupPermissionAttachments(player);
        }

        for (String message : OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("messages.applied")) {
            player.sendMessage(CC.translate(message).replace("%type%", type).replace("%duration%", duration));
        }
    }

    @EventHandler
    public void onExpire(GrantExpiredEvent event) {
        Player player = event.getPlayer();
        Grant grant = event.getGrant();

        String type = "Unknown";

        if (grant instanceof RankGrant) {
            RankGrant rankGrant = (RankGrant) grant;

            type = OxygenBukkit.getInstance().getRankManager().getRank(rankGrant.getRankName()).getName();
        } else if (grant instanceof PermissionGrant) {
            PermissionGrant permissionGrant = (PermissionGrant) grant;

            type = permissionGrant.getPermission();
        } else if (grant instanceof TagGrant) {
            TagGrant tagGrant = (TagGrant) grant;

            type = OxygenBukkit.getInstance().getTagManager().getTag(tagGrant.getTagName()).getName();
        }

        for (String message : OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("messages.expired")) {
            player.sendMessage(CC.translate(message).replace("%type%", type));
        }

        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if (profile != null) {
            profile.activateNextRankGrant();
            profile.setupPermissionAttachments(player);
        }
    }
}

