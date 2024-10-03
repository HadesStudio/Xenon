package net.ascend.core.bukkit.profile.packet;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.modules.database.redis.packet.PacketListener;
import net.ascend.core.bukkit.modules.database.redis.packet.annotation.IncomingPacketHandler;
import net.ascend.core.bukkit.profile.packet.implement.*;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.lib.Messages;
import lombok.AllArgsConstructor;
import net.ascend.core.bukkit.profile.packet.implement.staff.*;
import net.ascend.core.bukkit.staff.request.packet.StaffRequestClaimPacket;
import net.ascend.core.bukkit.staff.request.packet.StaffRequestClosePacket;
import net.ascend.core.bukkit.staff.request.packet.StaffRequestPacket;
import net.ascend.core.bukkit.staff.request.packet.StaffRequestReportPacket;
import org.bukkit.entity.Player;

/**
 * Created By LeandroSSJ
 * Created on 07/07/2023
 * Protect Name Xenon
 */
@AllArgsConstructor
public class ProfilePacketListener implements PacketListener {

    @IncomingPacketHandler
    public void onStaffJoin(StaffJoinPacket packet){
        String name = packet.getPlayerName();
        String server = packet.getServerName();

        Messages.sendMessage(Locale.STAFF_MESSAGES_CONNECT
                .replace("%player_name%", name)
                .replace("%server%", server), "xenon.staff.alerts");
    }

    @IncomingPacketHandler
    public void onStaffLeave(StaffLeavePacket packet){
        String name = packet.getPlayerName();
        String server = packet.getServerName();

        Messages.sendMessage(Locale.STAFF_MESSAGES_DISCONNECT
                .replace("%player_name%", name)
                .replace("%server%", server), "xenon.staff.alerts");
    }

    @IncomingPacketHandler
    public void onStaffSwitch(StaffSwitchPacket packet){
        String name = packet.getPlayerName();
        String toServer = packet.getToServer();
        String fromServer = packet.getFromServer();

        Messages.sendMessage(Locale.STAFF_MESSAGES_SWITCH
                .replace("%player_name%", name)
                .replace("%server%", toServer)
                .replace("%from%", fromServer), "xenon.staff.alerts");
    }

    @IncomingPacketHandler
    public void onStaffChat(StaffChatPacket packet){
        String name = packet.getPlayerName();
        String server = packet.getServerName();
        String message = packet.getMessage();

        Messages.sendMessage(Locale.STAFF_CHAT_FORMAT
                .replace("%player%", name)
                .replace("%server_name%", server)
                .replace("%message%", message), "xenon.staffchat");
    }


    @IncomingPacketHandler
    public void onAdminChat(AdminChatPacket packet){
        String name = packet.getPlayerName();
        String server = packet.getServerName();
        String message = packet.getMessage();

        Messages.sendMessage(Locale.ADMIN_CHAT_FORMAT
                .replace("%player%", name)
                .replace("%server_name%", server)
                .replace("%message%", message), "xenon.adminchat");
    }

    @IncomingPacketHandler
    public void onStaffMessagePacket(StaffRawMessagePacket packet) {
        String server = packet.getServerName();
        String permission = packet.getPermission();
        String message = packet.getMessage();

        Messages.sendMessage(message.replace("%server%", server), permission);
    }

    @IncomingPacketHandler
    public void onStaffMessagePacket(StaffRequestPacket packet){
        String name = packet.getPlayerName();
        String server = packet.getServerName();
        String message = packet.getMessage();

        for (String string : Locale.HELPOP_STAFF_ALERT) {
            Messages.sendMessage(string
                    .replace("%player_name%", name)
                    .replace("%server%", server)
                    .replace("%request%", message), "xenon.staff");
        }
    }


    @IncomingPacketHandler
    public void onStaffRequestClaimPacket(StaffRequestClaimPacket packet){
        String name = packet.getPlayerName();
        String creator = packet.getCreatorName();

        Messages.sendMessage(Locale.REQUEST_CLAIMED_BROADCAST
                .replace("%player%", name)
                .replace("%creator_name%", creator),
                "xenon.staff");
    }

    @IncomingPacketHandler
    public void onStaffRequestClosePacket(StaffRequestClosePacket packet){
        String name = packet.getPlayerName();
        String creator = packet.getCreatorName();

        Messages.sendMessage(Locale.REQUEST_CLOSED_BROADCAST
                        .replace("%staff_member%", name)
                        .replace("%player%", creator),
                "xenon.staff");
    }

    @IncomingPacketHandler
    public void onStaffRequestReportPacket(StaffRequestReportPacket packet){
        String name = packet.getPlayerName();
        String server = packet.getServerName();
        String message = packet.getMessage();
        String reportedPlayer = packet.getReportedPlayer();

        for (String string : Locale.REPORT_STAFF_ALERT) {
            Messages.sendMessage(string
                    .replace("%player_name%", name)
                    .replace("%server%", server)
                    .replace("%reason%", message)
                    .replace("%target_name%", reportedPlayer), "xenon.staff");
        }
    }

    @IncomingPacketHandler
    public void onProfileMessagePacket(ProfileMessagePacket packet) {
        Player player = packet.getPlayer();
        if (player != null && player.isOnline()) {
            Messages.sendMessage(CC.translate(packet.getMessage()));
        }
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
}
