package net.ascend.core.bukkit.rank.grant.listener;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.Grant;
import net.ascend.core.bukkit.rank.grant.event.GrantExpiredEvent;
import net.ascend.core.bukkit.rank.grant.event.GrantGivenEvent;
import net.ascend.core.bukkit.rank.grant.type.RankGrant;
import net.ascend.core.bukkit.rank.grant.type.TagGrant;
import net.ascend.core.bukkit.lib.CC;
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

        String duration = (grant.isPermanent() ? "Permanent" : grant.getDurationText(false));
        String type = "Unknown";

        if (grant instanceof RankGrant) {
            RankGrant rankGrant = (RankGrant) grant;

            type = XenonBukkit.getInstance().getRankManager().getRank(rankGrant.getRankName()).getName();
        }  else if (grant instanceof TagGrant) {
            TagGrant tagGrant = (TagGrant) grant;

            type = XenonBukkit.getInstance().getTagManager().getTag(tagGrant.getTagName()).getName();
        }

        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if (profile != null) {
            profile.setupPermissionAttachments(player);
        }

        for (String message : XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("grant.applied")) {
            player.sendMessage(CC.translate(message.replace("%duration%", duration))
                    .replace("%type%", type));
        }
    }

    @EventHandler
    public void onExpire(GrantExpiredEvent event) {
        Player player = event.getPlayer();
        Grant grant = event.getGrant();

        String type = "Unknown";

        if (grant instanceof RankGrant) {
            RankGrant rankGrant = (RankGrant) grant;

            type = XenonBukkit.getInstance().getRankManager().getRank(rankGrant.getRankName()).getName();
        } else if (grant instanceof TagGrant) {
            TagGrant tagGrant = (TagGrant) grant;

            type = XenonBukkit.getInstance().getTagManager().getTag(tagGrant.getTagName()).getName();
        }

        player.sendMessage(CC.translate("&cYou're '&c&l%type%&c' grant has just expired!").replace("%type%", type));

        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if (profile != null) {
            profile.activateNextRankGrant();
            profile.setupPermissionAttachments(player);
        }
    }
}

