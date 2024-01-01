package net.ascend.core.bukkit.vault.implement;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.ProfileManager;
import net.ascend.core.bukkit.rank.rank.Rank;
import net.ascend.core.bukkit.rank.rank.RankManager;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

/**
 * Created By LeandroSSJ
 * Created on 22/09/2023
 * Protect Name Oxygen
 */
public class VauntChatProvider extends Chat {

    private final XenonBukkit plugin;

    private final ProfileManager profileManager;
    private final RankManager rankManager;

    public VauntChatProvider(Permission perms, XenonBukkit plugin) {
        super(perms);

        this.plugin = plugin;
        this.profileManager = plugin.getProfileManager();
        this.rankManager =  plugin.getRankManager();
    }

    @Override
    public String getName() {
        return "Xenon-Chat";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPlayerPrefix(String world, String player) {
        Profile profile = profileManager.getProfile(player);
        if (profile != null) {
            return profile.getActiveRank().getPrefix();
        }

        return "";
    }

    @Override
    public void setPlayerPrefix(String world, String player, String prefix) {
    }

    @Override
    public String getPlayerSuffix(String world, String player) {
        Profile profile = profileManager.getProfile(player);
        if (profile != null) {
            return profile.getActiveRank().getSuffix();
        }

        return "";
    }

    @Override
    public void setPlayerSuffix(String world, String player, String suffix) {
    }

    @Override
    public String getGroupPrefix(String world, String group) {
        Rank rank = rankManager.getRank(group);
        if(rank != null) {
            return rank.getPrefix();
        }

        return null;
    }

    @Override
    public void setGroupPrefix(String world, String group, String prefix) {
        Rank rank = rankManager.getRank(group);
        if(rank != null) {
            rank.setPrefix(prefix);
            rank.save();
        }
    }

    @Override
    public String getGroupSuffix(String world, String group) {
        Rank rank = rankManager.getRank(group);
        if(rank != null) {
            return rank.getSuffix();
        }

        return null;
    }

    @Override
    public void setGroupSuffix(String world, String group, String suffix) {
        Rank rank = rankManager.getRank(group);
        if(rank != null) {
            rank.setSuffix(suffix);
            rank.save();
        }
    }

    @Override
    public int getPlayerInfoInteger(String s, String s1, String s2, int i) {
        return 0;
    }

    @Override
    public void setPlayerInfoInteger(String s, String s1, String s2, int i) {

    }

    @Override
    public int getGroupInfoInteger(String s, String s1, String s2, int i) {
        return 0;
    }

    @Override
    public void setGroupInfoInteger(String s, String s1, String s2, int i) {

    }

    @Override
    public double getPlayerInfoDouble(String s, String s1, String s2, double v) {
        return 0;
    }

    @Override
    public void setPlayerInfoDouble(String s, String s1, String s2, double v) {

    }

    @Override
    public double getGroupInfoDouble(String s, String s1, String s2, double v) {
        return 0;
    }

    @Override
    public void setGroupInfoDouble(String s, String s1, String s2, double v) {

    }

    @Override
    public boolean getPlayerInfoBoolean(String s, String s1, String s2, boolean b) {
        return false;
    }

    @Override
    public void setPlayerInfoBoolean(String s, String s1, String s2, boolean b) {

    }

    @Override
    public boolean getGroupInfoBoolean(String s, String s1, String s2, boolean b) {
        return false;
    }

    @Override
    public void setGroupInfoBoolean(String s, String s1, String s2, boolean b) {

    }

    @Override
    public String getPlayerInfoString(String s, String s1, String s2, String s3) {
        return null;
    }

    @Override
    public void setPlayerInfoString(String s, String s1, String s2, String s3) {

    }

    @Override
    public String getGroupInfoString(String s, String s1, String s2, String s3) {
        return null;
    }

    @Override
    public void setGroupInfoString(String s, String s1, String s2, String s3) {

    }
}
