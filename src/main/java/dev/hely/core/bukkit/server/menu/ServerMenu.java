package dev.hely.core.bukkit.server.menu;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.server.Server;
import dev.hely.core.bukkit.server.packet.implement.ServerUpdatePacket;
import dev.hely.core.bukkit.staff.menu.submenus.ActiveRequestsMenu;
import dev.hely.core.bukkit.utils.TimeUtil;
import dev.hely.lib.CC;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.button.implement.PageButton;
import dev.hely.lib.menu.implement.ConfirmationMenu;
import dev.hely.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created By LeandroSSJ
 * Created on 06/08/2023
 * Protect Name Oxygen
 */
public class ServerMenu extends PaginatedMenu {
    {
        setPlaceholder(true);
    }

    @Override
    public String getName() {
        return OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("menu.title");
    }

    @Override
    public int getSize() {
        return 36;
    }

    @Override
    public String getTitle(Player player) {
        return OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("menu.title");
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (Server server : OxygenBukkit.getInstance().getServerManager().getServers()) {
            buttons.put(buttons.size(), new Server_Button(server));
        }

        return buttons;
    }

    public Map<Integer, Button> getGlobalContent(Player player) {
        Map<Integer, Button> menuContent = new HashMap<>();

        menuContent.put(0, Button.getPlaceholder());
        menuContent.put(1, Button.getPlaceholder());
        menuContent.put(2, Button.getPlaceholder());

        menuContent.put(3, Button.getPlaceholder());
        menuContent.put(4, Button.getPlaceholder());
        menuContent.put(5, Button.getPlaceholder());

        menuContent.put(6, Button.getPlaceholder());
        menuContent.put(7, Button.getPlaceholder());
        menuContent.put(8, Button.getPlaceholder());

        menuContent.put(16, Button.getPlaceholder());
        menuContent.put(17, Button.getPlaceholder());
        menuContent.put(18, Button.getPlaceholder());

        menuContent.put(19, Button.getPlaceholder());
        menuContent.put(20, new PageButton(-1, this));
        menuContent.put(21, Button.getPlaceholder());

        menuContent.put(22, Button.getPlaceholder());
        menuContent.put(23, Button.getPlaceholder());
        menuContent.put(24, Button.getPlaceholder());

        menuContent.put(25, Button.getPlaceholder());
        menuContent.put(26, new PageButton(1, this));

        return menuContent;
    }

    @AllArgsConstructor
    public static class Server_Button extends Button {
        Server server;

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getServerManager().isServerOnline(server.getName())) {
                if (server.isWhitelisted()) { // The server IS ONLINE and is currently whitelisted.
                    if (OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("menu.buttons.server-button.material.whitelisted.material") != null) {
                        if (Material.getMaterial(OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("menu.buttons.server-button.material.whitelisted.material")) != null) {
                            return Material.valueOf(OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("menu.buttons.server-button.material.whitelisted.material"));
                        } else {
                            Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'whitelisted-server-button' is null, using a default material."));
                            return Material.BOOK;
                        }
                    } else {
                        Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'whitelisted-server-button' is broken, using a default material."));
                        return Material.BOOK;
                    }
                } else { // Server is not whitelisted, and IS ONLINE.
                    if (OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("menu.buttons.server-button.material.online.material") != null) {
                        if (Material.getMaterial(OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("menu.buttons.server-button.material.online.material")) != null) {
                            return Material.valueOf(OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("menu.buttons.server-button.material.online.material"));
                        } else {
                            Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'online-server-button' is null, using a default material."));
                            return Material.INK_SACK;
                        }
                    } else {
                        Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'online-server-button' is broken, using a default material."));
                        return Material.INK_SACK;
                    }
                }
            } else { // The server is NOT online.
                if (OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("menu.buttons.server-button.material.offline.material") != null) {
                    if (Material.getMaterial(OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("menu.buttons.server-button.material.offline.material")) != null) {
                        return Material.valueOf(OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("menu.buttons.server-button.material.offline.material"));
                    } else {
                        Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'offline-server-button' is null, using a default material."));
                        return Material.REDSTONE;
                    }
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'offline-server-button' is broken, using a default material."));
                    return Material.REDSTONE;
                }
            }
        }

        public short getData() {
            if (OxygenBukkit.getInstance().getServerManager().isServerOnline(server.getName())) {
                if (server.isWhitelisted()) { // The server IS ONLINE and whitelisted.
                    return (short) OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getInt("main.buttons.server-button.material.whitelisted.data");
                } else { // The server IS ONLINE and NOT whitelisted.
                    return (short) OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getInt("main.buttons.server-button.material.online.data");
                }
            } else { // The server is not online.
                return (short) OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getInt("main.buttons.server-button.material.offline.data");
            }
        }

        public String getDisplayName() {
            if (OxygenBukkit.getInstance().getServerManager().isServerOnline(server.getName())) {
                if (server.isWhitelisted()) { // The server IS ONLINE and whitelisted.
                    System.out.print("server is whitelisted");
                    return OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("main.buttons.server-button.display-name.whitelisted");
                } else { // The server IS ONLINE and NOT whitelisted.
                    System.out.print("server is online and not whitelisted");
                    return OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("main.buttons.server-button.display-name.online");
                }
            } else { // The server is not online.
                System.out.print("server is not online");
                return OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("main.buttons.server-button.display-name.offline");
            }
        }

        public List<String> getLore() {
            if (OxygenBukkit.getInstance().getServerManager().isServerOnline(server.getName())) {
                if (server.isWhitelisted()) { // The server IS ONLINE and whitelisted.
                    return OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getStringList("main.buttons.server-button.lores.whitelisted");
                } else { // The server IS ONLINE and NOT whitelisted.
                    return OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getStringList("main.buttons.server-button.lores.online");
                }
            } else { // The server is not online.
                return OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getStringList("main.buttons.server-button.lores.offline");
            }
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(getDisplayName().replace("%server_name%", server.getName()));

            for (String line : getLore()) {
                builder.addLore(line
                        .replace("%server_name%", server.getName())
                        .replace("%server_version%", server.getVersion())
                        .replace("%server_players%", String.valueOf(server.getOnlinePlayers()))
                        .replace("%server_started%", TimeUtil.formatDateDiff(server.getStartedAt()))
                        .replace("%server_last_update%", TimeUtil.formatDateDiff(server.getLastUpdate()))
                        .replace("%server_last_online%", TimeUtil.formatDateDiff(server.getLastOnline()))
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (OxygenBukkit.getInstance().getServerManager().isServerOnline(server.getName())) {
                if (server.isWhitelisted()) { // The server IS ONLINE and whitelisted.
                    if (clickType.isLeftClick()) {

                    } else {
                        OxygenBukkit.getInstance().getRedisModule().sendPacket(new ServerUpdatePacket(
                                server.getName(),
                                server.getVersion(),
                                System.currentTimeMillis(),
                                server.getTps(),
                                false,
                                server.getMaxPlayers(),
                                server.getOnlinePlayers(),
                                server.getLastUpdate(),
                                server.getLastOnline()
                        ));
                        player.sendMessage(CC.translate(OxygenBukkit.getInstance().getServersMenuConfig().getConfiguration().getString("main.messages.whitelist.disabled").replace("%server_name%", server.getName())));
                    }
                } else { // The server IS ONLINE and NOT whitelisted.
                }
            } else { // The server is not online.
            }
        }
    }
}
