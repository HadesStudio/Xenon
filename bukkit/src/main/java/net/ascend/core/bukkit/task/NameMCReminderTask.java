package net.ascend.core.bukkit.task;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.util.NameMCUtils;
import net.ascend.core.bukkit.lib.CC;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class NameMCReminderTask extends BukkitRunnable {

    @Override
    public void run() {
        for (Player onlinePlayer : XenonBukkit.getInstance().getServer().getOnlinePlayers()) {
            if (!NameMCUtils.isVerified(onlinePlayer.getUniqueId())) {
                for (String message : XenonBukkit.getInstance().getConfiguration().getConfiguration().getStringList("name-mc.messages.reminder")) {
                    onlinePlayer.sendMessage(CC.translate(message).replace("%player%", onlinePlayer.getName()));
                }
            }
        }
    }
}
