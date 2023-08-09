package dev.hely.core.bukkit.profile.packet;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.configuration.Locale;
import dev.hely.core.bukkit.modules.database.redis.packet.PacketListener;
import dev.hely.core.bukkit.modules.database.redis.packet.annotation.IncomingPacketHandler;
import dev.hely.core.bukkit.profile.packet.implement.*;
import dev.hely.lib.Messages;
import lombok.AllArgsConstructor;

/**
 * Created By LeandroSSJ
 * Created on 07/07/2023
 * Protect Name OxygenGlobal
 */
@AllArgsConstructor
public class ProfilePacketListener implements PacketListener {

    @IncomingPacketHandler
    public void onStaffJoin(StaffJoinPacket packet){
        String name = packet.getPlayerName();
        String server = packet.getServerName();

        Messages.sendMessage("&9[S] &r%player_name% &bjoined the network &7(%server%)"
                .replace("%player_name%", name)
                .replace("%server%", server), "oxygen.staff.alerts");
    }

    @IncomingPacketHandler
    public void onStaffLeave(StaffLeavePacket packet){
        String name = packet.getPlayerName();
        String server = packet.getServerName();

        Messages.sendMessage("&9[S] &r%player_name% &bdisconnected from &7(%server%)"
                .replace("%player_name%", name)
                .replace("%server%", server), "oxygen.staff.alerts");
    }

    @IncomingPacketHandler
    public void onChatSpyMessage(StaffChatSpyMessagePacket packet){
        String sender = packet.getSenderName();
        String target = packet.getTargetName();
        String server = packet.getServerName();
        String message = packet.getMessage();

        Messages.sendMessage(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("chat-spy.message")
                .replace("%sender%", sender)
                .replace("%target%", target)
                .replace("%server%", server)
                .replace("%message%", message),
                "oxygen.staff.chatspy");
    }

    @IncomingPacketHandler
    public void onStaffSwitch(StaffSwitchPacket packet){
        String name = packet.getPlayerName();
        String toServer = packet.getToServer();
        String fromServer = packet.getFromServer();

        Messages.sendMessage("&9[S] &r%player_name% &bjoined &f%server% &7(from %from%)"
                .replace("%player_name%", name)
                .replace("%server%", toServer)
                .replace("%from%", fromServer), "oxygen.staff.alerts");
    }

    @IncomingPacketHandler
    public void onStaffChat(StaffChatPacket packet){
        String name = packet.getPlayerName();
        String server = packet.getServerName();
        String message = packet.getMessage();

        Messages.sendMessage(Locale.STAFF_CHAT_FORMAT
                .replace("%player%", name)
                .replace("%server_name%", server)
                .replace("%message%", message), "oxygen.staffchat");
    }

    @IncomingPacketHandler
    public void onAdminChat(AdminChatPacket packet){
        String name = packet.getPlayerName();
        String server = packet.getServerName();
        String message = packet.getMessage();

        Messages.sendMessage(Locale.ADMIN_CHAT_FORMAT
                .replace("%player%", name)
                .replace("%server_name%", server)
                .replace("%message%", message), "oxygen.adminchat");
    }

    @IncomingPacketHandler
    public void onStaffMessagePacket(StaffMessagePacket packet){
        String name = packet.getPlayerName();
        String server = packet.getServerName();
        String message = packet.getMessage();

        Messages.sendMessage(Locale.HELPOP_STAFF_ALERT
                .replace("%player%", name)
                .replace("%server%", server)
                .replace("%message%", message), "oxygen.staff");
    }

    @IncomingPacketHandler
    public void onStaffRequestClaimPacket(StaffRequestClaimPacket packet){
        String name = packet.getPlayerName();
        String creator = packet.getCreatorName();

        Messages.sendMessage(Locale.REQUEST_CLAIMED_BROADCAST
                .replace("%player%", name)
                .replace("%creator_name%", creator),
                "oxygen.staff");
    }

    @IncomingPacketHandler
    public void onStaffRequestClosePacket(StaffRequestClosePacket packet){
        String name = packet.getPlayerName();
        String creator = packet.getCreatorName();

        Messages.sendMessage(Locale.REQUEST_CLOSED_BROADCAST
                        .replace("%staff_member%", name)
                        .replace("%player%", creator),
                "oxygen.staff");
    }

    @IncomingPacketHandler
    public void onStaffRequestReportPacket(StaffRequestReportPacket packet){
        String name = packet.getPlayerName();
        String server = packet.getServerName();
        String message = packet.getMessage();
        String reportedPlayer = packet.getReportedPlayer();

        Messages.sendMessage(Locale.REPORT_STAFF_ALERT
                .replace("%player%", name)
                .replace("%server%", server)
                .replace("%message%", message)
                .replace("%reported_player%", reportedPlayer), "oxygen.staff");
    }

    @IncomingPacketHandler
    public void onNotification(FriendSessionPacket packet) {
        packet.notifyPlayers();
    }

    @IncomingPacketHandler
    public void onFriendAccept(FriendRequestAcceptPacket packet) {
        packet.notifyPlayer();
    }

    @IncomingPacketHandler
    public void onFriendSend(FriendRequestSendPacket packet) {
        packet.notifyPlayer();
    }

    @IncomingPacketHandler
    public void onNotificationSend(NotificationSendPacket packet) {
        packet.notifyPlayer();
    }
}
