package dev.hely.core.bungee;

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
import dev.hely.core.bungee.listener.PermissionListener;
import dev.hely.core.bungee.packet.PermissionPacketListener;
import dev.hely.core.bungee.redis.RedisBungeeModule;
import dev.hely.core.bungee.utils.BungeeConfig;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
public class OxygenBungee extends Plugin {

    @Getter private static OxygenBungee instance;

    private BungeeConfig databaseConfig;

    private RedisBungeeModule redisBungeeModule;

    private final Map<UUID, List<String>> playerPermissions = new HashMap<>();

    public static final Gson PLAIN_GSON = (new GsonBuilder()).serializeNulls().create();

    @Override
    public void onEnable() {
        instance = this;

        databaseConfig = new BungeeConfig(this, "databases" + "databases");

        redisBungeeModule = new RedisBungeeModule();

        setupPackets();
        setupListener();
    }

    @Override
    public void onDisable() {
        redisBungeeModule.disable();
    }


    public void setupPackets() {
        redisBungeeModule.registerListener(new PermissionPacketListener());

        redisBungeeModule.subscribeJedis();
    }

    public void setupListener() {
        this.getProxy().getPluginManager().registerListener(this, new PermissionListener());
    }
}
