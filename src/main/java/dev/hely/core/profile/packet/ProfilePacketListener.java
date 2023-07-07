package dev.hely.core.profile.packet;

import dev.hely.core.modules.database.redis.packet.PacketListener;
import dev.hely.core.modules.database.redis.packet.annotation.IncomingPacketHandler;
import dev.hely.core.profile.packet.implement.StaffLeavePacket;
import dev.hely.core.profile.packet.implement.StaffJoinPacket;
import dev.hely.core.profile.packet.implement.StaffSwitchPacket;
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
}
