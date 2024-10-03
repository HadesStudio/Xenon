package net.ascend.core.bukkit.util;

import net.ascend.core.bukkit.XenonBukkit;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.net.URL;
import java.util.Scanner;
import java.util.UUID;

public class NameMCUtils {
    @SneakyThrows
    public static boolean isVerified(UUID uuid) {
        return new Scanner(new URL(XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("name-mc.link") + uuid.toString()).openStream()).nextBoolean();
    }

    public static void runCommands(Player player) {
        for (String commands : XenonBukkit.getInstance().getConfiguration().getConfiguration().getStringList("name-mc.commands")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commands.replace("%player%", player.getName()));
        }
    }
}
