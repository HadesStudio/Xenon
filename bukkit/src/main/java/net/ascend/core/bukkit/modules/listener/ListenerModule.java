package net.ascend.core.bukkit.modules.listener;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.modules.ModuleLoader;
import net.ascend.core.bukkit.profile.ProfileListener;
import net.ascend.core.bukkit.profile.log.listeners.LogListeners;
import net.ascend.core.bukkit.profile.note.NoteListener;
import net.ascend.core.bukkit.profile.permission.bukkit.BukkitPermissionListener;
import net.ascend.core.bukkit.punishment.PunishmentListener;
import net.ascend.core.bukkit.rank.grant.listener.GrantListener;
import net.ascend.core.bukkit.util.prompt.PromptListener;
import net.ascend.core.bukkit.lib.menu.MenuListener;
import org.bukkit.event.Listener;

import java.util.stream.Stream;

/**
 * Created By LeandroSSJ
 * Created on 07/07/2023
 * Protect Name Xenon
 */
public class ListenerModule implements ModuleLoader {

    private XenonBukkit plugin;

    @Override
    public void onEnable() {
        this.plugin = XenonBukkit.getInstance();

        registerListener(
                new BukkitPermissionListener(),
                new ProfileListener(plugin),
                new PunishmentListener(),
                new MenuListener(plugin),
                //new TFAListener(),
                new PromptListener(XenonBukkit.getInstance().getPromptManager()),
                new LogListeners(),
                new GrantListener(),
                new NoteListener());
    }

    public void registerListener(Listener... listeners) {
        Stream.of(listeners).forEach(listener -> plugin.getServer().getPluginManager().registerEvents(listener, plugin));
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onReload() {

    }
}
