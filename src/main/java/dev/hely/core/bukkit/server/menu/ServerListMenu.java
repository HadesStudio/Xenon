package dev.hely.core.bukkit.server.menu;

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

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.core.menus.OxygenMenu;
import dev.hely.core.bukkit.core.menus.buttons.InfoButton;
import dev.hely.core.bukkit.server.Server;
import dev.hely.core.bukkit.server.ServerState;
import dev.hely.core.bukkit.server.queue.packets.implement.QueueSendPlayerPacket;
import dev.hely.core.bukkit.utils.TPSUtil;
import dev.hely.core.bukkit.utils.TimeUtil;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.button.implement.PageButton;
import dev.hely.lib.menu.implement.PaginatedMenu;
import dev.hely.lib.util.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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

        List<Server> servers = new ArrayList<>(OxygenBukkit.getInstance().getServerManager().getServersList());
        List<String> currentList = new ArrayList<>();
        OxygenBukkit.getInstance().getServerManager().removeClosedServers();
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
            ItemMaker builder = ItemMaker.of(Material.STAINED_GLASS_PANE);
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
            ItemMaker builder = ItemMaker.of(Material.BED);
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

        public Material getMaterial() {
            if(server.getState() == ServerState.OFFLINE) {
                String material = OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("menu.buttons.server-button.material.offline.material");
                Material default_value = Material.REDSTONE_BLOCK;
                if (material != null) {
                    if (Material.getMaterial(material) != null) {
                        return Material.valueOf(material);
                    } else {
                        Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'server-material-button' is null, using a default material."));
                        return default_value;
                    }
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'server-material-button' is broken, using a default material."));
                    return default_value;
                }
            } else if (server.getState() == ServerState.HEARTBEAT_TIMEOUT) {
                String material = OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("menu.buttons.server-button.material.offline.material");
                Material default_value = Material.REDSTONE_BLOCK;
                if (material != null) {
                    if (Material.getMaterial(material) != null) {
                        return Material.valueOf(material);
                    } else {
                        Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'server-material-button' is null, using a default material."));
                        return default_value;
                    }
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'server-material-button' is broken, using a default material."));
                    return default_value;
                }
            } else if (server.getState() == ServerState.UNKNOWN) {
                String material = OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("menu.buttons.server-button.material.offline.material");
                Material default_value = Material.REDSTONE_BLOCK;
                if (material != null) {
                    if (Material.getMaterial(material) != null) {
                        return Material.valueOf(material);
                    } else {
                        Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'server-material-button' is null, using a default material."));
                        return default_value;
                    }
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'server-material-button' is broken, using a default material."));
                    return default_value;
                }
            } else if (server.getState() == ServerState.WHITELISTED) {
                String material = OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("menu.buttons.server-button.material.whitelisted.material");
                Material default_value = Material.LAPIS_BLOCK;
                if (material != null) {
                    if (Material.getMaterial(material) != null) {
                        return Material.valueOf(material);
                    } else {
                        Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'server-material-button' is null, using a default material."));
                        return default_value;
                    }
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'server-material-button' is broken, using a default material."));
                    return default_value;
                }
            } else if (server.getState() == ServerState.ONLINE) {
                String material = OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("menu.buttons.server-button.material.online.material");
                Material default_value = Material.EMERALD_BLOCK;
                if (material != null) {
                    if (Material.getMaterial(material) != null) {
                        return Material.valueOf(material);
                    } else {
                        Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'server-material-button' is null, using a default material."));
                        return default_value;
                    }
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'server-material-button' is broken, using a default material."));
                    return default_value;
                }
            }

            return null;
        }

        public int getData() {
            if(server.getState() == ServerState.OFFLINE) {
                return OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getInt("menu.buttons.server-button.material.offline.data");
            } else if (server.getState() == ServerState.HEARTBEAT_TIMEOUT) {
                return OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getInt("menu.buttons.server-button.material.offline.data");
            } else if (server.getState() == ServerState.UNKNOWN) {
                return OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getInt("menu.buttons.server-button.material.offline.data");
            } else if (server.getState() == ServerState.WHITELISTED) {
                return OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getInt("menu.buttons.server-button.material.whitelisted.data");
            } else if (server.getState() == ServerState.ONLINE) {
                return OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getInt("menu.buttons.server-button.material.online.data");
            }
            return 0;
        }

        public String getDisplayName() {
            if(server.getState() == ServerState.OFFLINE) {
                return OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("menu.buttons.server-button.display-name.offline");
            } else if (server.getState() == ServerState.HEARTBEAT_TIMEOUT) {
                return OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("menu.buttons.server-button.display-name.offline");
            } else if (server.getState() == ServerState.UNKNOWN) {
                return OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("menu.buttons.server-button.display-name.offline");
            } else if (server.getState() == ServerState.WHITELISTED) {
                return OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("menu.buttons.server-button.display-name.whitelisted");
            } else if (server.getState() == ServerState.ONLINE) {
                return OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("menu.buttons.server-button.display-name.online");
            }
            return "";
        }

        public List<String> getLore() {
            if(server.getState() == ServerState.OFFLINE) {
                return OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getStringList("menu.buttons.server-button.lores.offline");
            } else if (server.getState() == ServerState.HEARTBEAT_TIMEOUT) {
                return OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getStringList("menu.buttons.server-button.lores.offline");
            } else if (server.getState() == ServerState.UNKNOWN) {
                return OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getStringList("menu.buttons.server-button.lores.offline");
            } else if (server.getState() == ServerState.WHITELISTED) {
                return OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getStringList("menu.buttons.server-button.lores.whitelisted");
            } else {
                return OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getStringList("menu.buttons.server-button.lores.online");
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
                OxygenBukkit.getInstance().getRedisModule().sendPacket(new QueueSendPlayerPacket(server.getName(), player.getUniqueId()));
            } else if (clickType.isRightClick()) {
            }
        }
    }
}

