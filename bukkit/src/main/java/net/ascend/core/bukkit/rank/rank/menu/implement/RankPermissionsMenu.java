package net.ascend.core.bukkit.rank.rank.menu.implement;

import com.google.common.collect.Lists;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.lib.menu.button.implement.BackButton;
import net.ascend.core.bukkit.lib.menu.implement.FilterablePaginatedMenu;
import net.ascend.core.bukkit.lib.menu.implement.PageFilter;
import net.ascend.core.bukkit.rank.rank.Rank;
import net.ascend.core.bukkit.rank.rank.menu.RankListMenu;
import net.ascend.core.bukkit.rank.rank.packet.implement.RankUpdatePacket;
import net.ascend.core.bukkit.rank.rank.permission.PermissionData;
import net.ascend.core.bukkit.lib.callback.Callback;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.implement.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created By LeandroSSJ
 * Created on 08/08/2023
 * Protect Name Xenon
 */
@AllArgsConstructor
public class RankPermissionsMenu extends FilterablePaginatedMenu<PermissionData> {

    {
        setClickUpdate(true);
    }

    private final Rank rank;

    @Override
    public String getName() {
        return "Rank Permission";
    }

    @Override
    public String getTitle(Player player) {
        return rank.getName() + " - Editor Permissions";
    }

    @Override
    public Map<Integer, Button> getFilteredButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        getMenuContent().put(36, new BackButton(new RankListMenu()));

        loop:
        for (PermissionData permissionData : rank.getPermissionsData()) {
            for (PageFilter<PermissionData> filter : getFilters()) {
                if (filter.test(permissionData)) {
                    buttons.put(buttons.size(), new RankPermissionButton(rank, permissionData));
                    continue loop;
                }
            }
        }

        return buttons;
    }

    @Override
    public List<PageFilter<PermissionData>> generateFilters() {
        List<PageFilter<PermissionData>> filters = Lists.newArrayList();

        filters.add(new PageFilter<>("Filter: All Permissions", (Objects::nonNull), true));
        filters.add(new PageFilter<>("Filter: Bungee Permissions", (PermissionData::isBungeecord), false));
        filters.add(new PageFilter<>("Filter: Server Permissions", (permissionData -> !permissionData.isBungeecord()), false));

        return filters;
    }

    @AllArgsConstructor
    public static class RankPermissionButton extends Button {

        private final Rank rank;
        private final PermissionData permissionData;

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(permissionData.isGlobal() ? "ENCHANTED_BOOK" : "BOOK");
            builder.setDisplayName("&6" + permissionData.getPermission());

            builder.addLore("");
            builder.addLore("&6Bungee&7: &a" + (permissionData.isBungeecord() ? "&aYes" : "&cNo"));
            builder.addLore("&6Global&7: &a" + (permissionData.isGlobal() ? "&aYes" : "&cNo"));
            builder.addLore("&6Servers&7: &a" + (permissionData.getServers().isEmpty() ? "Global" : ""));
            for (String server : permissionData.getServers()) {
                builder.addLore("&7- &6" + server);
            }

            builder.addLore("");
            builder.addLore("&7Left-Click to save the rank.");
            builder.addLore("&7Middle-Click to remove the permission.");

            if (!permissionData.isGlobal()) {
                builder.addLore("&7Right-Click to remove a specific server.");
                builder.addLore("&7Shift-Left-Click to add a new server.");
            }

            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType == ClickType.LEFT) {
                rank.save();
            } else if (clickType == ClickType.MIDDLE) { // REMOVE THE PERMISSION
                rank.getPermissionsData().remove(permissionData);
                rank.save();

                XenonBukkit.getInstance().getRedisModule().sendPacket(new RankUpdatePacket(rank.getName()));
            } else if (clickType == ClickType.RIGHT) { // REMOVE THE SPECIFIC SERVER
                if (permissionData.isGlobal()) return;

                player.closeInventory();

                conversationString(player, "&eType the server to remove:", server -> {

                    if (permissionData.getServers().contains(server)) {
                        permissionData.getServers().remove(server);
                        rank.save();

                        XenonBukkit.getInstance().getRedisModule().sendPacket(new RankUpdatePacket(rank.getName()));

                        player.sendMessage("&aServer removed successfully.");
                        new RankPermissionsMenu(rank).openMenu(player);
                    } else {
                        player.sendMessage("&cServer not found.");
                    }
                });
            } else if (clickType == ClickType.SHIFT_LEFT) { // ADDED NEW SERVER
                if (permissionData.isGlobal()) return;

                player.closeInventory();

                conversationString(player, "&eType the server to add:", (server) -> {

                    if (permissionData.getServers().contains(server)) {
                        player.sendMessage("&cServer already added.");
                        new RankPermissionsMenu(rank).openMenu(player);
                        return;
                    }

                    permissionData.getServers().add(server);
                    rank.save();

                    XenonBukkit.getInstance().getRedisModule().sendPacket(new RankUpdatePacket(rank.getName()));

                    player.sendMessage("&aServer added successfully.");

                    new RankPermissionsMenu(rank).openMenu(player);
                });
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType == ClickType.LEFT || clickType == ClickType.MIDDLE;
        }
    }

    private static void conversationString(Player p, String prompt, Callback<String> callback) {
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
