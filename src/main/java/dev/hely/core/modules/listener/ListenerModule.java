package dev.hely.core.modules.listener;

import dev.hely.core.Oxygen;
import dev.hely.core.modules.ModuleLoader;
import dev.hely.core.profile.ProfileListener;
import dev.hely.core.profile.note.NoteListener;
import dev.hely.core.profile.permission.bukkit.BukkitPermissionListener;
import dev.hely.core.punishment.PunishmentListener;
import org.bukkit.event.Listener;

import java.util.stream.Stream;

/**
 * Created By LeandroSSJ
 * Created on 07/07/2023
 * Protect Name Oxygen
 */
public class ListenerModule implements ModuleLoader {

    private boolean enabled;
    private Oxygen plugin;

    @Override
    public void enable() {
        this.plugin = Oxygen.getInstance();

        registerListener(
                new BukkitPermissionListener(),
                new ProfileListener(),
                new PunishmentListener(),
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
        return "Listener Manager";
    }
}
