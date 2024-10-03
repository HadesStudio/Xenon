package net.ascend.core.bukkit.rank.grant.menu.grants.buttons;

import com.google.common.collect.Lists;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.log.category.LogCategories;
import net.ascend.core.bukkit.profile.log.LogType;
import net.ascend.core.bukkit.rank.grant.Grant;
import net.ascend.core.bukkit.rank.grant.menu.grants.rank.RankGrantsMenu;
import net.ascend.core.bukkit.rank.grant.menu.grants.tag.TagGrantsMenu;
import net.ascend.core.bukkit.rank.grant.promts.GrantRemoveReasonPrompt;
import net.ascend.core.bukkit.rank.grant.type.RankGrant;
import net.ascend.core.bukkit.rank.grant.type.TagGrant;
import net.ascend.core.bukkit.rank.rank.Rank;
import net.ascend.core.bukkit.rank.rank.RankManager;
import net.ascend.core.bukkit.tag.Tag;
import net.ascend.core.bukkit.tag.TagManager;
import net.ascend.core.bukkit.util.ColorUtil;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class GrantInformationButton extends Button {

    private final Profile profile;
    private final Grant grant;

    @Override
    public ItemStack getItemStack(Player player) {
        int durability = ColorUtil.getWoolColorFromChatColor(ChatColor.GREEN);
        String status = "(Active)";
        if (grant.hasExpired()) {
            durability = ColorUtil.getWoolColorFromChatColor(ChatColor.YELLOW);
            status = "(Expired)";
        } else if (grant.isRemoved() && grant.getRemovedBy() != null) {
            durability = ColorUtil.getWoolColorFromChatColor(ChatColor.RED);
            status = "(Removed)";
        }

        ItemMaker itemBuilder = ItemMaker.of("WOOL");

        boolean canRemove = true;
        if (grant instanceof RankGrant) {
            RankManager rankManager = XenonBukkit.getInstance().getRankManager();

            RankGrant rankGrant = (RankGrant) grant;

            Rank rank = rankManager.getRank(rankGrant.getRankName());

            if (rank != null) {
                //itemBuilder.setDisplayName(rank.getDisplay_color() + rank.getName() + "&7 (" + rank.getName() + ")");
            //    itemBuilder.setDisplayName("&a" + grant.getAddedDate());

                canRemove = !rank.isDefaultRank();
            } else {
              //  itemBuilder.setDisplayName("&a" + grant.getAddedDate());

                //itemBuilder.setDisplayName("&c" + rankGrant.getRankName());
            }
        } else if (grant instanceof TagGrant) {
            TagManager tagManager = XenonBukkit.getInstance().getTagManager();

            TagGrant tagGrant = (TagGrant) grant;

            Tag tag = tagManager.getTag(tagGrant.getTagName());
            //itemBuilder.setDisplayName("&a" + grant.getAddedDate());
        }

        itemBuilder.setDisplayName("&f" + grant.getAddedDate() + " &7" + status);
        itemBuilder.setData((short) durability);


        List<String> lore = Lists.newArrayList();

        lore.add("");
        if (grant instanceof RankGrant) {
            RankManager rankManager = XenonBukkit.getInstance().getRankManager();
            RankGrant rankGrant = (RankGrant) grant;
            Rank rank = rankManager.getRank(rankGrant.getRankName());
            if (rank != null) {
                lore.add("&6Rank&7: &f" + rank.getDisplay_color() + rank.getName());
            }
        } else if (grant instanceof TagGrant) {
            TagManager tagManager = XenonBukkit.getInstance().getTagManager();
            TagGrant tagGrant = (TagGrant) grant;
            Tag tag = tagManager.getTag(tagGrant.getTagName());
            if (tag != null) {
                lore.add("&6Tag&7: &f" + tagGrant.getTagName());
            }
        }

        lore.add("&6Reason&7: &f" + grant.getAddedReason());
        lore.add("&6Added By&7: &f" + grant.getAddedByString());
        lore.add("&6Duration&7: &f" + grant.getDurationText(false));
        if (!grant.hasExpired() && !grant.isRemoved()) {
            lore.add("&6Expiration&7: &f" + grant.getExpiration(false));
        }

        if (!grant.getServers().isEmpty()) {
            lore.add("&6Scopes&7:");
            lore.add(" &7- &f" + grant.getServers().toString().replace("[", "").replace("]", ""));
        }

        if (grant.isRemoved()) {
            lore.add(" ");
            lore.add("&6Removed By&7: &f" + grant.getRemovedByString());
            lore.add("&6Removed Reason&7: &f" + grant.getRemovedReason());
            lore.add("&6Removed Date&7: &f" + grant.getRemovedDate());
            lore.add(" ");
        }else {
            if (!grant.hasExpired() && canRemove) {
                lore.add(" ");
                if (player.hasPermission("xenon.grants.admin")) {
                    lore.add("&7Left-Click here to remove this grant.");
                }
            }
        }


        itemBuilder.setLore(lore);

        return itemBuilder.build();
    }

    @Override
    public void onClick(Player player, int slot, ClickType clickType) {
        if (clickType.isLeftClick()) {
            boolean canRemove = true;
            if (grant instanceof RankGrant) {
                RankManager rankManager = XenonBukkit.getInstance().getRankManager();

                RankGrant rankGrant = (RankGrant) grant;
                Rank rank = rankManager.getRank(rankGrant.getRankName());

                if (rank != null) {
                    canRemove = !rank.isDefaultRank();
                }
            }


            if (!grant.hasExpired() && canRemove) {
                if (!grant.isRemoved()) {
                    if (player.hasPermission("xenon.grants.admin")) {
                        player.closeInventory();

                        new GrantRemoveReasonPrompt(grant, profile, (ignored) -> {
                            if (grant instanceof RankGrant) {

                                XenonBukkit.getInstance().getLogManager().logPlayerAction(profile.getUniqueId(), profile.getName(), Arrays.asList(
                                        "&6Staff Member&7: &c" + player.getName(),
                                        "&6Reason&7: &c" + grant.getRemovedReason()
                                ), LogType.GRANT_REMOVED, LogCategories.RANK_LOGS);

                                new RankGrantsMenu(profile).openMenu(player);
                            } else if (grant instanceof TagGrant) {
                                new TagGrantsMenu(profile).openMenu(player);
                            }
                        }).startPrompt(player);
                    }
                }
            }
        }
    }
}
