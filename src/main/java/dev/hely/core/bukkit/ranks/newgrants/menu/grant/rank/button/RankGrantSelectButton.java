package dev.hely.core.bukkit.ranks.newgrants.menu.grant.rank.button;

import java.util.List;
import java.util.UUID;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.ranks.newgrants.menu.GrantScopesMenu;
import dev.hely.core.bukkit.ranks.newgrants.menu.grant.rank.RankGrantMenu;
import dev.hely.core.bukkit.ranks.newgrants.types.RankGrant;
import dev.hely.core.bukkit.ranks.rank.Rank;
import dev.hely.core.bukkit.utils.ColorUtil;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.util.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RankGrantSelectButton extends Button {
	private final Rank rank;
	private final Profile profile;
	
	@Override
	public ItemStack getItemStack(Player player) {

		ItemMaker builder = ItemMaker.of(Material.WOOL);

		builder.setDisplayName(rank.getDisplay_color() + rank.getName());
		builder.setData((short)ColorUtil.getWoolColorFromChatColor(rank.getDisplay_color()));

		List<String> lore = Lists.newArrayList();

		lore.add("");
		lore.add("&6Left-Click &7to grant this rank to " + profile.getColoredName() + "&7!");
		lore.add("");

		builder.setLore(lore);

		return builder.build();
	}
	
	@Override
	public void onClick(Player player, int slot, ClickType clickType) {
		if (clickType.isLeftClick()) {
			if (rank.getWeight() > OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId()).getActiveRank().getWeight()) {
				player.sendMessage(CC.translate(OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("messages.higher-priority")));
				return;
			}
			
			RankGrant rankGrant = new RankGrant(UUID.randomUUID(), rank.getName());
			new GrantScopesMenu(new RankGrantMenu(profile), profile, rankGrant, "rank").openMenu(player);
			Sound.AFFIRMATIVE.toPlayer(player);
		}
	}
}
