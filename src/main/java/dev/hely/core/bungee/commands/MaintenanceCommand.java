package dev.hely.core.bungee.commands;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bungee.commands
//   Date: Tuesday, August 29, 2023 - 2:43 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bungee.OxygenBungee;
import dev.hely.core.bungee.server.data.proxy.Proxy;
import dev.hely.core.bungee.server.packets.ProxyUpdatePacket;
import dev.hely.core.bungee.utils.BungeeCC;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MaintenanceCommand extends Command {
    public MaintenanceCommand() {
        super("maintenance", "oxygen.bungee.maintenance", "maintenancemode");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        Proxy proxy = OxygenBungee.getInstance().getServerManager().proxyFromName(OxygenBungee.getInstance().getServerConfig().getConfiguration().getString("server-name"));
        if (proxy == null) {
            return;
        }
        if (strings.length != 0) {
            if (strings[0].equalsIgnoreCase("toggle")) {
                if (proxy.isMaintenance()) {
                    proxy.setMaintenance(false);
                    OxygenBungee.getInstance().getRedisBungeeModule().sendPacket(new ProxyUpdatePacket(proxy.getName(), proxy.getPort(), proxy.toJsonObject()));
                    commandSender.sendMessage(BungeeCC.translate(OxygenBungee.getInstance().getLanguageConfig().getConfiguration().getString("maintenance-command.disabled")));
                } else {
                    proxy.setMaintenance(true);
                    OxygenBungee.getInstance().getRedisBungeeModule().sendPacket(new ProxyUpdatePacket(proxy.getName(), proxy.getPort(), proxy.toJsonObject()));
                    commandSender.sendMessage(BungeeCC.translate(OxygenBungee.getInstance().getLanguageConfig().getConfiguration().getString("maintenance-command.enabled")));
                }
            } else if (strings[0].equalsIgnoreCase("add")) {
                if (strings.length == 2) {
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(strings[1]);
                    if (target == null) {
                        commandSender.sendMessage(BungeeCC.translate(OxygenBungee.getInstance().getLanguageConfig().getConfiguration().getString("maintenance-command.not-online")));
                        return;
                    }
                    if (proxy.getMaintenanceList().contains(target.getUniqueId())) {
                        commandSender.sendMessage(BungeeCC.translate(OxygenBungee.getInstance().getLanguageConfig().getConfiguration().getString("maintenance-command.already-added")));
                        return;
                    }

                    proxy.getMaintenanceList().add(target.getUniqueId());
                    commandSender.sendMessage(BungeeCC.translate(OxygenBungee.getInstance().getLanguageConfig().getConfiguration().getString("maintenance-command.added").replace("%player%", target.getName())));
                } else {
                    commandSender.sendMessage(BungeeCC.translate(OxygenBungee.getInstance().getLanguageConfig().getConfiguration().getString("maintenance-command.usage")));
                }
            } else if (strings[0].equalsIgnoreCase("remove")) {
                if (strings.length == 2) {
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(strings[1]);
                    if (target == null) {
                        commandSender.sendMessage(BungeeCC.translate(OxygenBungee.getInstance().getLanguageConfig().getConfiguration().getString("maintenance-command.not-online")));
                        return;
                    }
                    if (!proxy.getMaintenanceList().contains(target.getUniqueId())) {
                        commandSender.sendMessage(BungeeCC.translate(OxygenBungee.getInstance().getLanguageConfig().getConfiguration().getString("maintenance-command.not-added")));
                        return;
                    }

                    proxy.getMaintenanceList().add(target.getUniqueId());
                    commandSender.sendMessage(BungeeCC.translate(OxygenBungee.getInstance().getLanguageConfig().getConfiguration().getString("maintenance-command.removed").replace("%player%", target.getName())));
                } else {
                    commandSender.sendMessage(BungeeCC.translate(OxygenBungee.getInstance().getLanguageConfig().getConfiguration().getString("maintenance-command.usage")));
                }
            } else {
                commandSender.sendMessage(BungeeCC.translate(OxygenBungee.getInstance().getLanguageConfig().getConfiguration().getString("maintenance-command.usage")));
            }
        } else {
            commandSender.sendMessage(BungeeCC.translate(OxygenBungee.getInstance().getLanguageConfig().getConfiguration().getString("maintenance-command.usage")));
        }
    }
}
