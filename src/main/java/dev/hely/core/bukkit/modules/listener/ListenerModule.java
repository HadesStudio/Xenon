package dev.hely.core.bukkit.modules.listener;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.modules.ModuleLoader;
import dev.hely.core.global.profile.ProfileListener;
import dev.hely.core.global.profile.note.NoteListener;
import dev.hely.core.global.profile.permission.bukkit.BukkitPermissionListener;
import dev.hely.core.global.punishment.PunishmentListener;
import dev.hely.lib.menu.MenuListener;
import org.bukkit.event.Listener;

import java.util.stream.Stream;

/**
 * Created By LeandroSSJ
 * Created on 07/07/2023
 * Protect Name OxygenGlobal
 */
public class ListenerModule implements ModuleLoader {

    private boolean enabled;
    private OxygenBukkit plugin;

    @Override
    public void enable() {
        this.plugin = OxygenBukkit.getInstance();

        registerListener(
                new BukkitPermissionListener(),
                new ProfileListener(plugin),
                new PunishmentListener(),
                new MenuListener(plugin),
                new NoteListener());

        this.enabled = true;
    }

    public void registerListener(Listener... listeners) {
        Stream.of(listeners).forEach(listener -> plugin.getServer().getPluginManager().registerEvents(listener, plugin));
    }

    @Override
    public void disable() {
        this.enabled = false;
    }

    @Override
    public void reload() {

    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getName() {
        return "Listener";
    }
}
