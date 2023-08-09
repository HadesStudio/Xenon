package dev.hely.core.bukkit.ranks.rank.menu;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.menu.submenus.GrantsMenu;
import dev.hely.core.bukkit.ranks.rank.Rank;
import dev.hely.core.bukkit.ranks.rank.permission.PermissionData;
import dev.hely.core.bukkit.utils.material.ColorUtil;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created By LeandroSSJ
 * Created on 08/08/2023
 * Protect Name Oxygen
 */
@AllArgsConstructor
public class RankPermissionsMenu extends PaginatedMenu {

    {
        setClickUpdate(true);
    }

    final Rank rank;

    @Override
    public String getName() {
        return "Rank Permission";
    }

    @Override
    public String getTitle(Player player) {
        return "Rank Permission";
    }

    @Override
    public Map<Integer, Button> getPaginatedContent(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        this.rank.getPermissionsData().forEach(permissionData -> buttons.put(buttons.size(), new RankPermissionButton(rank, permissionData)));

        return buttons;
    }

    @AllArgsConstructor
    public static class RankPermissionButton extends Button {

        private final Rank rank;
        private final PermissionData permissionData;

        @Override
        public ItemStack getItemStack(Player player) {
            ItemMaker builder = ItemMaker.of(permissionData.isGlobal() ? Material.ENCHANTED_BOOK : Material.BOOK);
            builder.setDisplayName("&6" + permissionData.getPermission());

            builder.addLore("");
            builder.addLore("&fGlobal&7: &a" + (permissionData.isGlobal() ? "&aYes" : "&cNo"));
            builder.addLore("&fServers&7: " + (permissionData.getServers().isEmpty() ? "Global" : ""));
            for (String server : permissionData.getServers()) {
                builder.addLore("&7- &f" + server);
            }

            return builder.build();
        }

        @Override
        public void onClick(Player player, int slot, ClickType clickType) {
            if (clickType == ClickType.LEFT) {
                permissionData.setGlobal(!permissionData.isGlobal());
                rank.save();
            } else if (clickType == ClickType.MIDDLE) { // REMOVE THE PERMISSION
                rank.getPermissionsData().remove(permissionData);
                rank.save();
            } else if (clickType == ClickType.RIGHT) { // REMOVE THE SPECIFIC SERVER
                if (permissionData.isGlobal()) return;

                player.closeInventory();

                conversationString(player, "", server -> {
                    if(permissionData.getServer(server)) {
                       permissionData.getServers().remove(server);
                        rank.save();
                        return;
                    }
                    player.sendMessage("no server");
                });
            } else if (clickType == ClickType.SHIFT_LEFT) { // ADDED NEW SERVER
                if (permissionData.isGlobal()) return;

                player.closeInventory();

                conversationString(player, "", (server) -> {
                    if(permissionData.getServer(server)) {
                        player.sendMessage("already.");
                        return;
                    }
                    permissionData.getServers().add(server);
                    rank.save();
                    player.sendMessage("succesfully");
                });
            }
        }

        @Override
        public boolean toUpdate(Player player, int slot, ClickType clickType) {
            return clickType == ClickType.LEFT || clickType == ClickType.MIDDLE;
        }
    }

    private static void conversationString(Player p, String prompt, Callback<String> callback) {
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
