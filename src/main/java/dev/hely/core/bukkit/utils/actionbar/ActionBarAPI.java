package dev.hely.core.bukkit.utils.actionbar;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import dev.hely.core.bukkit.OxygenBukkit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;

public class ActionBarAPI {

    private static final short SERVER_VERSION;

    public static void sendActionBar(Player player, String string) throws InvocationTargetException {
        if (player == null || !player.isOnline()) return;

        ActionBarMessageEvent actionBarMessageEvent = new ActionBarMessageEvent(player, string);
        Bukkit.getPluginManager().callEvent(actionBarMessageEvent);

        if (actionBarMessageEvent.isCancelled()) return;

        WrappedChatComponent wrappedChatComponent = WrappedChatComponent.fromText(string);
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        PacketContainer packetContainer;

        if (SERVER_VERSION >= 19) {
            packetContainer = protocolManager.createPacket(PacketType.Play.Server.CHAT);
            StructureModifier<Integer> structureModifier = packetContainer.getIntegers();
            int size = structureModifier.size();

            if (size == 1) {
                structureModifier.write(0, (int) EnumWrappers.ChatType.GAME_INFO.getId());
            } else {
                StructureModifier<Boolean> structureModifier2 = packetContainer.getBooleans();
                structureModifier2.write(0, true);
            }

            StructureModifier<String> structureModifier3 = packetContainer.getStrings();
            structureModifier3.write(0, wrappedChatComponent.getJson());
        } else if (SERVER_VERSION > 16 || SERVER_VERSION < 11) {
            packetContainer = protocolManager.createPacket(PacketType.Play.Server.CHAT);
            StructureModifier<EnumWrappers.ChatType> structureModifier = packetContainer.getChatTypes();
            structureModifier.write(0, EnumWrappers.ChatType.GAME_INFO);
            int size = packetContainer.getBytes().size();

            if (size == 1) {
                StructureModifier<Byte> structureModifier4 = packetContainer.getBytes();
                structureModifier4.write(0, (byte) 2);
            }

            StructureModifier<WrappedChatComponent> structureModifier5 = packetContainer.getChatComponents();
            structureModifier5.write(0, wrappedChatComponent);
        } else {
            packetContainer = protocolManager.createPacket(PacketType.Play.Server.TITLE);
            StructureModifier<EnumWrappers.TitleAction> structureModifier = packetContainer.getTitleActions();
            structureModifier.write(0, EnumWrappers.TitleAction.ACTIONBAR);
            StructureModifier<WrappedChatComponent> structureModifier6 = packetContainer.getChatComponents();
            structureModifier6.write(0, wrappedChatComponent);
        }

        protocolManager.sendServerPacket(player, packetContainer);
    }

    static {
        String[] versionArray = Bukkit.getServer().getBukkitVersion().split("\\.");
        String[] versionArray2 = versionArray[1].split("-");
        SERVER_VERSION = Short.parseShort(versionArray2[0]);
    }

    public static void sendActionBarToAllPlayers(OxygenBukkit plugin, String string) throws InvocationTargetException {
        sendActionBarToAllPlayers(plugin, string, -1);
    }

    public static void sendActionBar(OxygenBukkit plugin, Player player, String string, int duration) throws InvocationTargetException {
        sendActionBar(player, string);
        BukkitRunnable bukkitRunnable = new BukkitRunnable() {
            Player iPlayer;
            int ticks;

            public void run() {
                ticks++;
                if (ticks > duration) {
                    this.cancel();
                    return;
                }
                if (ticks % 20 != 0) {
                    return;
                }
                try {
                    sendActionBar(iPlayer, string);
                } catch (InvocationTargetException e) {
                }
            }
        };
        bukkitRunnable.runTaskTimer(plugin, 1L, 1L);
    }

    public static void sendActionBarToAllPlayers(OxygenBukkit plugin, String string, int duration) throws InvocationTargetException {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendActionBar(plugin, player, string, duration);
        }
    }
}
