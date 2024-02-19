package net.ascend.core.bukkit.server.menu;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.server.menu
//   Date: Wednesday, August 16, 2023 - 10:23 AM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.core.OxygenMenu;
import net.ascend.core.bukkit.core.buttons.InfoButton;
import net.ascend.core.bukkit.server.Server;
import net.ascend.core.bukkit.server.ServerState;
import net.ascend.core.bukkit.server.queue.packet.implement.QueueSendPlayerPacket;
import net.ascend.core.bukkit.util.TPSUtil;
import net.ascend.core.bukkit.util.TimeUtil;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.button.implement.PageButton;
import net.ascend.core.bukkit.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@AllArgsConstructor
public class ServerListMenu extends PaginatedMenu {
    {
        setClickUpdate(true);
    }
    Player player;

    @Override
    public String getName() {
        return player.getName() + " - Server List";
    }

    @Override
    public String getTitle(Player player) {
        return player.getName() + " - Server List";
    }

    @Override
    public int getSize() {
        return 45;
    }

    public Map<Integer, Button> getGlobalContent(Player player) {
        Map<Integer, Button> menuContent = new HashMap<>();

        menuContent.put(0, new Back_Button());
        menuContent.put(1, new Placeholder_Button());
        menuContent.put(4, new InfoButton());
        menuContent.put(7, new Placeholder_Button());
        menuContent.put(8, new Placeholder_Button());
        menuContent.put(9, new Placeholder_Button());
        menuContent.put(17, new Placeholder_Button());
        menuContent.put(18, new PageButton(-1, this));
        menuContent.put(26, new PageButton(1, this));
        menuContent.put(27, new Placeholder_Button());
        menuContent.put(35, new Placeholder_Button());
        menuContent.put(36, new Placeholder_Button());
        menuContent.put(37, new Placeholder_Button());
        menuContent.put(43, new Placeholder_Button());
        menuContent.put(44, new Placeholder_Button());

        return menuContent;
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        List<Server> servers = new ArrayList<>(XenonBukkit.getInstance().getServerManager().getServersList());
        List<String> currentList = new ArrayList<>();
        XenonBukkit.getInstance().getServerManager().removeClosedServers();
        servers.sort(Comparator.comparing(Server::getState));
        for (Server server : servers) {
            if (currentList.contains(server.getName())) {
                continue;
            }
            currentList.add(server.getName());
            buttons.put(buttons.size(), new ServerButton(server));
        }

        return buttons;
    }

    @AllArgsConstructor
    public static class Placeholder_Button extends Button {
        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of("STAINED_GLASS_PANE");
            builder.setData((short) 1);
            builder.setDisplayName("&7");
            builder.addFakeGlow();
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
        }
    }

    @AllArgsConstructor
    public static class Back_Button extends Button {
        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of("BED");
            builder.setData((short) 0);
            builder.setDisplayName("&cBack");
            builder.setLore(Collections.singletonList(
                    "&7Click here to return to the main menu."
            ));
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            new OxygenMenu(player).openMenu(player);
        }
    }

    @AllArgsConstructor
    public static class ServerButton extends Button {
        Server server;

        public String getMaterial() {
            if(server.getState() == ServerState.OFFLINE) {
                return XenonBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("menu.buttons.server-button.material.offline.material");
            } else if (server.getState() == ServerState.HEARTBEAT_TIMEOUT) {
                return XenonBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("menu.buttons.server-button.material.offline.material");
            } else if (server.getState() == ServerState.UNKNOWN) {
                return XenonBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("menu.buttons.server-button.material.offline.material");
            } else if (server.getState() == ServerState.WHITELISTED) {
                return XenonBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("menu.buttons.server-button.material.whitelisted.material");
            } else if (server.getState() == ServerState.ONLINE) {
                return XenonBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("menu.buttons.server-button.material.online.material");
            }

            return "AIR";
        }

        public int getData() {
            if(server.getState() == ServerState.OFFLINE) {
                return XenonBukkit.getInstance().getServersMenuConfig().getConfiguration().getInt("menu.buttons.server-button.material.offline.data");
            } else if (server.getState() == ServerState.HEARTBEAT_TIMEOUT) {
                return XenonBukkit.getInstance().getServersMenuConfig().getConfiguration().getInt("menu.buttons.server-button.material.offline.data");
            } else if (server.getState() == ServerState.UNKNOWN) {
                return XenonBukkit.getInstance().getServersMenuConfig().getConfiguration().getInt("menu.buttons.server-button.material.offline.data");
            } else if (server.getState() == ServerState.WHITELISTED) {
                return XenonBukkit.getInstance().getServersMenuConfig().getConfiguration().getInt("menu.buttons.server-button.material.whitelisted.data");
            } else if (server.getState() == ServerState.ONLINE) {
                return XenonBukkit.getInstance().getServersMenuConfig().getConfiguration().getInt("menu.buttons.server-button.material.online.data");
            }
            return 0;
        }

        public String getDisplayName() {
            if(server.getState() == ServerState.OFFLINE) {
                return XenonBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("menu.buttons.server-button.display-name.offline");
            } else if (server.getState() == ServerState.HEARTBEAT_TIMEOUT) {
                return XenonBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("menu.buttons.server-button.display-name.offline");
            } else if (server.getState() == ServerState.UNKNOWN) {
                return XenonBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("menu.buttons.server-button.display-name.offline");
            } else if (server.getState() == ServerState.WHITELISTED) {
                return XenonBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("menu.buttons.server-button.display-name.whitelisted");
            } else if (server.getState() == ServerState.ONLINE) {
                return XenonBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("menu.buttons.server-button.display-name.online");
            }
            return "";
        }

        public List<String> getLore() {
            if(server.getState() == ServerState.OFFLINE) {
                return XenonBukkit.getInstance().getServersMenuConfig().getConfiguration().getStringList("menu.buttons.server-button.lores.offline");
            } else if (server.getState() == ServerState.HEARTBEAT_TIMEOUT) {
                return XenonBukkit.getInstance().getServersMenuConfig().getConfiguration().getStringList("menu.buttons.server-button.lores.offline");
            } else if (server.getState() == ServerState.UNKNOWN) {
                return XenonBukkit.getInstance().getServersMenuConfig().getConfiguration().getStringList("menu.buttons.server-button.lores.offline");
            } else if (server.getState() == ServerState.WHITELISTED) {
                return XenonBukkit.getInstance().getServersMenuConfig().getConfiguration().getStringList("menu.buttons.server-button.lores.whitelisted");
            } else {
                return XenonBukkit.getInstance().getServersMenuConfig().getConfiguration().getStringList("menu.buttons.server-button.lores.online");
            }
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData((short) getData());
            builder.setDisplayName(getDisplayName().replace("%server_name%", server.getName()));

            for (String lore_line : getLore()) {
                builder.addLore(lore_line
                        .replace("%tps_status%", TPSUtil.getTPSStatus(server.getTps()))
                        .replace("%online%", String.valueOf(server.getOnlinePlayers()))
                        .replace("%max%", String.valueOf(server.getMaxPlayers()))
                        .replace("%status%", server.getState().getDisplayName())
                        .replace("%start_time%", TimeUtil.formatIntoCalendarString(new Date(server.getStartTime())))
                        .replace("%uptime%", TimeUtil.formatTimeMillis(System.currentTimeMillis() - server.getStartTime()))
                        .replace("%last_heartbeat%", TimeUtil.formatTimeMillis(System.currentTimeMillis() - server.getLastHeartbeat()))
                );
            }
            builder.addFakeGlow();
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.isLeftClick()) {
                XenonBukkit.getInstance().getRedisModule().sendPacket(new QueueSendPlayerPacket(server.getName(), player.getUniqueId()));
            } else if (clickType.isRightClick()) {
            }
        }
    }
}

