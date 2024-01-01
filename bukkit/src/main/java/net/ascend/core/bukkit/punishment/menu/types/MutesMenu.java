package net.ascend.core.bukkit.punishment.menu.types;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.punishment.menu.types
//   Date: Saturday, July 15, 2023 - 2:18 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.punishment.packet.implement.PunishmentRemovePacket;
import net.ascend.core.bukkit.punishment.Punishment;
import net.ascend.core.bukkit.punishment.PunishmentType;
import net.ascend.core.bukkit.util.TimeUtil;
import net.ascend.core.bukkit.util.material.MaterialUtil;
import net.ascend.lib.CC;
import net.ascend.lib.callback.Callback;
import net.ascend.lib.maker.ItemMaker;
import net.ascend.lib.menu.button.Button;
import net.ascend.lib.menu.implement.PaginatedMenu;
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
public class MutesMenu extends PaginatedMenu {
    private Profile profile;

    @Override
    public String getName() {
        return XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("mutes.title").replace("%player_name%", profile.getName());
    }

    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("mutes.title").replace("%player_name%", profile.getName());
    }

    @Override
    public int getSize() {
        return 9*5;
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        List<Punishment> mutes = profile.getPunishments().stream().sorted(Comparator.comparingLong(Punishment::getAddedAt).reversed()).filter(punishment -> punishment.getType() == PunishmentType.MUTE).collect(Collectors.toList());
        Map<Integer, Button> buttons = new HashMap<>();

        mutes.forEach(module -> buttons.put(buttons.size(), new MutesButton(module)));

        return buttons;
    }

    @AllArgsConstructor
    private static class MutesButton extends Button {
        private final Punishment punishment;

        public Material getPermanentMaterial() {
            if (XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("mutes.button.permanent.material") != null) {
                if (Material.getMaterial(XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("mutes.button.permanent.material")) != null) {
                    return Material.valueOf(XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("mutes.button.permanent.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'mutes-menu-permanent-mutes-material' is null, using a default value."));
                    return MaterialUtil.WOOL.getMaterial();
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'mutes-menu-permanent-mutes-material' is broken, using a default value."));
                return MaterialUtil.WOOL.getMaterial();
            }
        }

        public Material getTimeMaterial() {
            if (XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("mutes.button.time-mute.material") != null) {
                if (Material.getMaterial(XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("mutes.button.time-mute.material")) != null) {
                    return Material.valueOf(XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("mutes.button.time-mute.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'mutes-menu-time-mute-material' is null, using a default value."));
                    return MaterialUtil.WOOL.getMaterial();
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'mutes-menu-time-mute-material' is broken, using a default value."));
                return MaterialUtil.WOOL.getMaterial();
            }
        }

        public Material getPardonedMaterial() {
            if (XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("mutes.button.pardoned.material") != null) {
                if (Material.getMaterial(XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("mutes.button.pardoned.material")) != null) {
                    return Material.valueOf(XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("mutes.button.pardoned.material"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'mutes-menu-pardoned-material' is null, using a default value."));
                    return MaterialUtil.WOOL.getMaterial();
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Xenon] The material of 'mutes-menu-pardoned-material' is broken, using a default value."));
                return MaterialUtil.WOOL.getMaterial();
            }
        }

        public short getPermanentData() {
            if (punishment.isActive()) {
                return (short) XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getInt("mutes.button.permanent.data.active");
            }
            return 0;
        }

        public short getData() {
            if (punishment.isActive()) {
                return (short) XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getInt("mutes.button.time-mute.data.active");
            } else if (punishment.isClear()) {
                return (short) XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getInt("mutes.button.time-mute.data.expired");
            }
            return 0;
        }

        public short getPardonedData() {
            return (short) XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getInt("mutes.button.pardoned.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            if (punishment.isPermanent() && !punishment.isRemoved()) { // PERM MUTE OMG!!
                ItemMaker builder = ItemMaker.of(getPermanentMaterial());
                builder.setData(getPermanentData());
                builder.setDisplayName(XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("mutes.button.permanent.name"));
                for (String line : XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getStringList("mutes.button.permanent.lore")) {
                    builder.addLore(line
                            .replace("%muter%", punishment.getAddedByString())
                            .replace("%silent%", punishment.isSilent() ? "&a&nYes" : "&c&nNo")
                            .replace("%reason%", punishment.getAddedReason())
                            .replace("%server%", punishment.getAddedServer())
                            .replace("%added_date%", TimeUtil.formatIntoCalendarString(new Date(punishment.getAddedAt())))
                    );
                }
                return builder.build();
            }

            if (punishment.isActive()) {
                ItemMaker builder = ItemMaker.of(getTimeMaterial());
                builder.setData(getData());
                builder.setDisplayName(XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("mutes.button.time-mute.name"));
                for (String line : XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getStringList("mutes.button.time-mute.lore")) {
                    builder.addLore(line
                            .replace("%muter%", punishment.getAddedByString())
                            .replace("%silent%", punishment.isSilent() ? "&a&nYes" : "&c&nNo")
                            .replace("%reason%", punishment.getAddedReason())
                            .replace("%server%", punishment.getAddedServer())
                            .replace("%added_date%", TimeUtil.formatIntoCalendarString(new Date(punishment.getAddedAt())))
                            .replace("%expires%", punishment.getRemaining())
                    );
                }

                return builder.build();
            } else { // THE MUTE WAS PARDONED GG
                ItemMaker builder = ItemMaker.of(getPardonedMaterial());
                builder.setData(getPardonedData());
                builder.setDisplayName(XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getString("mutes.button.pardoned.name"));
                for (String line : XenonBukkit.getInstance().getHistoryMenuConfig().getConfiguration().getStringList("mutes.button.pardoned.lore")) {
                    builder.addLore(line
                            .replace("%muter%", punishment.getAddedByString())
                            .replace("%silent%", punishment.isSilent() ? "&a&nYes" : "&c&nNo")
                            .replace("%reason%", punishment.getAddedReason())
                            .replace("%server%", punishment.getAddedServer())
                            .replace("%added_date%", TimeUtil.formatIntoCalendarString(new Date(punishment.getAddedAt())))
                            .replace("%pardoner%", punishment.getRemovedByString())
                            .replace("%pardoned_at%", TimeUtil.formatIntoCalendarString(new Date(punishment.getRemovedAt())))
                            .replace("%pardoned_reason%", punishment.getRemovedReason())
                    );
                }
                return builder.build();
            }
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (punishment.isRemoved()) { // Retarded player is trying to remove a already pardoned mute???
                player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("punishments.history-menu.pardon.mute.already-removed").replace("%remover%", punishment.getRemovedByString())));
                return;
            }
            if (!player.hasPermission("xenon.pardon.mutes")) {
                player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("punishments.history-menu.pardon.mute.no-permission")));
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
                player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("punishments.history-menu.pardon.mute.pardoned")));
                XenonBukkit.getInstance().getRedisModule().sendPacket(new PunishmentRemovePacket(punishment.getUniqueId(), player.getName(), player.getUniqueId()));
            }));
        }
    }

    private static void conversationString(Player p, String prompt, Callback<String> callback) {
        p.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("punishments.history-menu.pardon.blacklist.enter-pardon-reason")));

        ConversationFactory factory = new ConversationFactory(XenonBukkit.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {

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
