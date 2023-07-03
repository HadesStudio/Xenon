package dev.hely.core;

import dev.hely.core.database.mongo.MongoManager;
import dev.hely.core.profile.ProfileManager;
import dev.hely.lib.configuration.Config;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created By LeandroSSJ
 * Created on 29/06/2023
 * Protect Name Oxygen
 */
@Getter
public class Oxygen extends JavaPlugin {

    @Getter private static Oxygen instance;

    private Config database;

    private MongoManager mongoManager;
    private ProfileManager profileManager;


    @Override
    public void onEnable() {
        instance = this;

        setupConfig();
        setupManagers();
    }

    @Override
    public void onDisable() {

    }

    public void setupConfig(){
        this.database = new Config(this, "database");
    }

    public void setupManagers(){
        mongoManager = new MongoManager(this);
        profileManager = new ProfileManager();
    }
}
