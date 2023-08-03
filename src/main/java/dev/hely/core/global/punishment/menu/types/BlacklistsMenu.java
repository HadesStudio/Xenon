package dev.hely.core.global.punishment.menu.types;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.punishment.menu.types
//   Date: Saturday, July 15, 2023 - 2:17 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.global.profile.Profile;
import dev.hely.core.global.punishment.packet.implement.PunishmentRemovePacket;
import dev.hely.core.global.punishment.Punishment;
import dev.hely.core.global.punishment.PunishmentType;
import dev.hely.core.utils.TimeFormatUtils;
import dev.hely.lib.CC;
import dev.hely.lib.callback.Callback;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class BlacklistsMenu extends PaginatedMenu {
    private Profile profile;

    @Override
    public String getName() {
        return OxygenBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("blacklists.title").replace("%player_name%", profile.getName());
    }

    @Override
    public String getTitle(Player player) {
        return OxygenBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("blacklists.title").replace("%player_name%", profile.getName());
    }

    @Override
    public int getSize() {
        return 9*5;
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        List<Punishment> blacklists = profile.getPunishments().stream().sorted(Comparator.comparingLong(Punishment::getAddedAt).reversed()).filter(punishment -> punishment.getType() == PunishmentType.BLACKLIST).collect(Collectors.toList());
        Map<Integer, Button> buttons = new HashMap<>();

        blacklists.forEach(module -> buttons.put(buttons.size(), new BlacklistButton(module)));

        return buttons;
    }

    @AllArgsConstructor
    private static class BlacklistButton extends Button {
        private final Punishment punishment;

        public Material getBlacklistedMaterial() {
            if (OxygenBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("blacklists.button.permanent.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("blacklists.button.permanent.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("blacklists.button.permanent.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'blacklists-menu-blacklisted-material' is null, using a default value."));
                    return Material.WOOL;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'blacklists-menu-blacklisted-material' is broken, using a default value."));
                return Material.WOOL;
            }
        }

        public Material getPardonedMaterial() {
            if (OxygenBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("blacklists.button.pardoned.material") != null) {
                if (Material.getMaterial(OxygenBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("blacklists.button.pardoned.material")) != null) {
                    return Material.valueOf(OxygenBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("blacklists.button.pardoned.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'blacklists-menu-pardoned-material' is null, using a default value."));
                    return Material.WOOL;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[OxygenGlobal] &cThe material of 'blacklists-menu-pardoned-material' is broken, using a default value."));
                return Material.WOOL;
            }
        }

        public short getData() {
            return (short) OxygenBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getInt("blacklists.button.blacklisted.data");
        }

        public short getPardonedData() {
            return (short) OxygenBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getInt("blacklists.button.pardoned.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            if (punishment.isRemoved()) { // THE BLACKLIST WAS PARDONED GG
                ItemMaker builder = ItemMaker.of(getPardonedMaterial());
                builder.setData(getPardonedData());
                builder.setDisplayName(OxygenBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("bans.button.pardoned.name").replace("%ban_id%", String.valueOf(punishment.getBan_ID())));
                for (String line : OxygenBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getStringList("bans.button.pardoned.lore")) {
                    builder.addLore(line
                            .replace("%banner%", punishment.getAddedBy())
                            .replace("%silent%", punishment.isSilent() ? "&a&nYes" : "&c&nNo")
                            .replace("%reason%", punishment.getAddedReason())
                            .replace("%added_date%", TimeFormatUtils.formatIntoCalendarString(new Date(punishment.getAddedAt())))
                            .replace("%pardoner%", punishment.getRemovedBy())
                            .replace("%pardoned_at%", TimeFormatUtils.formatIntoCalendarString(new Date(punishment.getRemovedAt())))
                            .replace("%pardoned_reason%", punishment.getRemovedReason())
                    );
                }
                return builder.build();
            } else {
                ItemMaker builder = ItemMaker.of(getBlacklistedMaterial());
                builder.setData(getData());
                builder.setDisplayName(OxygenBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("blacklists.button.blacklisted.name").replace("%ban_id%", String.valueOf(punishment.getBan_ID())));
                for (String line : OxygenBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getStringList("blacklists.button.blacklisted.lore")) {
                    builder.addLore(line
                            .replace("%banner%", punishment.getAddedBy())
                            .replace("%silent%", punishment.isSilent() ? "&a&nYes" : "&c&nNo")
                            .replace("%reason%", punishment.getAddedReason())
                            .replace("%added_date%", TimeFormatUtils.formatIntoCalendarString(new Date(punishment.getAddedAt())))
                    );
                }
                return builder.build();
            }
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (punishment.isRemoved()) { // Retarded player is trying to remove a already pardoned ban???
                player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("punishments.history-menu.pardon.ban.already-removed").replace("%remover%", punishment.getRemovedBy())));
                return;
            }
            if (!player.hasPermission("oxygen.pardon.blacklists")) {
                player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("punishments.history-menu.pardon.ban.no-permission")));
                return;
            }

            player.closeInventory();
            conversationString(player, "", (message -> {
                punishment.setRemoved(true);
                punishment.setRemovedBy(player.getName());
                punishment.setRemovedAt(System.currentTimeMillis());
                punishment.setRemovedReason(message);
                punishment.setSilent(true);
                punishment.save();
                player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("punishments.history-menu.pardon.ban.pardoned")));
                OxygenBukkit.getInstance().getRedisModule().sendPacket(new PunishmentRemovePacket(punishment.getUniqueId(), player.getName(), player.getUniqueId()));
            }));
        }
    }

    private static void conversationString(Player p, String prompt, Callback<String> callback) {
        p.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("punishments.history-menu.pardon.blacklist.enter-pardon-reason")));

        ConversationFactory factory = new ConversationFactory(OxygenBukkit.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {

            public String getPromptText(ConversationContext context) {
                return prompt;
            }


            public Prompt acceptInput(ConversationContext cc, String newName) {
                callback.callback(newName);
                return Prompt.END_OF_CONVERSATION;
            }

        }).withLocalEcho(false).withEscapeSequence("quit").withTimeout(10).thatExcludesNonPlayersWithMessage("how does console have this?? what m88888");

        Conversation con = factory.buildConversation(p);
        p.beginConversation(con);
    }
}
