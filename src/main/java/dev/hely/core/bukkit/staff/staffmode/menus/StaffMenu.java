package dev.hely.core.bukkit.staff.staffmode.menus;

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
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaffMenu extends PaginatedMenu {
    @Override
    public String getName() {
        return OxygenBukkit.getInstance().getConfiguration().getConfiguration().getString("staff_mode.online_menu.title");
    }

    @Override
    public String getTitle(Player player) {
        return OxygenBukkit.getInstance().getConfiguration().getConfiguration().getString("staff_mode.online_menu.title");
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
            for (String lores : OxygenBukkit.getInstance().getConfiguration().getConfiguration().getStringList("staff_mode.online_menu.lore")) {
                lores = lores.replace("%staffmode%", (OxygenBukkit.getInstance().getStaffManager().isStaffModeActive(this.target) ? "&aYes" : "&cNo"));
                lores = lores.replace("%vanish%", (OxygenBukkit.getInstance().getStaffManager().isVanished(this.target) ? "&aYes" : "&cNo"));
                lores = lores.replace("%rank_name%", OxygenBukkit.getInstance().getProfileManager().getProfile(this.target.getUniqueId()).getActiveHighestRank().getName());
                lores = lores.replace("%rank_prefix%", OxygenBukkit.getInstance().getProfileManager().getProfile(this.target.getUniqueId()).getActiveHighestRank().getPrefix());
                lores = lores.replace("%rank_suffix%", OxygenBukkit.getInstance().getProfileManager().getProfile(this.target.getUniqueId()).getActiveHighestRank().getSuffix());
                lores = lores.replace("%gamemode%", WordUtils.capitalizeFully(this.target.getGameMode().name()));
                lores = lores.replace("%target_name%", this.target.getName());
                lore.add(lores);
            }

            ItemMaker itemMaker = ItemMaker.of(Material.SKULL_ITEM, 1, 3)
                    .setLore(lore)
                    .setDisplayName(OxygenBukkit.getInstance().getConfiguration().getConfiguration().getString("staff_mode.online_menu.name")
                            .replace("%target_name%", this.target.getName())
                            .replace("%rank_name%", OxygenBukkit.getInstance().getProfileManager().getProfile(this.target.getUniqueId()).getActiveHighestRank().getName())
                            .replace("%rank_prefix%", OxygenBukkit.getInstance().getProfileManager().getProfile(this.target.getUniqueId()).getActiveHighestRank().getPrefix())
                            .replace("%rank_suffix%", OxygenBukkit.getInstance().getProfileManager().getProfile(this.target.getUniqueId()).getActiveHighestRank().getSuffix()))
                    .setOwner(this.target.getName());


            return itemMaker.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            player.teleport(this.target.getLocation());
        }
    }
}

