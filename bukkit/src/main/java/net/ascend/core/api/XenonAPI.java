package net.ascend.core.api;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.punishment.Punishment;
import net.ascend.core.bukkit.rank.grant.Grant;
import net.ascend.core.bukkit.rank.rank.Rank;

import java.util.List;
import java.util.UUID;
public class XenonAPI {
    public static XenonAPI INSTANCE;

    public XenonAPI() {
        INSTANCE = this;
    }

    private final XenonBukkit plugin = XenonBukkit.getInstance();

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
        return this.getProfile(uuid).getActiveRank();
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

    public List<Punishment> getActivePunishments(UUID uuid) {
        return this.getProfile(uuid).getPunishments();
    }

    public boolean isProfileDiscordSynced(UUID uuid) {
        Profile profile = this.getProfile(uuid);

        return profile != null && profile.isDiscordSynced();
    }

    public String getProfileDiscordUserID(UUID uuid) {
        if (this.getProfile(uuid).isDiscordSynced()) {
            return this.getProfile(uuid).getDiscordID();
        } else {
            return "Unknown, the user has not linked their discord.";
        }
    }

    public String getProfileDiscordName(UUID uuid) {
        if (this.getProfile(uuid).isDiscordSynced()) {
            return this.getProfile(uuid).getDiscordName();
        } else {
            return "Unknown, the user has not linked their discord.";
        }
    }

    public boolean isProfileOnline(UUID uuid) {
        return !this.getProfile(uuid).getCurrentServer().equalsIgnoreCase("None");
    }
}
