package dev.hely.core.profile.packet;

import dev.hely.core.configuration.Language;
import dev.hely.core.modules.database.redis.packet.PacketListener;
import dev.hely.core.modules.database.redis.packet.annotation.IncomingPacketHandler;
import dev.hely.core.profile.packet.implement.*;
import dev.hely.lib.Messages;
import lombok.AllArgsConstructor;

/**
 * Created By LeandroSSJ
 * Created on 07/07/2023
 * Protect Name Oxygen
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

        Messages.sendMessage(Language.STAFF_CHAT_FORMAT
                .replace("%player%", name)
                .replace("%server_name%", server)
                .replace("%message%", message), "oxygen.staffchat");
    }

    @IncomingPacketHandler
    public void onAdminChat(AdminChatPacket packet){
        String name = packet.getPlayerName();
        String server = packet.getServerName();
        String message = packet.getMessage();

        Messages.sendMessage(Language.ADMIN_CHAT_FORMAT
                .replace("%player%", name)
                .replace("%server_name%", server)
                .replace("%message%", message), "oxygen.adminchat");
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
