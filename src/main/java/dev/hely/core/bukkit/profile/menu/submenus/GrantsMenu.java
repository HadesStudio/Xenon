package dev.hely.core.bukkit.profile.menu.submenus;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.profile.menu.submenus
//   Date: Tuesday, August 01, 2023 - 4:07 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.profile.menu.EditProfileMenu;
import dev.hely.core.bukkit.ranks.events.GrantGivenEvent;
import dev.hely.core.bukkit.ranks.grants.Grant;
import dev.hely.core.bukkit.utils.TimeUtil;
import dev.hely.core.bukkit.utils.DurationWrapper;
import dev.hely.lib.util.CC;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.button.implement.BackButton;
import dev.hely.lib.menu.button.implement.PageButton;
import dev.hely.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class GrantsMenu extends PaginatedMenu {

    {
        setAutoUpdate(true);
    }

    final Profile profile;
    final boolean grantsMenu;

    public final Comparator<Grant> GRANT_COMPARATOR = Comparator.comparingLong(Grant::getAddedTime).reversed();

    @Override
    public String getName() {
        return OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.title").replace("%player%", profile.getName());
    }

    @Override
    public String getTitle(Player player) {
        return OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.title").replace("%player%", profile.getName());
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        this.profile.getGrants().stream().sorted(GRANT_COMPARATOR).forEach(grant -> {
            buttons.put(buttons.size(), new Grants_Button(profile, grant));
        }); // CHECK ALL GRANTS


        /* CHECK ONLY GRANTS ACTIVES
        this.profile.getGrants().stream().sorted(GRANT_COMPARATOR).filter(grant -> !grant.isActive()).forEach(grant -> {
            buttons.put(buttons.size(), new Grants_Button(profile, grant));
        });
         */

        return buttons;
    }

    @Override
    public Map<Integer, Button> getGlobalContent(Player player) {
        Map<Integer, Button> menuContent = new HashMap<>();

        if (grantsMenu) {
            menuContent.put(0, new BackButton(new EditProfileMenu(profile)));
        } else {
            menuContent.put(0, Button.getPlaceholder());
        }

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
        menuContent.put(21, Button.getPlaceholder());
        menuContent.put(22, new Grant_Add_Button(profile));
        menuContent.put(23, Button.getPlaceholder());
        menuContent.put(24, Button.getPlaceholder());
        menuContent.put(25, Button.getPlaceholder());

        menuContent.put(26, new PageButton(1, this));


        return menuContent;
    }

    @AllArgsConstructor
    public static class Grants_Button extends Button {
        final Profile profile;
        final Grant grant;

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.buttons.grants-button.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.buttons.grants-button.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.buttons.grants-button.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'grants-button' is null, using a default value."));
                    return Material.PAPER;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'grants-button' is broken, using a default value."));
                return Material.PAPER;
            }
        }

        public short getData() {
            return (short) OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getInt("grants-menu.buttons.grants-button.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.buttons.grants-button.display-name").replace("%rank_display%", grant.getRankName()));
            for (String line : OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getStringList("grants-menu.buttons.grants-button.lore")) {
                builder.addLore(line
                        .replace("%rank_name%", grant.getRankName())
                        .replace("%rank_duration%", grant.getRemaining())
                        .replace("%reason%", grant.getReason())
                        .replace("%staff_member%", grant.getAdder())
                        .replace("%added_date%", TimeUtil.formatDateDiff(grant.getAddedTime()))
                        .replace("%creation_server%", grant.getServer())
                );
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.isLeftClick()) {
                if (player.hasPermission("oxygen.staff.grants.remove")) {
                    //profile.getGrants().remove(grant);

                    grant.setActive(false);
                    grant.setRemovedTime(System.currentTimeMillis());
                    grant.setRemover(player.getName());

                    profile.save();
                    player.sendMessage(CC.translate(OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.messages.removed-grant").replace("%player%", profile.getName())));
                } else {
                    player.sendMessage(CC.translate(OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.messages.cannot-delete")));
                }
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isLeftClick() || clickType.isRightClick();
        }
    }

    @AllArgsConstructor
    public class Grant_Add_Button extends Button {
        final Profile profile;

        public Material getMaterial() {
            if (OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.buttons.create-button.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.buttons.create-button.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.buttons.create-button.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'create-button' is null, using a default value."));
                    return Material.PAPER;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'create-button' is broken, using a default value."));
                return Material.PAPER;
            }
        }

        public short getData() {
            return (short) OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getInt("grants-menu.buttons.create-button.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(getMaterial());
            builder.setData(getData());
            builder.setDisplayName(OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.buttons.create-button.display-name"));
            for (String line : OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getStringList("grants-menu.buttons.create-button.lore")) {
                builder.addLore(line);
            }
            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType.isLeftClick()) {
                if (player.hasPermission("oxygen.staff.grants.add")) {
                    player.closeInventory();
                    ConversationFactory factory = new ConversationFactory(OxygenBukkit.getInstance()).withFirstPrompt(new rankNamePromt(profile)).withLocalEcho(false);

                    player.beginConversation(factory.buildConversation(player));
                } else {
                    player.sendMessage(CC.translate(OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.messages.cannot-add")));
                }
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType.isCreativeAction() || clickType.isRightClick();
        }
    }

    @AllArgsConstructor
    public class rankNamePromt extends StringPrompt {
        @Getter private Profile profile;

        @Override
        public String getPromptText(ConversationContext conversationContext) {
            return CC.translate(OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.messages.promt-1"));
        }

        @Override
        public Prompt acceptInput(ConversationContext conversationContext, String input) {
            Player sender = (Player) conversationContext.getForWhom();

            if (OxygenBukkit.getInstance().getRankManager().getRank(input) == null) {
                sender.playSound(sender.getLocation(), Sound.BLAZE_HIT, 5, 5);
                conversationContext.getForWhom().sendRawMessage(CC.translate(OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.messages.invalid-rank")));
                new GrantsMenu(profile, grantsMenu).openMenu(sender);
                return Prompt.END_OF_CONVERSATION;
            }

            for (Grant grants : profile.getActiveGrants()) {
                if (grants.getRankName().equalsIgnoreCase(input)) {
                    sender.playSound(sender.getLocation(), Sound.BLAZE_HIT, 5, 5);
                    conversationContext.getForWhom().sendRawMessage(CC.translate(OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.messages.already-has")));
                    new GrantsMenu(profile, grantsMenu).openMenu(sender);
                    return Prompt.END_OF_CONVERSATION;
                }
            }

            if (input.equalsIgnoreCase("cancel")) {
                sender.playSound(sender.getLocation(), Sound.BLAZE_HIT, 5, 5);
                conversationContext.getForWhom().sendRawMessage(CC.translate(OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.messages.canceled-promt")));
                new GrantsMenu(profile, grantsMenu).openMenu(sender);
                return END_OF_CONVERSATION;
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    sender.playSound(sender.getLocation(), Sound.ORB_PICKUP, 5, 5);
                    promptTime(sender, profile, input);
                }
            }.runTask(OxygenBukkit.getInstance());

            return Prompt.END_OF_CONVERSATION;
        }
    }

    @AllArgsConstructor
    public class durationPromt extends StringPrompt {
        @Getter private Profile profile;
        @Getter private String rank;

        @Override
        public String getPromptText(ConversationContext conversationContext) {
            return CC.translate(OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.messages.promt-2"));
        }

        @Override
        public Prompt acceptInput(ConversationContext conversationContext, String input) {
            Player sender = (Player) conversationContext.getForWhom();

            if (input.equalsIgnoreCase("cancel")) {
                sender.playSound(sender.getLocation(), Sound.BLAZE_HIT, 5, 5);
                conversationContext.getForWhom().sendRawMessage(CC.translate(OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.messages.canceled-promt")));
                new GrantsMenu(profile, grantsMenu).openMenu(sender);
                return END_OF_CONVERSATION;
            }

            DurationWrapper durationWrapper = DurationWrapper.findDurationWrapper(input);

            if (durationWrapper.isPermanent() && !input.toLowerCase().startsWith("permanent")) {
                sender.playSound(sender.getLocation(), Sound.BLAZE_HIT, 5, 5);
                conversationContext.getForWhom().sendRawMessage(CC.translate(OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.messages.canceled-promt")));
                new GrantsMenu(profile, grantsMenu).openMenu(sender);
                return END_OF_CONVERSATION;
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    promtReason(sender, profile, rank, durationWrapper);
                    sender.playSound(sender.getLocation(), Sound.ORB_PICKUP, 5, 5);
                }
            }.runTask(OxygenBukkit.getInstance());

            return Prompt.END_OF_CONVERSATION;
        }
    }

    public void promptTime(Player player, Profile profile, String rank) {
        final ConversationFactory factory = new ConversationFactory(OxygenBukkit.getInstance()).withFirstPrompt(new durationPromt(profile, rank)).withLocalEcho(false);

        player.beginConversation(factory.buildConversation(player));
    }

    public void promtReason(Player player, Profile profile, String rank, DurationWrapper durationWrapper) {
        final ConversationFactory factory = new ConversationFactory(OxygenBukkit.getInstance()).withFirstPrompt(new reasonPromt(profile, rank, durationWrapper)).withLocalEcho(false);

        player.beginConversation(factory.buildConversation(player));
    }

    @AllArgsConstructor
    public class reasonPromt extends StringPrompt {
        @Getter private Profile target;
        @Getter private String rank;
        @Getter private DurationWrapper durationWrapper;

        @Override
        public String getPromptText(ConversationContext conversationContext) {
            return CC.translate(OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.messages.promt-3"));
        }

        @Override
        public Prompt acceptInput(ConversationContext conversationContext, String input) {
            Player sender = (Player) conversationContext.getForWhom();

            if (input.equalsIgnoreCase("cancel")) {
                sender.playSound(sender.getLocation(), Sound.BLAZE_HIT, 5, 5);
                conversationContext.getForWhom().sendRawMessage(CC.translate(OxygenBukkit.getInstance().getEditProfileMenuConfig().getConfiguration().getString("grants-menu.messages.canceled-promt")));
                new GrantsMenu(profile, grantsMenu).openMenu(sender);
                return END_OF_CONVERSATION;
            }

            Grant grant = new Grant();
            grant.setActive(true);
            grant.setAdder(sender.getName());
            grant.setDuration(durationWrapper.getDuration());
            grant.setRankName(rank);
            grant.setAddedTime(System.currentTimeMillis());
            grant.setReason(input);
            new GrantGivenEvent(grant, profile, sender);


            profile.getGrants().add(grant);
            profile.save();

            new GrantsMenu(profile, grantsMenu).openMenu(sender);
            sender.playSound(sender.getLocation(), Sound.ORB_PICKUP, 5, 5);

            return Prompt.END_OF_CONVERSATION;
        }
    }
}
