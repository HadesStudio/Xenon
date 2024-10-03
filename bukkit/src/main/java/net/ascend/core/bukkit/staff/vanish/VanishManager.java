package net.ascend.core.bukkit.staff.vanish;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class VanishManager {

    public int getVanishPriority(Player player) {
        for (int i = 50; i > 0; i--) {
            if (player.hasPermission("xenon.vanish.priority." + i)) return i;
        }
        return 1;
    }

    public void enabledVanish(Player player) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(online.getUniqueId());

            if (profile.getStaffProfile().isHideStaff()) {
                online.hidePlayer(player);
                continue;
            }

            if (this.getVanishPriority(online) >= this.getVanishPriority(player)) continue;

            online.hidePlayer(player);
        }

        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());
        profile.getStaffProfile().setVanish(true);
    }

    public void disabledVanish(Player player) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(online.getUniqueId());

            if (!online.canSee(player) && (profile == null || !profile.getStaffProfile().isHideStaff())) {
                online.showPlayer(player);
            }
        }

        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());
        profile.getStaffProfile().setVanish(false);
    }
}
