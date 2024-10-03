package net.ascend.core.bukkit.punishment.menu.types;

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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@AllArgsConstructor
public class WarningsMenu extends PaginatedMenu {

    private Profile profile;

    @Override
    public String getName() {
        return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("warnings-menu.title")
                .replace("%profile_name%", profile.getName())
                .replace("%profile_color%", profile.getColor());
    }

    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("warnings-menu.title")
                .replace("%profile_name%", profile.getName())
                .replace("%profile_color%", profile.getColor());
    }

    @Override
    public int getSize() {
        return 9*5;
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        List<Punishment> warnings = profile.getPunishments().stream()
                .sorted(Comparator.comparingLong(Punishment::getAddedAt).reversed())
                .filter(punishment -> punishment.getType() == PunishmentType.WARN)
                .collect(Collectors.toList());

        AtomicInteger index = new AtomicInteger(1);
        for (Punishment punishment : warnings) {
            buttons.put(buttons.size(), new WarningsButton(punishment, index.getAndIncrement()));
        }

        return buttons;
    }

    @AllArgsConstructor
    private static class WarningsButton extends Button {

        private final Punishment punishment;
        private final int id;

        public String getWarningMaterial() {
            return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("warnings-menu.button.warned.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("warnings-menu.button.warned.data");
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getWarningMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("warnings-menu.button.warned.name")
                    .replace("%warnings_id%", String.valueOf(id)));

            for (String line : XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("warnings-menu.button.warned.lore")) {
                builder.addLore(line
                        .replace("%warned_by%", punishment.getAddedByString())
                        .replace("%silent%", punishment.isSilent() ? "&a&nYes" : "&c&nNo")
                        .replace("%reason%", punishment.getAddedReason())
                        .replace("%added_date%", TimeUtil.formatIntoCalendarString(new Date(punishment.getAddedAt())))
                        .replace("%warnings_id%", String.valueOf(id))
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
        }
    }
}
