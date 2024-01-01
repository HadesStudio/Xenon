package net.ascend.core.bukkit.rank.grant.menu.rank.button;

import com.google.common.collect.Lists;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.types.RankGrant;
import net.ascend.core.bukkit.rank.rank.Rank;
import net.ascend.core.bukkit.rank.rank.RankManager;
import net.ascend.core.bukkit.util.ColorUtil;
import net.ascend.core.bukkit.util.material.MaterialUtil;
import net.ascend.lib.maker.ItemMaker;
import net.ascend.lib.menu.button.Button;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@RequiredArgsConstructor
public class RankGrantActiveButton extends Button {
    private final Profile profile;

    @Override
    public ItemStack getItemStack(Player player) {
        ItemMaker itemBuilder = ItemMaker.of(MaterialUtil.WOOL.getMaterial());

        itemBuilder.setDisplayName("&6Currently Active Grant");

        RankGrant grant = profile.getActiveRankGrant();
        RankManager rankManager = XenonBukkit.getInstance().getRankManager();
        Rank rank = rankManager.getRank(grant.getRankName());
        itemBuilder.setData((short)ColorUtil.getWoolColorFromChatColor(rank.getDisplay_color()));
        List<String> lore = Lists.newArrayList();

        lore.add(" ");
        lore.add("&6Rank&7: &c" + rank.getDisplay_color() + rank.getName() + " &7(" + rank.getName() + ")");
        lore.add(" ");
        lore.add("&6Added By&7: &c" + grant.getAddedByString());
        lore.add("&6Added Reason&7: &c" + grant.getAddedReason());
        lore.add("&6Added Date&7: &c" + grant.getAddedDate());
        lore.add("&6Duration&7: &c" + grant.getDurationText(false));

        if (!grant.hasExpired() && !grant.isRemoved()) {
            lore.add("&6Expiration&7: &c" + grant.getExpiration(false));
        }
        if (!grant.getServers().isEmpty()) {
            lore.add("&6Servers&7:");
            lore.add(" &7- &c" + grant.getServers().toString().replace("[", "").replace("]", ""));
        }
        lore.add(" ");

        itemBuilder.setLore(lore);

        return itemBuilder.build();
    }

}