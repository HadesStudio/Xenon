package dev.hely.core.punishment.menu.types;

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

import dev.hely.core.Oxygen;
import dev.hely.core.profile.Profile;
import dev.hely.core.punishment.Punishment;
import dev.hely.core.punishment.PunishmentType;
import dev.hely.core.punishment.packet.implement.PunishmentRemovePacket;
import dev.hely.core.utils.TimeFormatUtils;
import dev.hely.lib.CC;
import dev.hely.lib.callback.Callback;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class BansMenu extends PaginatedMenu {
    private Profile profile;

    @Override
    public String getName() {
        return Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("bans.title").replace("%player_name%", profile.getName());
    }

    @Override
    public String getTitle(Player player) {
        return Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("bans.title").replace("%player_name%", profile.getName());
    }

    @Override
    public int getSize() {
        return 9*5;
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        List<Punishment> bans = profile.getPunishments().stream().sorted(Comparator.comparingLong(Punishment::getAddedAt).reversed()).filter(punishment -> punishment.getType() == PunishmentType.BAN).collect(Collectors.toList());
        Map<Integer, Button> buttons = new HashMap<>();

        bans.forEach(module -> buttons.put(buttons.size(), new BanButton(module)));

        return buttons;
    }

    @AllArgsConstructor
    private static class BanButton extends Button {
        private final Punishment punishment;

        public Material getPermanentMaterial() {
            if (Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("bans.button.permanent.material") != null) {
                if (Material.getMaterial(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("bans.button.permanent.material")) != null) {
                    return Material.valueOf(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("bans.button.permanent.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'bans-menu-permanent-ban-material' is null, using a default value."));
                    return Material.WOOL;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'bans-menu-permanent-ban-material' is broken, using a default value."));
                return Material.WOOL;
            }
        }

        public Material getTimeMaterial() {
            if (Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("bans.button.time-ban.material") != null) {
                if (Material.getMaterial(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("bans.button.time-ban.material")) != null) {
                    return Material.valueOf(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("bans.button.time-ban.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'bans-menu-time-ban-material' is null, using a default value."));
                    return Material.WOOL;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'bans-menu-time-ban-material' is broken, using a default value."));
                return Material.WOOL;
            }
        }

        public Material getPardonedMaterial() {
            if (Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("bans.button.pardoned.material") != null) {
                if (Material.getMaterial(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("bans.button.pardoned.material")) != null) {
                    return Material.valueOf(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("bans.button.pardoned.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'bans-menu-pardoned-material' is null, using a default value."));
                    return Material.WOOL;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&4[Oxygen] &cThe material of 'bans-menu-pardoned-material' is broken, using a default value."));
                return Material.WOOL;
            }
        }

        public short getPermanentData() {
            if (punishment.isActive()) {
                return (short) Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getInt("bans.button.permanent.data.active");
            }
            return 0;
        }

        public short getData() {
            if (punishment.isActive()) {
                return (short) Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getInt("bans.button.time-ban.data.active");
            } else if (punishment.isClear()) {
                return (short) Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getInt("bans.button.time-ban.data.expired");
            }
            return 0;
        }

        public short getPardonedData() {
            return (short) Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getInt("bans.button.pardoned.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            if (punishment.isPermanent()) { // PERM BAN OMG!!
                ItemMaker builder = ItemMaker.of(getPermanentMaterial());
                builder.setData(getPermanentData());
                builder.setDisplayName(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("bans.button.permanent.name").replace("%ban_id%", String.valueOf(punishment.getBan_ID())));
                for (String line : Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getStringList("bans.button.permanent.lore")) {
                    builder.addLore(line
                            .replace("%banner%", punishment.getAddedBy())
                            .replace("%silent%", punishment.isSilent() ? "&a&nYes" : "&c&nNo")
                            .replace("%reason%", punishment.getAddedReason())
                            .replace("%added_date%", TimeFormatUtils.formatIntoCalendarString(new Date(punishment.getAddedAt())))
                            .replace("%server%", punishment.getAddedServer())
                    );
                }
                return builder.build();
            } else if (punishment.isRemoved()) { // THE BAN WAS PARDONED GG
                ItemMaker builder = ItemMaker.of(getPardonedMaterial());
                builder.setData(getPardonedData());
                builder.setDisplayName(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("bans.button.pardoned.name").replace("%ban_id%", String.valueOf(punishment.getBan_ID())));
                for (String line : Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getStringList("bans.button.pardoned.lore")) {
                    builder.addLore(line
                            .replace("%banner%", punishment.getAddedBy())
                            .replace("%silent%", punishment.isSilent() ? "&a&nYes" : "&c&nNo")
                            .replace("%reason%", punishment.getAddedReason())
                            .replace("%added_date%", TimeFormatUtils.formatIntoCalendarString(new Date(punishment.getAddedAt())))
                            .replace("%server%", punishment.getAddedServer())
                            .replace("%pardoner%", punishment.getRemovedBy())
                            .replace("%pardoned_at%", TimeFormatUtils.formatIntoCalendarString(new Date(punishment.getRemovedAt())))
                            .replace("%pardoned_reason%", punishment.getRemovedReason())
                    );
                }
                return builder.build();
            } else { // OTHER??
                ItemMaker builder = ItemMaker.of(getTimeMaterial());
                builder.setData(getData());
                builder.setDisplayName(Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getString("bans.button.time-ban.name").replace("%ban_id%", String.valueOf(punishment.getBan_ID())));
                for (String line : Oxygen.getInstance().getHistoryMenuConfig().getConfiguration().getStringList("bans.button.time-ban.lore")) {
                    builder.addLore(line
                            .replace("%banner%", punishment.getAddedBy())
                            .replace("%silent%", punishment.isSilent() ? "&a&nYes" : "&c&nNo")
                            .replace("%reason%", punishment.getAddedReason())
                            .replace("%added_date%", TimeFormatUtils.formatIntoCalendarString(new Date(punishment.getAddedAt())))
                            .replace("%server%", punishment.getAddedServer())
                            .replace("%expires%", punishment.getRemaining())
                    );
                }
                return builder.build();
            }
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (punishment.isRemoved()) { // Retarded player is trying to remove a already pardoned ban???
                player.sendMessage(CC.translate(Oxygen.getInstance().getLanguageConfig().getConfiguration().getString("punishments.history-menu.pardon.ban.already-removed").replace("%remover%", punishment.getRemovedBy())));
                return;
            }
            if (!player.hasPermission("oxygen.pardon.bans")) {
                player.sendMessage(CC.translate(Oxygen.getInstance().getLanguageConfig().getConfiguration().getString("punishments.history-menu.pardon.ban.no-permission")));
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
                player.sendMessage(CC.translate(Oxygen.getInstance().getLanguageConfig().getConfiguration().getString("punishments.history-menu.pardon.ban.pardoned")));
                Oxygen.getInstance().getRedisModule().sendPacket(new PunishmentRemovePacket(punishment.getUniqueId(), player.getName(), player.getUniqueId()));
            }));
        }
    }

    private static void conversationString(Player p, String prompt, Callback<String> callback) {
        p.sendMessage(CC.translate(Oxygen.getInstance().getLanguageConfig().getConfiguration().getString("punishments.history-menu.pardon.ban.enter-pardon-reason")));

        ConversationFactory factory = new ConversationFactory(Oxygen.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {

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
