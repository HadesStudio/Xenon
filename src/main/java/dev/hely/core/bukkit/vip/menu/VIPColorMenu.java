package dev.hely.core.bukkit.vip.menu;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.vip.menu
//   Date: Saturday, September 16, 2023 - 4:09 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.utils.NewColorsUtil;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.Menu;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.fill.FillerTemplate;
import dev.hely.lib.util.CC;
import lombok.AllArgsConstructor;
import net.md_5.bungee.protocol.packet.Chat;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class VIPColorMenu extends Menu {
    Profile profile;

    public static List<ChatColor> colors = Arrays.asList(ChatColor.LIGHT_PURPLE, ChatColor.AQUA, ChatColor.GREEN, ChatColor.YELLOW, ChatColor.RED, ChatColor.DARK_AQUA, ChatColor.DARK_BLUE, ChatColor.GOLD, ChatColor.BLUE, ChatColor.DARK_GREEN, ChatColor.DARK_PURPLE, ChatColor.DARK_RED);
    public static Map<ChatColor, ChatColor> colorMatch = new HashMap<>();

    @Override
    public FillerTemplate getFillerTemplate() {
        return FillerTemplate.FILL;
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }

    @Override
    public String getName() {
        return OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("color-menu.title");
    }

    @Override
    public String getTitle(Player player) {
        return OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("color-menu.title");
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (ChatColor chatColor : colors) {
            buttons.put(buttons.size(), new ColorButton(chatColor));
        }

        return buttons;
    }

    @AllArgsConstructor
    public class ColorButton extends Button {
        ChatColor color;


        public String getVipFormat() {
            return OxygenBukkit.getInstance().getConfiguration().getConfiguration().getString("chat-system.vip-format").replace("%symbol%", CC.translate(color + OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.settings.vip-icon")));
        }

        @Override
        public ItemStack getItemStack(Player player) {
            return ItemMaker.of(Material.INK_SACK)
                    .setData(NewColorsUtil.COLOR_MAP.get(color).getDyeData())
                    .setDisplayName(color + WordUtils.capitalizeFully(color.name().toLowerCase()))
                    .setLore(
                            " ",
                            "&6Preview Display:",
                            getVipFormat() + player.getName() + "&7: &fHello everyone!",
                            " ",
                            "&6Left-Click &7to select this prefix option."
                    ).build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            profile.setVipColor(color.name());
            profile.save();
            player.closeInventory();

            player.playSound(player.getLocation(), org.bukkit.Sound.LEVEL_UP, 1, 1);
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.messages.updated-color").replace("%example%", color + OxygenBukkit.getInstance().getVipMenuConfig().getConfiguration().getString("menu.settings.vip-icon"))));
        }
    }

    static {
        colorMatch.put(ChatColor.DARK_RED, ChatColor.RED);
        colorMatch.put(ChatColor.AQUA, ChatColor.WHITE);
        colorMatch.put(ChatColor.GREEN, ChatColor.AQUA);
        colorMatch.put(ChatColor.YELLOW, ChatColor.WHITE);
        colorMatch.put(ChatColor.LIGHT_PURPLE, ChatColor.WHITE);
        colorMatch.put(ChatColor.DARK_BLUE, ChatColor.BLUE);
        colorMatch.put(ChatColor.DARK_GREEN, ChatColor.GREEN);
        colorMatch.put(ChatColor.DARK_AQUA, ChatColor.AQUA);
        colorMatch.put(ChatColor.DARK_PURPLE, ChatColor.LIGHT_PURPLE);
        colorMatch.put(ChatColor.DARK_GRAY, ChatColor.GRAY);
        colorMatch.put(ChatColor.GOLD, ChatColor.YELLOW);
        colorMatch.put(ChatColor.RED, ChatColor.WHITE);
        colorMatch.put(ChatColor.BLUE, ChatColor.AQUA);
    }
}
