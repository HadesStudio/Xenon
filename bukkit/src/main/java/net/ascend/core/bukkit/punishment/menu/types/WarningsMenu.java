package net.ascend.core.bukkit.punishment.menu.types;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.punishment.menu.types
//   Date: Saturday, July 15, 2023 - 2:17 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.punishment.Punishment;
import net.ascend.core.bukkit.punishment.PunishmentType;
import net.ascend.core.bukkit.util.TimeUtil;
import net.ascend.core.bukkit.util.material.MaterialUtil;
import net.ascend.lib.CC;
import net.ascend.lib.maker.ItemMaker;
import net.ascend.lib.menu.button.Button;
import net.ascend.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class WarningsMenu extends PaginatedMenu {
    private Profile profile;

    @Override
    public String getName() {
        return XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("warnings.title").replace("%player_name%", profile.getName());
    }

    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("warnings.title").replace("%player_name%", profile.getName());
    }

    @Override
    public int getSize() {
        return 9*5;
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        List<Punishment> warnings = profile.getPunishments().stream().sorted(Comparator.comparingLong(Punishment::getAddedAt).reversed()).filter(punishment -> punishment.getType() == PunishmentType.WARN).collect(Collectors.toList());
        Map<Integer, Button> buttons = new HashMap<>();

        warnings.forEach(module -> buttons.put(buttons.size(), new WarningsButton(module)));

        return buttons;
    }

    @AllArgsConstructor
    private static class WarningsButton extends Button {
        private final Punishment punishment;

        public Material getWarningMaterial() {
            if (XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("warnings.button.warned.material") != null) {
                if (Material.getMaterial(XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("warnings.button.warned.material")) != null) {
                    return Material.valueOf(XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("warnings.button.warned.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'warnings-menu-warned-material' is null, using a default value."));
                    return MaterialUtil.WOOL.getMaterial();
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'warnings-menu-warned-material' is broken, using a default value."));
                return MaterialUtil.WOOL.getMaterial();
            }
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getInt("warnings.button.warned.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getWarningMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("warnings.button.warned.name"));
            for (String line : XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getStringList("warnings.button.warned.lore")) {
                builder.addLore(line
                        .replace("%warned_by%", punishment.getAddedByString())
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
