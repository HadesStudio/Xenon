package dev.hely.core.punishment.menu.types;

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

import dev.hely.core.Oxygen;
import dev.hely.core.profile.Profile;
import dev.hely.core.punishment.Punishment;
import dev.hely.core.punishment.PunishmentType;
import dev.hely.core.utils.TimeFormatUtils;
import dev.hely.lib.CC;
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
public class WarningsMenu extends PaginatedMenu {
    private Profile profile;

    @Override
    public String getName() {
        return Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("warnings.title").replace("%player_name%", profile.getName());
    }

    @Override
    public String getTitle(Player player) {
        return Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("warnings.title").replace("%player_name%", profile.getName());
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
            if (Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("warnings.button.warned.material") != null) {
                if (Material.getMaterial(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("warnings.button.warned.material")) != null) {
                    return Material.valueOf(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("warnings.button.warned.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'warnings-menu-warned-material' is null, using a default value."));
                    return Material.WOOL;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'warnings-menu-warned-material' is broken, using a default value."));
                return Material.WOOL;
            }
        }

        public short getData() {
            return (short) Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getInt("warnings.button.warned.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getWarningMaterial());
            builder.setData(getData());
            builder.setDisplayName(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("warnings.button.warned.name"));
            for (String line : Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getStringList("warnings.button.warned.lore")) {
                builder.addLore(line
                        .replace("%warned_by%", punishment.getAddedBy())
                        .replace("%silent%", punishment.isSilent() ? "&a&nYes" : "&c&nNo")
                        .replace("%reason%", punishment.getAddedReason())
                        .replace("%added_date%", TimeFormatUtils.formatIntoCalendarString(new Date(punishment.getAddedAt())))
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
        }
    }
}
