package dev.hely.core.bungee.listener;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bungee.listener
//   Date: Tuesday, August 29, 2023 - 3:00 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bungee.OxygenBungee;
import dev.hely.core.bungee.server.data.proxy.Proxy;
import dev.hely.core.bungee.utils.BungeeCC;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;

@RequiredArgsConstructor
public class ProxyListener implements Listener {
    @EventHandler
    public void onPing(ProxyPingEvent event) {
        ServerPing response = event.getResponse();
        Proxy proxy = OxygenBungee.getInstance().getServerManager().proxyFromName(OxygenBungee.getInstance().getServerConfig().getConfiguration().getString("server-name"));

        if (proxy.isMaintenance()) {
            response.setDescription(StringUtils.join(BungeeCC.translate(Arrays.asList(OxygenBungee.getInstance().getBungeeConfig().getConfiguration().getString("motd-lines.maintenance.top-line"), OxygenBungee.getInstance().getBungeeConfig().getConfiguration().getString("motd-lines.maintenance.bottom-line"))), "\n"));
            event.setResponse(response);
        } else {
            response.setDescription(StringUtils.join(BungeeCC.translate(Arrays.asList(OxygenBungee.getInstance().getBungeeConfig().getConfiguration().getString("motd-lines.normal.top-line"), OxygenBungee.getInstance().getBungeeConfig().getConfiguration().getString("motd-lines.normal.bottom-line"))), "\n"));
            event.setResponse(response);
        }
    }
}
