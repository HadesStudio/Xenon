package net.ascend.core.bukkit.server.menu.submenus;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.server.menu.submenus
//   Date: Wednesday, August 16, 2023 - 11:52 AM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.server.Server;
import net.ascend.core.bukkit.server.ServerState;
import net.ascend.core.bukkit.server.packet.ServerStateChangePacket;
import net.ascend.lib.menu.fill.FillerTemplate;
import net.ascend.lib.CC;
import net.ascend.lib.maker.ItemMaker;
import net.ascend.lib.menu.Menu;
import net.ascend.lib.menu.button.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class InformationMenu extends Menu {
    {
        setClickUpdate(true);
    }

    @Override
    public FillerTemplate getFillerTemplate() {
        return FillerTemplate.BORDER;
    }

    Server server;

    @Override
    public String getName() {
        return server.getName() + " - Information";
    }

    @Override
    public String getTitle(Player player) {
        return server.getName() + " - Information";
    }

    @Override
    public int getSize() {
        return 27;
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(4, new Restart_Button(server));

        return buttons;
    }

    @AllArgsConstructor
    public static class Restart_Button extends Button {
        Server server;

        public Material getMaterial() {
            if(server.getState() == ServerState.OFFLINE) return Material.REDSTONE_BLOCK;
            if(server.getState() == ServerState.WHITELISTED) return Material.GOLD_BLOCK;
            if(server.getState() == ServerState.ONLINE) return Material.EMERALD_BLOCK;
            return null;
        }
        public String getDisplayName() {
            return server.getState().getDisplayName();
        }

        public List<String> getLore() {
            List<String> lore = new ArrayList<>();

            if (server.getState() == ServerState.OFFLINE) {
                lore.clear();
                lore.add("&7This server is currently &coffline&7.");
                return lore;
            } else if (server.getState() == ServerState.WHITELISTED) {
                lore.clear();
                lore.add("&7This server is currently &dwhitelisted&7.");
                lore.add("&7");
                lore.add("&6Left-Click &7to toggle the whitelist.");
                return lore;
            } else {
                lore.clear();
                lore.add("&7This server is currently &aonline&7.");
                lore.add("&7");
                lore.add("&6Left-Click &7to shutdown this server.");
                lore.add("&6Right-Click &7to toggle the whitelist.");
                return lore;
            }
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setDisplayName(getDisplayName());
            builder.setLore(getLore());
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if ((server.getState() == ServerState.ONLINE)) {
                if (clickType.isLeftClick()) {
                } else {
                    XenonBukkit.getInstance().getRedisModule().sendPacket(new ServerStateChangePacket(server.getName(), ServerState.WHITELISTED));
                    player.sendMessage(CC.translate("&aYou have successfully enabled the &dWhitelist &aon &6" + server.getName() + " Server&a."));
                }
                return;
            } else if (server.getState() == ServerState.WHITELISTED) {
                if (server.getState() == ServerState.WHITELISTED) {
                    XenonBukkit.getInstance().getRedisModule().sendPacket(new ServerStateChangePacket(server.getName(), ServerState.ONLINE));
                    player.sendMessage(CC.translate("&aYou have successfully disabled the &dWhitelist &aon &6" + server.getName() + " Server&a."));
                }
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isRightClick() || clickType.isLeftClick();
        }
    }
}

