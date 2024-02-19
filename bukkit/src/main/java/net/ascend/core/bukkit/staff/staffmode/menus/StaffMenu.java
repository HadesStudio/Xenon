package net.ascend.core.bukkit.staff.staffmode.menus;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.staff.staffmode.menus
//   Date: Saturday, August 26, 2023 - 9:15 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import com.google.common.collect.Lists;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaffMenu extends PaginatedMenu {
    @Override
    public String getName() {
        return XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("staff_mode.online_menu.title");
    }

    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("staff_mode.online_menu.title");
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        int pos = 0;
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.hasPermission("hcf.command.staff")) {
                buttons.put(pos, new ButtonBuilder(online));
                ++pos;
            }
        }


        return buttons;
    }

    @AllArgsConstructor
    private static class ButtonBuilder extends Button {

        private Player target;

        @Override
        public ItemStack getItemStack(Player player) {
            List<String> lore = Lists.newArrayList();
            for (String lores : XenonBukkit.getInstance().getConfiguration().getConfiguration().getStringList("staff_mode.online_menu.lore")) {
                lores = lores.replace("%staffmode%", (XenonBukkit.getInstance().getStaffManager().isStaffModeActive(this.target) ? "&aYes" : "&cNo"));
                lores = lores.replace("%vanish%", (XenonBukkit.getInstance().getStaffManager().isVanished(this.target) ? "&aYes" : "&cNo"));
                lores = lores.replace("%rank_name%", XenonBukkit.getInstance().getProfileManager().getProfile(this.target.getUniqueId()).getActiveRank().getName());
                lores = lores.replace("%rank_prefix%", XenonBukkit.getInstance().getProfileManager().getProfile(this.target.getUniqueId()).getActiveRank().getPrefix());
                lores = lores.replace("%rank_suffix%", XenonBukkit.getInstance().getProfileManager().getProfile(this.target.getUniqueId()).getActiveRank().getSuffix());
                lores = lores.replace("%gamemode%", WordUtils.capitalizeFully(this.target.getGameMode().name()));
                lores = lores.replace("%target_name%", this.target.getName());
                lore.add(lores);
            }

            ItemMaker itemMaker = ItemMaker.of("SKULL_ITEM")
                    .setLore(lore)
                    .setDisplayName(XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("staff_mode.online_menu.name")
                            .replace("%target_name%", this.target.getName())
                            .replace("%rank_name%", XenonBukkit.getInstance().getProfileManager().getProfile(this.target.getUniqueId()).getActiveRank().getName())
                            .replace("%rank_prefix%", XenonBukkit.getInstance().getProfileManager().getProfile(this.target.getUniqueId()).getActiveRank().getPrefix())
                            .replace("%rank_suffix%", XenonBukkit.getInstance().getProfileManager().getProfile(this.target.getUniqueId()).getActiveRank().getSuffix()))
                    .setOwner(this.target.getName());


            return itemMaker.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            player.teleport(this.target.getLocation());
        }
    }
}

