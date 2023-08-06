package dev.hely.core.api;


import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.ranks.grants.Grant;
import dev.hely.core.bukkit.ranks.rank.Rank;

import java.util.List;
import java.util.UUID;

/**
 * Created By LeandroSSJ
 * Created on 02/08/2023
 * Protect Name OxygenGlobal
 */

public class OxygenAPI {
    public static OxygenAPI INSTANCE;

    public OxygenAPI() {
        INSTANCE = this;
    }

    private final OxygenBukkit plugin = OxygenBukkit.getInstance();

    public Profile getProfile(UUID uuid) {
        return plugin.getProfileManager().getProfile(uuid);
    }

    public String getTag(UUID uuid) {
        return this.getProfile(uuid).getTag();
    }

    public boolean isStaffChat(UUID uuid) {
        Profile profile = this.getProfile(uuid);

        return profile != null && profile.getStaffProfile().isStaffChat();
    }

    public boolean isAdminChat(UUID uuid) {
        Profile profile = this.getProfile(uuid);

        return profile != null && profile.getStaffProfile().isAdminChat();
    }

    public Rank getRank(UUID uuid) {
        if (this.getProfile(uuid) == null) return new Rank("Default");
        return this.getProfile(uuid).getActiveHighestRank();
    }

    public List<Grant> getActiveGrants(UUID uuid) {
        return this.getProfile(uuid).getActiveGrants();
    }

    public List<Grant> getAllGrants(UUID uuid) {
        return this.getProfile(uuid).getGrants();
    }

    public boolean isMuted(UUID uuid) {
        Profile profile = this.getProfile(uuid);

        return profile != null && profile.getActiveMute() != null;
    }

    public boolean isBanned(UUID uuid) {
        Profile profile = this.getProfile(uuid);

        return profile != null && profile.getActiveBan() != null;
    }

    public boolean isBlacklisted(UUID uuid) {
        Profile profile = this.getProfile(uuid);

        return profile != null && profile.getActiveBlacklist() != null;
    }
}
