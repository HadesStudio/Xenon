package dev.hely.core.punishment.menu;

import dev.hely.core.profile.Profile;
import dev.hely.core.punishment.Punishment;
import dev.hely.core.punishment.PunishmentType;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.Menu;
import dev.hely.lib.menu.button.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created By LeandroSSJ
 * Created on 14/07/2023
 * Protect Name Oxygen
 */
@AllArgsConstructor
public class HistoryMenu extends Menu {

    private Profile profile;

    @Override
    public String getName() {
        return "HistoryMenu";
    }

    @Override
    public String getTitle(Player player) {
        return "Punishments of " + profile.getName();
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        return buttons;
    }

    private class BlacklistButton extends Button {

        @Override
        public ItemStack getItemStack(Player player) {
            List<Punishment> blacklists = profile.getPunishments().stream().filter(punishment -> punishment.getType() == PunishmentType.BLACKLIST).collect(Collectors.toList());
            return ItemMaker.of(Material.WOOL)
                    .setData((short) 15)
                    .setDisplayName("&4Blacklists &7(Total: " + blacklists.size() + ")")
                    .setLore("",
                            "&fCurrently Blacklisted&7: " + profile.getActiveBlacklist() != null ? "&aYes" : "&cNo",
                            "")
                    .build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {

        }
    }


    private class BanButton extends Button {

        @Override
        public ItemStack getItemStack(Player player) {
            List<Punishment> bans = profile.getPunishments().stream().filter(punishment -> punishment.getType() == PunishmentType.BAN).collect(Collectors.toList());
            return ItemMaker.of(Material.WOOL)
                    .setData((short) 15)
                    .setDisplayName("&4Bans &7(Total: " + bans.size() + ")")
                    .setLore("",
                            "&fCurrently Banned&7: " + profile.getActiveBan() != null ? "&aYes" : "&cNo",
                            "")
                    .build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {

        }
    }

    private class KickButton extends Button {

        @Override
        public ItemStack getItemStack(Player player) {
            List<Punishment> kicks = profile.getPunishments().stream().filter(punishment -> punishment.getType() == PunishmentType.KICK).collect(Collectors.toList());
            return ItemMaker.of(Material.WOOL)
                    .setData((short) 3)
                    .setDisplayName("&bKick &7(Total: " + kicks.size() + ")")
                    .setLore("",
                            "",
                            "")
                    .build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {

        }
    }

    private class MuteButton extends Button {

        @Override
        public ItemStack getItemStack(Player player) {
            List<Punishment> mutes = profile.getPunishments().stream().filter(punishment -> punishment.getType() == PunishmentType.MUTE).collect(Collectors.toList());
            return ItemMaker.of(Material.WOOL)
                    .setData((short) 4)
                    .setDisplayName("&eMute &7(Total: " + mutes.size() + ")")
                    .setLore("",
                            "&fCurrently Muted&7: " + profile.getActiveMute() != null ? "&aYes" : "&cNo",
                            "")
                    .build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {

        }
    }

    private class WarnButton extends Button {

        @Override
        public ItemStack getItemStack(Player player) {
            List<Punishment> warns = profile.getPunishments().stream().filter(punishment -> punishment.getType() == PunishmentType.WARN).collect(Collectors.toList());
            return ItemMaker.of(Material.WOOL)
                    .setData((short) 5)
                    .setDisplayName("&aWarn &7(Total: " + warns.size() + ")")
                    .setLore("",
                            "",
                            "")
                    .build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {

        }
    }
}
