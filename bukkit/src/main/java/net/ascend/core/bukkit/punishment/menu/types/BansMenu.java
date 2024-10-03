package net.ascend.core.bukkit.punishment.menu.types;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.punishment.packet.implement.PunishmentRemovePacket;
import net.ascend.core.bukkit.punishment.Punishment;
import net.ascend.core.bukkit.punishment.PunishmentType;
import net.ascend.core.bukkit.util.TimeUtil;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.lib.callback.Callback;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@AllArgsConstructor
public class BansMenu extends PaginatedMenu {

    private Profile profile;

    @Override
    public String getName() {
        return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("bans-menu.title")
                .replace("%profile_name%", profile.getName())
                .replace("%profile_color%", profile.getColor());
    }

    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("bans-menu.title")
                .replace("%profile_name%", profile.getName())
                .replace("%profile_color%", profile.getColor());
    }

    @Override
    public int getSize() {
        return 9*5;
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        List<Punishment> bans = profile.getPunishments().stream()
                .sorted(Comparator.comparingLong(Punishment::getAddedAt).reversed())
                .filter(punishment -> punishment.getType() == PunishmentType.BAN)
                .collect(Collectors.toList());

        AtomicInteger index = new AtomicInteger(1);

        for (Punishment punishment : bans) {
            buttons.put(buttons.size(), new BanButton(punishment, index.getAndIncrement()));
        }

        return buttons;
    }

    @AllArgsConstructor
    private static class BanButton extends Button {

        private final Punishment punishment;
        private final int id;

        public String getPermanentMaterial() {
            return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("bans-menu.button.permanent.material");
        }

        public String getTimeMaterial() {
            return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("bans-menu.button.time-ban.material");
        }

        public String getPardonedMaterial() {
            return XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("bans-menu.button.pardoned.material");
        }

        public short getPermanentData() {
            if (punishment.isActive()) {
                return (short) XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("bans-menu.button.permanent.data.active");
            }
            return 0;
        }

        public short getData() {
            if (punishment.isActive()) {
                return (short) XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("bans-menu.button.time-ban.data.active");
            } else if (punishment.isClear()) {
                return (short) XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("bans-menu.button.time-ban.data.expired");
            }
            return 0;
        }

        public short getPardonedData() {
            return (short) XenonBukkit.getInstance().getMenuConfig().getConfiguration().getInt("bans-menu.button.pardoned.data");
        }


        @Override
        public ItemStack getItemStack(Player player) {
            if (punishment.isPermanent() && !punishment.isRemoved()) {
                ItemMaker builder = ItemMaker.of(getPermanentMaterial());
                builder.setData(getPermanentData());
                builder.setDisplayName(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("bans-menu.button.permanent.name")
                        .replace("%ban_id%", String.valueOf(id)));

                for (String line : XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("bans-menu.button.permanent.lore")) {
                    builder.addLore(line
                            .replace("%banner%", punishment.getAddedByString())
                            .replace("%silent%", punishment.isSilent() ? "&a&nYes" : "&c&nNo")
                            .replace("%reason%", punishment.getAddedReason())
                            .replace("%added_date%", TimeUtil.formatIntoCalendarString(new Date(punishment.getAddedAt())))
                            .replace("%server%", punishment.getAddedServer())
                            .replace("%ban_id%", String.valueOf(id))
                    );
                }
                return builder.build();
            }

            if (punishment.isActive()) {
                ItemMaker builder = ItemMaker.of(getTimeMaterial());
                builder.setData(getData());
                builder.setDisplayName(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("bans-menu.button.time-ban.name")
                        .replace("%ban_id%", String.valueOf(id)));

                for (String line : XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("bans-menu.button.time-ban.lore")) {
                    builder.addLore(line
                            .replace("%banner%", punishment.getAddedByString())
                            .replace("%silent%", punishment.isSilent() ? "&a&nYes" : "&c&nNo")
                            .replace("%reason%", punishment.getAddedReason())
                            .replace("%added_date%", TimeUtil.formatIntoCalendarString(new Date(punishment.getAddedAt())))
                            .replace("%server%", punishment.getAddedServer())
                            .replace("%expires%", punishment.getRemaining())
                            .replace("%ban_id%", String.valueOf(id))
                    );
                }
                return builder.build();
            } else {
                ItemMaker builder = ItemMaker.of(getPardonedMaterial());
                builder.setData(getPardonedData());
                builder.setDisplayName(XenonBukkit.getInstance().getMenuConfig().getConfiguration().getString("bans-menu.button.pardoned.name")
                        .replace("%ban_id%", String.valueOf(id)));

                for (String line : XenonBukkit.getInstance().getMenuConfig().getConfiguration().getStringList("bans-menu.button.pardoned.lore")) {
                    builder.addLore(line
                            .replace("%banner%", punishment.getAddedByString())
                            .replace("%silent%", punishment.isSilent() ? "&a&nYes" : "&c&nNo")
                            .replace("%reason%", punishment.getAddedReason())
                            .replace("%added_date%", TimeUtil.formatIntoCalendarString(new Date(punishment.getAddedAt())))
                            .replace("%server%", punishment.getAddedServer())
                            .replace("%pardoner%", punishment.getRemovedByString())
                            .replace("%pardoned_at%", TimeUtil.formatIntoCalendarString(new Date(punishment.getRemovedAt())))
                            .replace("%pardoned_reason%", punishment.getRemovedReason())
                            .replace("%ban_id%", String.valueOf(id))
                    );
                }

                return builder.build();
            }
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (punishment.isRemoved()) {
                player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("punishments.history-menu.pardon.ban.already-removed").replace("%remover%", punishment.getRemovedByString())));
                return;
            }

            if (!player.hasPermission("xenon.punishment.unban")) {
                player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("punishments.history-menu.pardon.ban.no-permission")));
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
                player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("punishments.history-menu.pardon.ban.pardoned")));
                XenonBukkit.getInstance().getRedisModule().sendPacket(new PunishmentRemovePacket(punishment.getUniqueId(), player.getName(), player.getUniqueId()));
            }));
        }
    }

    private static void conversationString(Player p, String prompt, Callback<String> callback) {
        p.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("punishments.history-menu.pardon.ban.enter-pardon-reason")));

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
