package net.ascend.core.bukkit.punishment.menu.staff;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.lib.menu.button.implement.BackButton;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.punishment.Punishment;
import net.ascend.core.bukkit.punishment.menu.StaffHistoryMenu;
import net.ascend.core.bukkit.util.TimeUtil;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.button.implement.PageButton;
import net.ascend.core.bukkit.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@AllArgsConstructor
public class StaffHistoryViewingMenu extends PaginatedMenu {

    final Profile profile;
    final List<Punishment> punishments;
    final Map<UUID, List<Punishment>> executedPunishments;

    @Override
    public String getName() {
        String title = XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("staffhistory-viewing-menu.title");
        return title.replace("%profile_name%", profile.getName())
                .replace("%profile_color%", profile.getColor());
    }

    @Override
    public String getTitle(Player player) {
        String title = XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("staffhistory-viewing-menu.title");
        return title.replace("%profile_name%", profile.getName())
                .replace("%profile_color%", profile.getColor());
    }

    @Override
    public int getSize() {
        return 5 * 9;
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (Punishment punishment : punishments) {
            buttons.put(buttons.size(), new PunishmentButton(profile, punishment));
        }

        return buttons;
    }

    @Override
    public Map<Integer, Button> getGlobalContent(Player player) {
        Map<Integer, Button> menuContent = new HashMap<>();

        menuContent.put(0, new BackButton(new StaffHistoryMenu(profile, executedPunishments)));
        menuContent.put(1, Button.getPlaceholder());
        menuContent.put(2, Button.getPlaceholder());
        menuContent.put(3, Button.getPlaceholder());
        menuContent.put(4, Button.getPlaceholder());
        menuContent.put(5, Button.getPlaceholder());
        menuContent.put(6, Button.getPlaceholder());
        menuContent.put(7, Button.getPlaceholder());
        menuContent.put(8, Button.getPlaceholder());

        menuContent.put(36, new PageButton(-1, this));
        menuContent.put(37, Button.getPlaceholder());
        menuContent.put(38, Button.getPlaceholder());
        menuContent.put(39, Button.getPlaceholder());
        menuContent.put(40, Button.getPlaceholder());
        menuContent.put(41, Button.getPlaceholder());
        menuContent.put(42, Button.getPlaceholder());
        menuContent.put(43, Button.getPlaceholder());
        menuContent.put(44, new PageButton(1, this));


        return menuContent;
    }

    @AllArgsConstructor
    public static class PunishmentButton extends Button {

        final Profile profile;
        final Punishment punishment;

        @Override
        public ItemStack getItemStack(Player player) {

            ConfigurationSection config = XenonBukkit.getInstance().getMenuConfig().getConfiguration().getConfigurationSection("staffhistory-viewing-menu.button.punishment");
            String material = config.getString("material");
            short data = (short) config.getInt("data");

            ItemMaker builder = ItemMaker.of(material);
            builder.setData(data);
            builder.setDisplayName(config.getString("display_name").replace("%type%", CC.capitalize(punishment.getType().name())));

            List<String> lore = new ArrayList<>();
            List<String> loreConfig = punishment.isRemoved() ? config.getStringList("lore.pardoned") : config.getStringList("lore.not_pardoned");
            for (String line : loreConfig) {
                lore.add(line
                        .replace("%target%", XenonBukkit.getInstance().getProfileManager().getProfile(punishment.getTargetUUID()).getName())
                        .replace("%reason%", punishment.getAddedReason())
                        .replace("%silent%", punishment.isSilent() ? "&a&nYes" : "&c&nNo")
                        .replace("%added_date%", TimeUtil.formatIntoCalendarString(new Date(punishment.getAddedAt())))
                        .replace("%duration%", punishment.isPermanent() ? "Permanent" : TimeUtil.getMoreDetailedTime(punishment.getDuration()))
                        .replace("%pardoner%", punishment.getRemovedBy() != null ? punishment.getRemovedBy() : "N/A")
                        .replace("%pardoned_at%", punishment.getRemovedAt() > 0 ? TimeUtil.formatIntoCalendarString(new Date(punishment.getRemovedAt())) : "N/A")
                        .replace("%pardoned_reason%", punishment.getRemovedReason() != null ? punishment.getRemovedReason() : "N/A")
                );
            }
            builder.setLore(lore);

            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
        }
    }
}


