package dev.hely.core.bukkit.punishment.menu.types;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.punishment.menu.types
//   Date: Saturday, July 15, 2023 - 2:18 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.punishment.Punishment;
import dev.hely.core.bukkit.punishment.PunishmentType;
import dev.hely.core.bukkit.utils.TimeUtil;
import dev.hely.lib.util.CC;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class KicksMenu extends PaginatedMenu {
    private Profile profile;

    @Override
    public String getName() {
        return OxygenBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("kicks.title").replace("%player_name%", profile.getName());
    }

    @Override
    public String getTitle(Player player) {
        return OxygenBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("kicks.title").replace("%player_name%", profile.getName());
    }

    @Override
    public int getSize() {
        return 9*5;
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        List<Punishment> kicks = profile.getPunishments().stream().sorted(Comparator.comparingLong(Punishment::getAddedAt).reversed()).filter(punishment -> punishment.getType() == PunishmentType.KICK).collect(Collectors.toList());
        Map<Integer, Button> buttons = new HashMap<>();

        kicks.forEach(module -> buttons.put(buttons.size(), new KicksButton(module)));

        return buttons;
    }

    @AllArgsConstructor
    private static class KicksButton extends Button {
        private final Punishment punishment;

        public Material getBlacklistedMaterial() {
            if (OxygenBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("kicks.button.kicked.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("kicks.button.kicked.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("kicks.button.kicked.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'kicks-menu-kicked-material' is null, using a default value."));
                    return Material.WOOL;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'kicks-menu-kicked-material' is broken, using a default value."));
                return Material.WOOL;
            }
        }

        public short getData() {
            return (short) OxygenBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getInt("kicks.button.kicked.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getBlacklistedMaterial());
            builder.setData(getData());
            builder.setDisplayName(OxygenBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("kicks.button.kicked.name"));
            for (String line : OxygenBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getStringList("kicks.button.kicked.lore")) {
                builder.addLore(line
                        .replace("%kicker%", punishment.getAddedByString())
                        .replace("%silent%", punishment.isSilent() ? "&a&nYes" : "&c&nNo")
                        .replace("%reason%", punishment.getAddedReason())
                        .replace("%added_date%", TimeUtil.formatIntoCalendarString(new Date(punishment.getAddedAt())))
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
        }
    }
}
