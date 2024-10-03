package net.ascend.core.bukkit.server.menu;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.lib.menu.implement.ConfirmationMenu;
import net.ascend.core.bukkit.server.Server;
import net.ascend.core.bukkit.server.ServerState;
import net.ascend.core.bukkit.server.packet.impl.ServerCommandExecutePacket;

import net.ascend.core.bukkit.util.TPSUtil;
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

public class ServerListMenu extends PaginatedMenu {

    {
        setAutoUpdate(true);
    }

    @Override
    public String getName() {
        return "Server Manager";
    }

    @Override
    public String getTitle(Player player) {
        return "Server Manager";
    }

    @Override
    public int getSize() {
        return 5 * 9;
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        List<Server> servers = new ArrayList<>(XenonBukkit.getInstance().getServerManager().getServers());
        List<String> currentList = servers.stream()
                .sorted(Comparator.comparing(Server::getState))
                .map(Server::getName)
                .distinct()
                .collect(Collectors.toList());

        currentList.forEach(serverName -> {
            Server server = XenonBukkit.getInstance().getServerManager().getServer(serverName);
            buttons.put(buttons.size(), new ServerButton(server));
        });

        return buttons;
    }

    @AllArgsConstructor
    public static class ServerButton extends Button {

        private final Server server;

        public String getMaterial() {
            if(server.getState() == ServerState.OFFLINE) return "REDSTONE_BLOCK";
            if(server.getState() == ServerState.WHITELISTED) return "GOLD_BLOCK";
            if(server.getState() == ServerState.ONLINE) return "EMERALD_BLOCK";
            return "PAPER";
        }


        public String getDisplayName() {
            if(server.getState() == ServerState.OFFLINE) {
                return "&f" + server.getName() + " &c[Offline]";
            } else if (server.getState() == ServerState.WHITELISTED) {
                return "&f" + server.getName() + " &d[Whitelisted]";
            } else if (server.getState() == ServerState.ONLINE) {
                return "&f" + server.getName() + " &a[Online]";
            }
            return "";
        }



        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setDisplayName(getDisplayName());

            if (server.getState() == ServerState.ONLINE) {
                builder.setLore(
                        "",
                        "&6This server is currently &aonline&6.",
                        "",
                        "&6Server Information:",
                        " &7- &6Status&7: &f" + server.getState().getDisplayName(),
                        " &7- &6Players&7: &f" + server.getOnlinePlayers() + "&7/&f" + server.getMaxPlayers(),
                        " &7- &6TPS&7: &f" + TPSUtil.getTPSStatus(server.getTps()),
                        " &7- &6Start Time&7: &f" + TimeUtil.formatIntoCalendarString(new Date(server.getStartTime())),
                        " &7- &6Uptime&7: &f" + TimeUtil.formatTimeMillis(System.currentTimeMillis() - server.getStartTime()),
                        " &7- &6Last Heartbeat&7: &f" + TimeUtil.formatTimeMillis(System.currentTimeMillis() - server.getLastHeartbeat()),
                        "",
                        "&7Left-Click to shutdown this server.",
                        "&7Right-Click to toggle the whitelist.");
            }

            if (server.getState() == ServerState.WHITELISTED) {
                builder.setLore(
                        "",
                        "&6This server is currently &dwhitelisted&6.",
                        "",
                        "&6Server Information:",
                        " &7- &6Status&7: &f" + server.getState().getDisplayName(),
                        " &7- &6Players&7: &f" + server.getOnlinePlayers() + "&7/&f" + server.getMaxPlayers(),
                        " &7- &6TPS&7: &f" + TPSUtil.getTPSStatus(server.getTps()),
                        " &7- &6Start Time&7: &f" + TimeUtil.formatIntoCalendarString(new Date(server.getStartTime())),
                        " &7- &6Uptime&7: &f" + TimeUtil.formatTimeMillis(System.currentTimeMillis() - server.getStartTime()),
                        " &7- &6Last Heartbeat&7: &f" + TimeUtil.formatTimeMillis(System.currentTimeMillis() - server.getLastHeartbeat()),
                        "",
                        "&7Left-Click to shutdown this server.",
                        "&7Right-Click to toggle the whitelist.");
            }

            if (server.getState() == ServerState.OFFLINE) {
                builder.setLore("&7This server is currently &coffline&e.");
            }

            builder.addFakeGlow();
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (server.getState() == ServerState.OFFLINE) return;

            if (clickType.isLeftClick()) {
                new ConfirmationMenu("Shutdown " + server.getName() + "?", (b) -> {
                    if (b) {
                        player.sendMessage(CC.translate(""));
                        player.sendMessage(CC.translate("&aYou have successfully shutdown " + server.getName() + " server."));
                        player.sendMessage(CC.translate(""));

                        XenonBukkit.getInstance().getRedisModule().sendPacket(new ServerCommandExecutePacket(server.getName(), player.getName(), "stop"));
                    } else {
                        player.closeInventory();
                    }
                }).openMenu(player);
            } else if (clickType.isRightClick()) {

            }
        }
    }
}

