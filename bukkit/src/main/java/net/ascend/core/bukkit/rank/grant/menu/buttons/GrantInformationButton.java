package net.ascend.core.bukkit.rank.grant.menu.buttons;

import com.google.common.collect.Lists;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.Grant;
import net.ascend.core.bukkit.rank.grant.menu.rank.RankGrantsMenu;
import net.ascend.core.bukkit.rank.grant.promts.GrantRemoveReasonPrompt;
import net.ascend.core.bukkit.rank.grant.types.PermissionGrant;
import net.ascend.core.bukkit.rank.grant.types.RankGrant;
import net.ascend.core.bukkit.rank.grant.types.TagGrant;
import net.ascend.core.bukkit.rank.rank.Rank;
import net.ascend.core.bukkit.rank.rank.RankManager;
import net.ascend.core.bukkit.tag.Tag;
import net.ascend.core.bukkit.tag.TagManager;
import net.ascend.core.bukkit.util.ColorUtil;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@RequiredArgsConstructor
public class GrantInformationButton extends Button {
    private final Profile profile;
    private final Grant grant;

    @Override
    public ItemStack getItemStack(Player player) {
        int durability = ColorUtil.getWoolColorFromChatColor(ChatColor.GREEN);

        if (grant.hasExpired()) {
            durability = ColorUtil.getWoolColorFromChatColor(ChatColor.YELLOW);
        } else if (grant.isRemoved() && grant.getRemovedBy() != null) {
            durability = ColorUtil.getWoolColorFromChatColor(ChatColor.RED);
        }

        ItemMaker itemBuilder = ItemMaker.of("WOOL");

        if (grant instanceof RankGrant) {
            RankManager rankManager = XenonBukkit.getInstance().getRankManager();

            RankGrant rankGrant = (RankGrant) grant;

            Rank rank = rankManager.getRank(rankGrant.getRankName());

            if (rank != null) {
                itemBuilder.setDisplayName(rank.getDisplay_color() + rank.getName() + "&7 (" + rank.getName() + ")");
            } else {
                itemBuilder.setDisplayName("&c" + rankGrant.getRankName());
            }
        } else if (grant instanceof PermissionGrant) {
            PermissionGrant permissionGrant = (PermissionGrant) grant;

            itemBuilder.setDisplayName("&6" + permissionGrant.getPermission() + " &7(Bungeecord: " + (permissionGrant.isBungeecord() ? "Yes" : "No") + ")");
        } else if (grant instanceof TagGrant) {
            TagManager tagManager = XenonBukkit.getInstance().getTagManager();

            TagGrant tagGrant = (TagGrant) grant;

            Tag tag = tagManager.getTag(tagGrant.getTagName());

            if (tag != null) {
                itemBuilder.setDisplayName(tag.getDisplay() + "&7 (" + tag.getName() + ")");
            } else {
                itemBuilder.setDisplayName("&c" + tagGrant.getTagName());
            }
        }

        itemBuilder.setData((short) durability);


        List<String> lore = Lists.newArrayList();

        lore.add("");
        lore.add("&6&lAdded Information:");
        lore.add(" &7- &6Added By&7: &c" + grant.getAddedByString());
        lore.add(" &7- &6Added Reason&7: &c" + grant.getAddedReason());
        lore.add(" &7- &6Added Date&7: &c" + grant.getAddedDate());
        lore.add(" &7- &6Duration&7: &c" + grant.getDurationText(false));

        if (!grant.hasExpired() && !grant.isRemoved()) {
            lore.add(" &7- &6Expiration&7: &c" + grant.getExpiration(false));
        }

        if (!grant.getServers().isEmpty()) {
            lore.add(" &7- &6Servers&7:");
            lore.add("  &7- &c" + grant.getServers().toString().replace("[", "").replace("]", ""));
        }
        lore.add(" ");
        if (grant.isRemoved()) {
            lore.add("&6&lRemoval Information:");
            lore.add(" &7- &6Removed By&7: &r" + grant.getRemovedByString());
            lore.add(" &7- &6Removed Reason&7: &r" + grant.getRemovedReason());
            lore.add(" &7- &6Removed Date&7: &r" + grant.getRemovedDate());
            lore.add(" ");
        }

        if (player.hasPermission("xenon.grants.admin")) {
            lore.add("&6Left-Click &7here to remove this grant.");
        }

        itemBuilder.setLore(lore);

        return itemBuilder.build();
    }

    @Override
    public void onClick(Player player, int slot, ClickType clickType) {
        if (clickType.isLeftClick()) {
            if (grant instanceof RankGrant) {
                RankManager rankManager = XenonBukkit.getInstance().getRankManager();
                RankGrant rankGrant = (RankGrant) grant;
                Rank rank = rankManager.getRank(rankGrant.getRankName());

                if (rank != null) {
                    if (rank.isDefaultRank()) {
                        player.sendMessage(CC.translate(XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("messages.cannot-grant")));
                        return;
                    }
                    if (!grant.hasExpired()) {
                        if (player.hasPermission("xenon.grants.admin")) {
                            new GrantRemoveReasonPrompt(grant, profile, (ignored) -> {
                                new RankGrantsMenu(profile).openMenu(player);
                            }).startPrompt(player);
                        }
                    }
                }
            }
        }
    }
}
