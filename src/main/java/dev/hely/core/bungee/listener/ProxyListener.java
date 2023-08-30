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
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class ProxyListener implements Listener {
    @EventHandler
    public void onPing(ProxyPingEvent event) {
        ServerPing response = event.getResponse();
        Proxy proxy = OxygenBungee.getInstance().getServerManager().proxyFromName(OxygenBungee.getInstance().getServerConfig().getConfiguration().getString("server-name"));
        ServerPing.Players playerCount = response.getPlayers();

        if (OxygenBungee.getInstance().getBungeeConfig().getConfiguration().getBoolean("motd-lines.enabled")) {
            try {
                if (proxy.isMaintenance()) {
                    String serverversion = BungeeCC.translate(OxygenBungee.getInstance().getBungeeConfig().getConfiguration().getString("motd-lines.maintenance.version"));

                    ServerPing.Protocol version = response.getVersion();
                    version.setName(serverversion);
                    version.setProtocol(2);
                    response.setPlayers(playerCount);
                    response.setVersion(version);

                    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
                    format.setTimeZone(TimeZone.getTimeZone(OxygenBungee.getInstance().getBungeeConfig().getConfiguration().getString("motd-lines.cooldown.timezone")));
                    Date date = format.parse(OxygenBungee.getInstance().getBungeeConfig().getConfiguration().getString("motd-lines.cooldown.date"));
                    Date localDate = Calendar.getInstance().getTime();
                    long finalR = date.getTime() - localDate.getTime();

                    response.setDescription(BungeeCC.translate(OxygenBungee.getInstance().getBungeeConfig().getConfiguration().getString("motd-lines.maintenance.top-line").replace("⨠", "\u2a20").replace("|", "\u2503").replace("★", "\u2605").replace("✦", "\u2726").replace("✵", "\u2735").replace("⚠", "\u26a0").replace("⚠", "\u26a0").replace("◀", "\u25C0").replace("▶", "\u25B6").replace("«", "\u00AB").replace("»", "\u00BB").replace("❆", "\u2746").replace("%time%", this.getDate(finalR))) + "\n" +
                            BungeeCC.translate(OxygenBungee.getInstance().getBungeeConfig().getConfiguration().getString("motd-lines.maintenance.bottom-line")
                                    .replace("⨠", "\u2a20")
                                    .replace("|", "\u2503")
                                    .replace("★", "\u2605")
                                    .replace("✦", "\u2726")
                                    .replace("✵", "\u2735")
                                    .replace("⚠", "\u26a0")
                                    .replace("⚠", "\u26a0")
                                    .replace("◀", "\u25C0")
                                    .replace("▶", "\u25B6")
                                    .replace("«", "\u00AB")
                                    .replace("»", "\u00BB")
                                    .replace("❆", "\u2746")
                                    .replace("%cooldown%", this.getDate(finalR))));
                    return;
                }

                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
                format.setTimeZone(TimeZone.getTimeZone(OxygenBungee.getInstance().getBungeeConfig().getConfiguration().getString("motd-lines.cooldown.timezone")));
                Date date = format.parse(OxygenBungee.getInstance().getBungeeConfig().getConfiguration().getString("motd-lines.cooldown.date"));
                Date localDate = Calendar.getInstance().getTime();
                long finalR = date.getTime() - localDate.getTime();

                response.setDescription(BungeeCC.translate(OxygenBungee.getInstance().getBungeeConfig().getConfiguration().getString("motd-lines.normal.top-line").replace("⨠", "\u2a20").replace("|", "\u2503").replace("★", "\u2605").replace("✦", "\u2726").replace("✵", "\u2735").replace("⚠", "\u26a0").replace("⚠", "\u26a0").replace("◀", "\u25C0").replace("▶", "\u25B6").replace("«", "\u00AB").replace("»", "\u00BB").replace("❆", "\u2746").replace("%time%", this.getDate(finalR))) + "\n" +
                        BungeeCC.translate(OxygenBungee.getInstance().getBungeeConfig().getConfiguration().getString("motd-lines.normal.bottom-line")
                                .replace("⨠", "\u2a20")
                                .replace("|", "\u2503")
                                .replace("★", "\u2605")
                                .replace("✦", "\u2726")
                                .replace("✵", "\u2735")
                                .replace("⚠", "\u26a0")
                                .replace("⚠", "\u26a0")
                                .replace("◀", "\u25C0")
                                .replace("▶", "\u25B6")
                                .replace("«", "\u00AB")
                                .replace("»", "\u00BB")
                                .replace("❆", "\u2746")
                                .replace("%cooldown%", this.getDate(finalR))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private String getDate(long dataFinal) {
        if (dataFinal > 0L) {
            StringBuilder builder = new StringBuilder();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(dataFinal);
            long days = TimeUnit.MILLISECONDS.toDays(dataFinal);
            long hours = TimeUnit.SECONDS.toHours(seconds) - (days * 24);
            long minutes = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
            long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) * 60);
            String format = "DETAILED";
            String format2 = "SUMMARIZED";
            String provided = OxygenBungee.getInstance().getBungeeConfig().getConfiguration().getString("motd-lines.cooldown.format").toUpperCase();
            String[] compatible = {"SUMMARIZED", "DETAILED"};
            if (Arrays.asList(compatible).contains(provided)) format = provided;
            if (Arrays.asList(compatible).contains(provided)) format2 = provided;
            switch (format2) {
                case "SUMMARIZED":
                    if (days > 0L) {
                        if (days == 1L) {
                            builder.append(days + "d, ");
                        } else {
                            builder.append(days + "d, ");
                        }
                        if (hours == 1L) {
                            builder.append(hours + "h, ");
                        } else {
                            builder.append(hours + "h, ");
                        }
                        if (minutes == 1L) {
                            builder.append(minutes + "m, ");
                        } else {
                            builder.append(minutes + "m, ");
                        }
                        if (seconds == 1L) {
                            builder.append(second + "s ");
                        } else {
                            builder.append(second + "s ");
                        }
                    } else if (hours > 0L) {
                        if (hours == 1L) {
                            builder.append(hours + "h, ");
                        } else {
                            builder.append(hours + "h, ");
                        }
                        if (minutes == 1L) {
                            builder.append(minutes + "m, ");
                        } else {
                            builder.append(minutes + "m, ");
                        }
                        if (seconds == 1L) {
                            builder.append(second + "s ");
                        } else {
                            builder.append(second + "s ");
                        }
                    } else if (minutes > 0L) {
                        if (minutes == 1L) {
                            builder.append(minutes + "m, ");
                        } else {
                            builder.append(minutes + "m, ");
                        }
                        if (seconds == 1L) {
                            builder.append(second + "s ");
                        } else {
                            builder.append(second + "s ");
                        }
                    } else if (seconds > 0L) {
                        if (seconds == 1L) {
                            builder.append(second + "s ");
                        } else {
                            builder.append(second + "s ");
                        }
                    } else {
                        return ChatColor.translateAlternateColorCodes('&', OxygenBungee.getInstance().getBungeeConfig().getConfiguration().getString("motd-lines.cooldown.end_message"));
                    }
                    return builder.toString();
            }
            switch (format) {
                case "DETAILED":
                    if(days > 0L) {
                        if(days == 1L) {
                            builder.append(days + " day, ");
                        } else {
                            builder.append(days + " days, ");
                        }
                        if(hours == 1L) {
                            builder.append(hours + " hour, ");
                        } else {
                            builder.append(hours + " hours, ");
                        }
                        if(minutes == 1L) {
                            builder.append(minutes + " minute, ");
                        } else {
                            builder.append(minutes + " minutes, ");
                        }
                        if(seconds == 1L) {
                            builder.append(second + " second ");
                        } else {
                            builder.append(second + " seconds ");
                        }
                    } else if(hours > 0L) {
                        if(hours == 1L) {
                            builder.append(hours + " hour, ");
                        } else {
                            builder.append(hours + " hours, ");
                        }
                        if(minutes == 1L) {
                            builder.append(minutes + " minute, ");
                        } else {
                            builder.append(minutes + " minutes, ");
                        }
                        if(seconds == 1L) {
                            builder.append(second + " second ");
                        } else {
                            builder.append(second + " seconds ");
                        }
                    } else if(minutes > 0L) {
                        if(minutes == 1L) {
                            builder.append(minutes + " minute, ");
                        } else {
                            builder.append(minutes + " minutes, ");
                        }
                        if(seconds == 1L) {
                            builder.append(second + " second ");
                        } else {
                            builder.append(second + " seconds ");
                        }
                    } else if(seconds > 0L) {
                        if(seconds == 1L) {
                            builder.append(second + " second ");
                        } else {
                            builder.append(second + " seconds ");
                        }
                    } else {
                        return ChatColor.translateAlternateColorCodes('&', OxygenBungee.getInstance().getBungeeConfig().getConfiguration().getString("motd-lines.cooldown.end_message"));
                    }
                    return builder.toString();
            }
        }else{
            return ChatColor.translateAlternateColorCodes('&', OxygenBungee.getInstance().getBungeeConfig().getConfiguration().getString("motd-lines.cooldown.end_message"));
        }
        return null;
    }
}