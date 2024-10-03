package net.ascend.core.bukkit.chat.listener;

import lombok.AllArgsConstructor;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.chat.ChatManager;
import net.ascend.core.bukkit.filter.Filter;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.packet.implement.staff.AdminChatPacket;
import net.ascend.core.bukkit.profile.packet.implement.staff.StaffChatPacket;
import net.ascend.core.bukkit.util.TimeUtil;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@AllArgsConstructor
public class ChatListener implements Listener {

    private ChatManager chatManager;

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    private void onAsyncChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (profile.getStaffProfile().isStaffChat() || profile.getStaffProfile().isAdminChat()) {
            event.setCancelled(true);
            return;
        }

        if (chatManager.isMuted()) {
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("chat.muted")));
            event.setCancelled(true);
            return;
        }

        if (chatManager.getSlowTime() > 0) {
            if (chatManager.getSlowChatRemaining(player.getUniqueId()) > 0 && (!player.hasPermission("xenon.chat.bypass"))) {
                event.setCancelled(true);
                for (String message : XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("chat.slow-chat")) {
                    message = message.replace("%time%", String.valueOf(chatManager.getSlowTime()))
                            .replace("%wait_time%", TimeUtil.formatTimeMillis((int) chatManager.getSlowChatRemaining(player.getUniqueId())));

                    player.sendMessage(CC.translate(message));
                }
                return;
            }

            chatManager.getSlowCache().put(player.getUniqueId(), System.currentTimeMillis());
        }

        String msg = event.getMessage();

        Filter filter = XenonBukkit.getInstance().getFilterManager().checkMessage(msg);
        if (filter != null && !player.hasPermission("xenon.filter.bypass")) {
            filter.chatPunish(msg, profile);

            event.setCancelled(true);
            return;
        }

        event.setCancelled(true);

        ConsoleCommandSender console = Bukkit.getConsoleSender();
        console.sendMessage(chatManager.formatChatDisplay(player, msg));

        event.getRecipients().forEach(recipient -> {
            Profile recipientProfile = XenonBukkit.getInstance().getProfileManager().getProfile(recipient.getUniqueId());
            if ((player != recipient && !player.hasPermission("xenon.ignore.bypass") || recipientProfile.getIgnoredList().contains(player.getName())))
                return;

            recipient.sendMessage(chatManager.formatChatDisplay(player, msg));

        });
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void handleStaffChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("xenon.staffchat")) return;

        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (profile != null) {
            if (profile.getStaffProfile().isStaffChat() && !profile.getStaffProfile().isAdminChat()) {
                String message = event.getMessage();

                XenonBukkit.getInstance().getRedisModule().sendPacket(new StaffChatPacket(player.getName(), profile.getCurrentServer(), message));

                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void handleAdminChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("xenon.adminchat")) return;

        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (profile != null) {
            if (profile.getStaffProfile().isAdminChat() && !profile.getStaffProfile().isStaffChat() ||
                    profile.getStaffProfile().isAdminChat() && profile.getStaffProfile().isStaffChat()) {
                String message = event.getMessage();

                XenonBukkit.getInstance().getRedisModule().sendPacket(new AdminChatPacket(player.getName(), profile.getCurrentServer(), message));

                event.setCancelled(true);
            }
        }
    }
}
