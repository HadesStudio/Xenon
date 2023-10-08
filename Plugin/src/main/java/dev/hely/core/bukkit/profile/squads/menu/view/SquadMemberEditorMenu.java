package dev.hely.core.bukkit.profile.squads.menu.view;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.profile.squads.Squad;
import dev.hely.core.bukkit.profile.squads.members.SquadMemberType;
import dev.hely.core.bukkit.utils.material.MaterialUtil;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.Menu;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.button.implement.BackButton;
import dev.hely.lib.menu.fill.FillerTemplate;
import dev.hely.lib.util.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class SquadMemberEditorMenu extends Menu {
    final Profile profile;
    final Squad squad;
    final Profile targetProfile;

    @Override
    public FillerTemplate getFillerTemplate() {
        return FillerTemplate.FILL;
    }

    @Override
    public String getName() {
        return OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.member-editor.title").replace("%member%", targetProfile.getName()).replace("%role%", squad.getMemberRankString(targetProfile.getUniqueId()));
    }

    @Override
    public String getTitle(Player player) {
        return OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.member-editor.title").replace("%member%", targetProfile.getName()).replace("%role%", squad.getMemberRankString(targetProfile.getUniqueId()));
    }

    @Override
    public int getSize() {
        return OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getInt("menus.member-editor.size") * 9;
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getInt("menus.member-editor.buttons.back-button.slot"), new BackButton(new SquadInfoMenu(profile, squad)));
        buttons.put(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getInt("menus.member-editor.buttons.kick-member.slot"), new KickMemberButton(profile, squad, targetProfile));
        buttons.put(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getInt("menus.member-editor.buttons.promote-member.slot"), new PromoteMemberButton(profile, squad, targetProfile));
        buttons.put(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getInt("menus.member-editor.buttons.demote-member.slot"), new DemoteMemberButton(profile, squad, targetProfile));

        return buttons;
    }

    @AllArgsConstructor
    public static class KickMemberButton extends Button {
        final Profile profile;
        final Squad squad;
        final Profile targetProfile;

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.member-editor.buttons.kick-member.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.member-editor.buttons.kick-member.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.member-editor.buttons.kick-member.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'kick-member-button' is null, using a default value."));
                    return MaterialUtil.IRON_FENCE.getMaterial();
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'kick-member-button' is broken, using a default value."));
                return MaterialUtil.IRON_FENCE.getMaterial();
            }
        }

        public List<String> getLore() {
            return OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getStringList("menus.member-editor.buttons.kick-member.lore");
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setDisplayName(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.member-editor.buttons.kick-member.display-name"));
            for (String line : getLore()) {
                builder.addLore(line);
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            Squad squad = OxygenBukkit.getInstance().getSquadManager().getSquad(targetProfile.getSquad());
            if (!squad.getCaptains().contains(player.getUniqueId()) && !squad.getLeader().equals(player.getUniqueId())) {
                player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.kick.not-captain")));
                return;
            }
            if (targetProfile.getName().equalsIgnoreCase(profile.getName())) {
                player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.kick.kick-self")));
                return;
            }
            if (!squad.getMembers().contains(targetProfile.getUniqueId())) {
                player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.kick.not-in")));
                return;
            }
            if ((squad.getCaptains().contains(targetProfile.getUniqueId()) && !squad.getLeader().equals(player.getUniqueId())) || squad.getLeader() == targetProfile.getUniqueId()) {
                player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.kick.kick-members")));
                return;
            }
            squad.getMembers().remove(targetProfile.getUniqueId());
            squad.getCaptains().remove(targetProfile.getUniqueId());

            if (Bukkit.getPlayer(targetProfile.getUniqueId()) != null && Bukkit.getPlayer(targetProfile.getUniqueId()).isOnline()) {
                Player target = Bukkit.getPlayer(targetProfile.getUniqueId());

                Profile targetProfile = OxygenBukkit.getInstance().getProfileManager().getProfile(target.getUniqueId());
                targetProfile.setSquad(null);

                target.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.kick.kicked")));
            }

            squad.broadcast(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.kick.broadcast").replace("%player%", targetProfile.getName())));
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isLeftClick() || clickType.isRightClick();
        }
    }

    @AllArgsConstructor
    public static class PromoteMemberButton extends Button {
        final Profile profile;
        final Squad squad;
        final Profile targetProfile;

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.member-editor.buttons.promote-member.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.member-editor.buttons.promote-member.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.member-editor.buttons.promote-member.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'promote-member-button' is null, using a default value."));
                    return Material.NETHER_STAR;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'promote-member-button' is broken, using a default value."));
                return Material.NETHER_STAR;
            }
        }

        public List<String> getLore() {
            return OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getStringList("menus.member-editor.buttons.promote-member.lore");
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setDisplayName(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.member-editor.buttons.promote-member.display-name").replace("%new_rank%", getNewRole()));
            for (String line : getLore()) {
                builder.addLore(line);
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            Squad squad = OxygenBukkit.getInstance().getSquadManager().getSquad(targetProfile.getSquad());
            if (!squad.getLeader().equals(player.getUniqueId())) {
                player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.promote.not-leader")));
                return;
            }
            OfflinePlayer offlinePlayer = OxygenBukkit.getInstance().getServer().getOfflinePlayer(targetProfile.getUniqueId());
            if (offlinePlayer.getPlayer() != null && offlinePlayer.getPlayer() == player) {
                player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.promote.promote-self")));
                return;
            }
            if (!squad.getMembers().contains(offlinePlayer.getUniqueId())) {
                player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.promote.not-in")));
                return;
            }

            if (squad.getCaptains().contains(offlinePlayer.getUniqueId())) {
                player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.promote.already-captain")));
                return;
            }

            squad.getCaptains().add(offlinePlayer.getUniqueId());
            squad.broadcast(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.promote.broadcast").replace("%player%", offlinePlayer.getName())));
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.promote.promote")));
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isLeftClick() || clickType.isRightClick();
        }

        public String getNewRole() {
            if (squad.getMemberRank(targetProfile.getUniqueId()) == SquadMemberType.MEMBER) {
                return "Captain";
            } else if (squad.getMemberRank(targetProfile.getUniqueId()) == SquadMemberType.CAPTAIN) {
                return "Leader";
            }
            return "Unknown";
        }
    }

    @AllArgsConstructor
    public static class DemoteMemberButton extends Button {
        final Profile profile;
        final Squad squad;
        final Profile targetProfile;

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.member-editor.buttons.demote-member.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.member-editor.buttons.demote-member.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.member-editor.buttons.demote-member.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'demote-member-button' is null, using a default value."));
                    return Material.NETHER_STAR;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'demote-member-button' is broken, using a default value."));
                return Material.NETHER_STAR;
            }
        }

        public List<String> getLore() {
            return OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getStringList("menus.member-editor.buttons.demote-member.lore");
        }

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setDisplayName(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.member-editor.buttons.demote-member.display-name").replace("%new_rank%", getNewRole()));
            for (String line : getLore()) {
                builder.addLore(line);
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            Squad squad = OxygenBukkit.getInstance().getSquadManager().getSquad(targetProfile.getSquad());
            if (!squad.getLeader().equals(player.getUniqueId())) {
                player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.demote.not-leader")));
                return;
            }
            OfflinePlayer offlinePlayer = OxygenBukkit.getInstance().getServer().getOfflinePlayer(targetProfile.getUniqueId());
            if (offlinePlayer.getPlayer() != null && offlinePlayer.getPlayer() == player) {
                player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.demote.demote-self")));
                return;
            }
            if (!squad.getMembers().contains(offlinePlayer.getUniqueId())) {
                player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.demote.not-in")));
                return;
            }

            if (!squad.getCaptains().contains(offlinePlayer.getUniqueId())) {
                player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.demote.not-captain")));
                return;
            }

            squad.getCaptains().remove(offlinePlayer.getUniqueId());
            squad.broadcast(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.promote.broadcast").replace("%player%", offlinePlayer.getName())));
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.promote.promote")));
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isLeftClick() || clickType.isRightClick();
        }

        public String getNewRole() {
            if (squad.getMemberRank(targetProfile.getUniqueId()) == SquadMemberType.LEADER) {
                return "Captain";
            } else if (squad.getMemberRank(targetProfile.getUniqueId()) == SquadMemberType.CAPTAIN) {
                return "Member";
            }
            return "Unknown";
        }
    }
}
