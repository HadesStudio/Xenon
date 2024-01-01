package net.ascend.core.bukkit.profile.squads.menu.view;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.squads.Squad;
import net.ascend.core.bukkit.profile.squads.commands.prompts.DiscordSetupPrompt;
import net.ascend.core.bukkit.profile.squads.commands.prompts.InvitePlayerPrompt;
import net.ascend.core.bukkit.profile.squads.commands.prompts.JoinPasswordPrompt;
import net.ascend.core.bukkit.profile.squads.invites.SquadInvite;
import net.ascend.core.bukkit.util.TimeUtil;
import net.ascend.core.bukkit.util.material.MaterialUtil;
import net.ascend.lib.maker.ItemMaker;
import net.ascend.lib.menu.button.Button;
import net.ascend.lib.menu.button.implement.PageButton;
import net.ascend.lib.menu.implement.PaginatedMenu;
import net.ascend.lib.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@AllArgsConstructor
public class SquadInfoMenu extends PaginatedMenu {
    final Profile profile;
    final Squad squad;

    @Override
    public String getName() {
        return XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.info-menu.title").replace("%squad%", squad.getName()).replace("%online%", String.valueOf(squad.getOnline())).replace("%members%", String.valueOf(squad.getMembersAmount()));
    }

    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.info-menu.title").replace("%squad%", squad.getName()).replace("%online%", String.valueOf(squad.getOnline())).replace("%members%", String.valueOf(squad.getMembersAmount()));
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (UUID uuid : squad.getMembers()) {
            buttons.put(buttons.size(), new SquadMemberButton(profile, uuid, squad));
        }

        return buttons;
    }

    @Override
    public Map<Integer, Button> getGlobalContent(Player player) {
        Map<Integer, Button> menuContent = new HashMap<>();

        menuContent.put(0, Button.getPlaceholder());
        menuContent.put(1, Button.getPlaceholder());
        menuContent.put(2, Button.getPlaceholder());
        menuContent.put(3, Button.getPlaceholder());
        menuContent.put(4, Button.getPlaceholder());
        menuContent.put(5, Button.getPlaceholder());
        menuContent.put(6, Button.getPlaceholder());
        menuContent.put(7, Button.getPlaceholder());
        menuContent.put(8, Button.getPlaceholder());
        menuContent.put(9, Button.getPlaceholder());
        menuContent.put(17, Button.getPlaceholder());
        menuContent.put(18, new PageButton(-1, this));
        menuContent.put(19, Button.getPlaceholder());
        menuContent.put(20, Button.getPlaceholder());
        menuContent.put(21, new SquadDiscordButton(profile, squad));
        menuContent.put(22, new SquadInformationButton(profile, squad));
        if (profile.getSquad().equalsIgnoreCase(squad.getName())) {
            menuContent.put(23, new SquadInviteButton(profile, squad));
        } else {
            menuContent.put(23, new SquadJoinButton(profile, squad));
        }
        menuContent.put(24, Button.getPlaceholder());
        menuContent.put(25, Button.getPlaceholder());

        menuContent.put(26, new PageButton(1, this));


        return menuContent;
    }

    @AllArgsConstructor
    public static class SquadMemberButton extends Button {
        final Profile profile;
        final UUID uuid;
        final Squad squad;

        public List<String> getLore() {
            if (profile.getSquad().equalsIgnoreCase(squad.getName())) {
                return XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getStringList("menus.info-menu.buttons.member-button.lore.member");
            } else {
                return XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getStringList("menus.info-menu.buttons.member-button.lore.no-access");
            }
        }

        @Override
        public ItemStack getItemStack(Player player) {
            OfflinePlayer offlinePlayer = XenonBukkit.getInstance().getServer().getOfflinePlayer(uuid);
            ItemMaker builder = ItemMaker.of(MaterialUtil.HUMAN_SKULL.getMaterial());
            builder.setData((short) 3);
            builder.setOwner(offlinePlayer.getName());
            builder.setDisplayName(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.info-menu.buttons.member-button.display-name")
                    .replace("%member_name%", offlinePlayer.getName())
                    .replace("%rank%", squad.getMemberRankString(offlinePlayer.getUniqueId()))
            );

            for (String line : getLore()) {
                builder.addLore(line);
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.isLeftClick()) {
                Squad squad = XenonBukkit.getInstance().getSquadManager().getSquad(profile.getSquad());
                if (!squad.getLeader().equals(player.getUniqueId())) {
                    player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.member-editor.messages.not-leader")));
                    return;
                }
                Profile targetProfile = XenonBukkit.getInstance().getProfileManager().getProfile(uuid);
                new SquadMemberEditorMenu(profile, squad, targetProfile).openMenu(player);
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isLeftClick() || clickType.isRightClick();
        }
    }

    @AllArgsConstructor
    public static class SquadDiscordButton extends Button {
        final Profile profile;
        final Squad squad;

        public String getDisplayName() {
            if (profile.getSquad().equalsIgnoreCase(squad.getName())) {
                return XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.info-menu.buttons.squad-discord.display-name.in-squad");
            } else {
                return XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.info-menu.buttons.squad-discord.display-name.visitor");
            }
        }

        public List<String> getLore() {
            if (profile.getSquad().equalsIgnoreCase(squad.getName())) {
                return XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getStringList("menus.info-menu.buttons.squad-discord.lore.in-squad");
            } else {
                return XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getStringList("menus.info-menu.buttons.squad-discord.lore.visitor");
            }
        }
        
        public Material getMaterial() {
            if (XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.info-menu.buttons.squad-discord.material") != null) {
                if (Material.getMaterial(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.info-menu.buttons.squad-discord.material")) != null) {
                    return Material.valueOf(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.info-menu.buttons.squad-discord.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'squad-discord-button' is null, using a default value."));
                    return Material.PAINTING;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'squad-discord-button' is broken, using a default value."));
                return Material.PAINTING;
            }
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getInt("menus.info-menu.buttons.squad-discord.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(getDisplayName());
            for (String line : getLore()) {
                builder.addLore(line
                        .replace("%link%", squad.getDiscord())
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (profile.getSquad().equalsIgnoreCase(squad.getName())) {
                if (clickType.isRightClick()) {
                    squad.setDiscord(null);
                    player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.info-menu.buttons.squad-discord.messages.reset")));
                } else {
                    player.closeInventory();
                    new DiscordSetupPrompt(profile, squad).startPrompt(player);
                }
            } else {
                player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.no-permission")));
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isRightClick();
        }
    }

    @AllArgsConstructor
    public static class SquadInformationButton extends Button {
        final Profile profile;
        final Squad squad;

        public Material getMaterial() {
            if (XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.info-menu.buttons.squad-info.material") != null) {
                if (Material.getMaterial(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.info-menu.buttons.squad-info.material")) != null) {
                    return Material.valueOf(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.info-menu.buttons.squad-info.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'squad-info-button' is null, using a default value."));
                    return Material.NAME_TAG;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'squad-info-button' is broken, using a default value."));
                return Material.NAME_TAG;
            }
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getInt("menus.info-menu.buttons.squad-info.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.info-menu.buttons.squad-info.display-name"));
            for (String line : XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getStringList("menus.info-menu.buttons.squad-info.lore")) {
                builder.addLore(line
                        .replace("%squad_name%", squad.getName())
                        .replace("%leader%", XenonBukkit.getInstance().getProfileManager().getProfile(squad.getLeader()).getName())
                        .replace("%members%", String.valueOf(squad.getMembersAmount()))
                        .replace("%date%", TimeUtil.formatIntoCalendarString(new Date(squad.getDateCreated())))
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isRightClick();
        }
    }

    @AllArgsConstructor
    public static class SquadInviteButton extends Button {
        final Profile profile;
        final Squad squad;

        public Material getMaterial() {
            if (XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.info-menu.buttons.squad-invite.material") != null) {
                if (Material.getMaterial(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.info-menu.buttons.squad-invite.material")) != null) {
                    return Material.valueOf(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.info-menu.buttons.squad-invite.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'squad-invite-button' is null, using a default value."));
                    return MaterialUtil.BOOK_AND_QUILL.getMaterial();
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'squad-invite-button' is broken, using a default value."));
                return MaterialUtil.BOOK_AND_QUILL.getMaterial();
            }
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getInt("menus.info-menu.buttons.squad-invite.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.info-menu.buttons.squad-invite.display-name"));
            for (String line : XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getStringList("menus.info-menu.buttons.squad-invite.lore")) {
                builder.addLore(line);
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            player.closeInventory();
            new InvitePlayerPrompt(profile, squad).startPrompt(player);
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isRightClick();
        }
    }

    @AllArgsConstructor
    public static class SquadJoinButton extends Button {
        final Profile profile;
        final Squad squad;

        public Material getMaterial() {
            if (XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.info-menu.buttons.squad-join.material") != null) {
                if (Material.getMaterial(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.info-menu.buttons.squad-join.material")) != null) {
                    return Material.valueOf(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.info-menu.buttons.squad-join.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'squad-join-button' is null, using a default value."));
                    return MaterialUtil.BOOK_AND_QUILL.getMaterial();
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'squad-join-button' is broken, using a default value."));
                return MaterialUtil.BOOK_AND_QUILL.getMaterial();
            }
        }

        public short getData() {
            return (short) XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getInt("menus.info-menu.buttons.squad-join.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            SquadInvite squadInvite = getInvite(player, squad.getName());

            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("menus.info-menu.buttons.squad-join.display-name"));
            for (String line : XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getStringList("menus.info-menu.buttons.squad-join.lore")) {
                builder.addLore(line
                        .replace("%invited%", squadInvite == null ? CC.translate("&aYes") : CC.translate("&cNo"))
                        .replace("%password%", squad.getPassword() != null ? CC.translate("&aYes") : CC.translate("&cNo"))
                        .replace("%open%", squad.isOpen() ? CC.translate("&aYes") : CC.translate("&cNo"))
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            SquadInvite squadInvite = getInvite(player, squad.getName());

            if (profile.getSquad() != null) {
                player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.already-in-squad")));
                return;
            }
            if (squadInvite == null && (getSquad(profile.getSquad()) == null || getSquad(profile.getSquad()).getPassword() == null)) {
                player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.join.not-invited")));
                return;
            }
            if (squadInvite == null && getSquad(profile.getSquad()).getPassword() != null) {
                player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.join.password")));
                return;
            }
            if (squad.getPassword() != null && squadInvite == null) {
                player.closeInventory();
                new JoinPasswordPrompt(profile, squad, squadInvite).startPrompt(player);
            }

            profile.setSquad(squad.getName());
            squad.getMembers().add(player.getUniqueId());

            if (squadInvite != null) {
                profile.getSquadInvites().remove(squadInvite);
            }

            squad.broadcast(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.join.broadcast").replace("%player%", player.getName())));
            player.sendMessage(CC.translate(XenonBukkit.getInstance().getSquadMenuConfig().getConfiguration().getString("commands.join.joined")));

            XenonBukkit.getInstance().getTaskUtil().runAsync(XenonBukkit.getInstance().getSquadManager()::saveAll);
            profile.save();
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isRightClick();
        }

        private SquadInvite getInvite(Player player, String name) {
            Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

            return profile.getSquadInvites().stream().filter(ci -> ci.getSquad().equalsIgnoreCase(name) && System.currentTimeMillis() - ci.getTimestamp() <= 60000 && !ci.isCancelled()).findFirst().orElse(null);
        }

        private Squad getSquad(String name) {
            return XenonBukkit.getInstance().getSquadManager().getSquads().stream().filter(c -> c.getName().equalsIgnoreCase(name) || c.getMembers().contains(XenonBukkit.getInstance().getServer().getOfflinePlayer(name).getUniqueId())).findFirst().orElse(null);
        }
    }
}
