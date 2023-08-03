package dev.hely.core;


import dev.hely.core.profile.Profile;
import dev.hely.core.ranks.grants.Grant;
import dev.hely.core.ranks.rank.Rank;

import java.util.List;
import java.util.UUID;

/**
 * Created By LeandroSSJ
 * Created on 02/08/2023
 * Protect Name Oxygen
 */

public class OxygenAPI {

    private final Oxygen plugin = Oxygen.getInstance();

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
