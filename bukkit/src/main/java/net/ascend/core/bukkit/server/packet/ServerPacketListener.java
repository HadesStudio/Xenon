package net.ascend.core.bukkit.server.packet;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.modules.database.redis.packet.PacketListener;
import net.ascend.core.bukkit.modules.database.redis.packet.annotation.IncomingPacketHandler;

import net.ascend.core.bukkit.profile.packet.implement.staff.StaffRawMessagePacket;
import net.ascend.core.bukkit.server.Server;
import net.ascend.core.bukkit.server.ServerManager;
import net.ascend.core.bukkit.server.packet.impl.ServerCommandExecutePacket;
import net.ascend.core.bukkit.server.packet.impl.ServerStateChangePacket;
import net.ascend.core.bukkit.server.packet.impl.ServerUpdatePacket;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;

@AllArgsConstructor
public class ServerPacketListener implements PacketListener {

    private ServerManager serverManager;

    @IncomingPacketHandler
    public void onServerUpdate(ServerStateChangePacket packet) {
        String server = packet.getName();
        boolean state = packet.isOnline();

        //String message = XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("server-manager.state-changed").replace("%server%", server.getName()).replace("%state%", serverState.getDisplayName());
        //XenonBukkit.getInstance().getRedisModule().sendPacket(new StaffRawMessagePacket("xenon.staff", server.getName(), message));
    }

    @IncomingPacketHandler
    public void onServerCommandExecute(ServerCommandExecutePacket packet) {
        Server server = this.serverManager.getServer(packet.getServer());
        String executor = packet.getExecutor();
        String command = packet.getCommand();

        if (server == null) return;
        if (!this.serverManager.getCurrentServerName().equalsIgnoreCase(server.getName())) return;

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);

        String message = Locale.SERVER_MANAGER_RUNCMD_ALERT
                .replace("%server%", server.getName())
                .replace("%command%", command)
                .replace("%sender%", executor);

        XenonBukkit.getInstance().getRedisModule().sendPacket(new StaffRawMessagePacket(
                "xenon.command.servermanager.argument.runcmd",
                server.getName(),
                message));
    }

    @IncomingPacketHandler
    public void onServerUpdate(ServerUpdatePacket packet) {
        this.serverManager.updateServer(packet.getServer());
    }
}
