package net.ascend.core.bungee;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bungee
//   Date: Thursday, August 03, 2023 - 4:51 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.ascend.core.bungee.listener.PermissionListener;
import net.ascend.core.bungee.packet.PermissionPacketListener;
import net.ascend.core.bungee.database.redis.RedisBungeeModule;
import net.ascend.core.bungee.util.CC;
import net.ascend.core.bungee.util.Config;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.*;

@Getter
public class XenonBungee extends Plugin {

    @Getter private static XenonBungee instance;
    private boolean fullyEnabled;

    private Config databaseConfig;

    private RedisBungeeModule redisBungeeModule;

    private final Map<UUID, List<String>> playerPermissions = new HashMap<>();

    public static final Gson PLAIN_GSON = (new GsonBuilder()).serializeNulls().create();

    @Override
    public void onEnable() {
        instance = this;

        this.registerConfigs();

        this.setupModules();
        this.setupPackets();
        this.setupCommands();
        this.setupListeners();

        this.fullyEnabled = true;
    }

    public void registerConfigs() {
        databaseConfig = new Config(this, "bungee-config");

        getLogger().info(CC.translate("[Xenon] Successfully loaded all configuration files."));
    }

    @Override
    public void onDisable() {
        if (!this.fullyEnabled) return;

        redisBungeeModule.disable();
    }

    public void setupModules(){
        redisBungeeModule = new RedisBungeeModule();
    }

    public void setupPackets() {
        redisBungeeModule.registerListener(new PermissionPacketListener());

        redisBungeeModule.subscribeJedis();

        getLogger().info(CC.translate("[Xenon] Successfully registered all packets."));
    }

    public void setupCommands() {
        getLogger().info(CC.translate("[Xenon] Successfully registered all commands."));
    }

    public void setupListeners() {
        this.getProxy().getPluginManager().registerListener(this, new PermissionListener());

        getLogger().info(CC.translate("[Xenon] Successfully registered all listeners."));
    }
}