package dev.hely.core.utils;

import dev.hely.core.Oxygen;
import org.bukkit.scheduler.*;

public class Tasks {

    public static void run(Oxygen plugin, Callable callable) {
        plugin.getServer().getScheduler().runTask(plugin, callable::call);
    }

    public static void runAsync(Oxygen plugin, Callable callable) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, callable::call);
    }

    public static void runLater(Oxygen plugin, Callable callable, long delay) {
        plugin.getServer().getScheduler().runTaskLater(plugin, callable::call, delay);
    }

    public static BukkitTask runAsyncLater(Oxygen plugin, Callable callable, long delay) {
        return plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, callable::call, delay);
    }

    public static void runTimer(Oxygen plugin, Callable callable, long delay, long interval) {
        plugin.getServer().getScheduler().runTaskTimer(plugin, callable::call, delay, interval);
    }

    public static void runAsyncTimer(Oxygen plugin, Callable callable, long delay, long interval) {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, callable::call, delay, interval);
    }

    public interface Callable {

        void call();
    }
}
