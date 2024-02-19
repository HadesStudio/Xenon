package net.ascend.core.bukkit.punishment.menu.types;

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

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.punishment.Punishment;
import net.ascend.core.bukkit.punishment.PunishmentType;
import net.ascend.core.bukkit.util.TimeUtil;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
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
        return XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("kicks.title").replace("%player_name%", profile.getName());
    }

    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("kicks.title").replace("%player_name%", profile.getName());
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

        public String getBlacklistedMaterial() {
            return XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("kicks.button.kicked.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getInt("kicks.button.kicked.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getBlacklistedMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("kicks.button.kicked.name"));
            for (String line : XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getStringList("kicks.button.kicked.lore")) {
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
