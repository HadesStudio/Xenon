package net.ascend.core.bukkit.rank.grant.menu.grants.rank.button;

import com.google.common.collect.Lists;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.type.RankGrant;
import net.ascend.core.bukkit.rank.rank.Rank;
import net.ascend.core.bukkit.rank.rank.RankManager;
import net.ascend.core.bukkit.util.ColorUtil;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@RequiredArgsConstructor
public class RankGrantActiveButton extends Button {

    private final Profile profile;

    @Override
    public ItemStack getItemStack(Player player) {
        ItemMaker itemBuilder = ItemMaker.of("LEATHER_CHESTPLATE");

        itemBuilder.setDisplayName("&6Currently Active Grant");

        RankGrant grant = profile.getActiveRankGrant();
        RankManager rankManager = XenonBukkit.getInstance().getRankManager();
        Rank rank = rankManager.getRank(grant.getRankName());

        itemBuilder.setColor(ColorUtil.getColorFromChatColor(rank.getDisplay_color()));
        List<String> lore = Lists.newArrayList();

        lore.add(" ");
        lore.add("&6Rank&7: &f" + rank.getDisplay_color() + rank.getName() + " &7(" + rank.getName() + ")");
        lore.add(" ");
        lore.add("&6Added By&7: &f" + grant.getAddedByString());
        lore.add("&6Added Reason&7: &f" + grant.getAddedReason());
        lore.add("&6Added Date&7: &f" + grant.getAddedDate());
        lore.add("&6Duration&7: &f" + grant.getDurationText(false));

        if (!grant.hasExpired() && !grant.isRemoved()) {
            lore.add("&6Expiration&7: &f" + grant.getExpiration(false));
        }

        if (!grant.getServers().isEmpty()) {
            lore.add("&6Servers&7:");
            lore.add(" &7- &f" + grant.getServers().toString().replace("[", "").replace("]", ""));
        }

        lore.add(" ");

        itemBuilder.setLore(lore);

        return itemBuilder.build();
    }

}