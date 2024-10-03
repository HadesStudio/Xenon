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
public class KicksMenu extends PaginatedMenu {

    private Profile profile;

    @Override
    public String getName() {
        return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("kicks-menu.title")
                .replace("%profile_name%", profile.getName())
                .replace("%profile_color%", profile.getColor());
    }

    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("kicks-menu.title")
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

        List<Punishment> kicks = profile.getPunishments().stream()
                .sorted(Comparator.comparingLong(Punishment::getAddedAt).reversed())
                .filter(punishment -> punishment.getType() == PunishmentType.KICK)
                .collect(Collectors.toList());

        AtomicInteger index = new AtomicInteger(1);

        for (Punishment punishment : kicks) {
            buttons.put(buttons.size(), new KicksButton(punishment, index.getAndIncrement()));
        }

        return buttons;
    }

    @AllArgsConstructor
    private static class KicksButton extends Button {

        private final Punishment punishment;
        private final int id;

        public String getBlacklistedMaterial() {
            return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("kicks-menu-menu.button.kicked.material");
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("kicks-menu.button.kicked.data");
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getBlacklistedMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("kicks-menu.button.kicked.name")
                    .replace("%kicked_id%", String.valueOf(id)));

            for (String line : XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("kicks-menu.button.kicked.lore")) {
                builder.addLore(line
                        .replace("%kicker%", punishment.getAddedByString())
                        .replace("%silent%", punishment.isSilent() ? "&a&nYes" : "&c&nNo")
                        .replace("%reason%", punishment.getAddedReason())
                        .replace("%added_date%", TimeUtil.formatIntoCalendarString(new Date(punishment.getAddedAt())))
                        .replace("%kicked_id%", String.valueOf(id))
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
        }
    }
}
