package dev.hely.core.bukkit.ranks.newgrants.menu.grant.rank.button;

import java.util.UUID;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.ranks.newgrants.menu.GrantScopesMenu;
import dev.hely.core.bukkit.ranks.newgrants.menu.grant.rank.RankGrantMenu;
import dev.hely.core.bukkit.ranks.newgrants.types.RankGrant;
import dev.hely.core.bukkit.ranks.rank.Rank;
import dev.hely.core.bukkit.utils.ColorUtil;
import dev.hely.core.bukkit.utils.material.MaterialUtil;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.util.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RankGrantSelectButton extends Button {
	private final Rank rank;
	private final Profile profile;
	
	@Override
	public ItemStack getItemStack(Player player) {

		ItemMaker builder = ItemMaker.of(MaterialUtil.WOOL.getMaterial());

		builder.setDisplayName(OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.rank-grant-selection.buttons.grant.display-name").replace("%rank%", rank.getName()));
		builder.setData((short)ColorUtil.getWoolColorFromChatColor(rank.getDisplay_color()));


		for (String lore : OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("menus.rank-grant-selection.buttons.grant.lore")) {
			builder.addLore(lore
					.replace("%rank_name%", rank.getName())
					.replace("%prefix%", rank.getPrefix())
					.replace("%suffix%", rank.getSuffix())
					.replace("%weight%", String.valueOf(rank.getWeight()))
					.replace("%queue_priority%", String.valueOf(rank.getQueuePriority()))
					.replace("%display_color%", rank.getDisplay_color().toString())
			);
		}

		return builder.build();
	}
	
	@Override
	public void onClick(Player player, int slot, ClickType clickType) {
		if (clickType.isLeftClick()) {
			if (rank.getWeight() > OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId()).getActiveRank().getWeight() && !player.isOp()) {
				player.sendMessage(CC.translate(OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("messages.higher-priority")));
				return;
			}
			
			RankGrant rankGrant = new RankGrant(UUID.randomUUID(), rank.getName());
			new GrantScopesMenu(new RankGrantMenu(profile), profile, rankGrant).openMenu(player);
			Sound.AFFIRMATIVE.toPlayer(player);
		}
	}
}
