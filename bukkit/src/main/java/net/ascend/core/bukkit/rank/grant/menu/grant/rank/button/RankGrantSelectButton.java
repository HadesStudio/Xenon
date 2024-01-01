package net.ascend.core.bukkit.rank.grant.menu.grant.rank.button;

import java.util.UUID;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.menu.GrantScopesMenu;
import net.ascend.core.bukkit.rank.grant.menu.grant.rank.RankGrantMenu;
import net.ascend.core.bukkit.rank.grant.types.RankGrant;
import net.ascend.core.bukkit.rank.rank.Rank;
import net.ascend.core.bukkit.util.ColorUtil;
import net.ascend.core.bukkit.util.material.MaterialUtil;
import net.ascend.lib.maker.ItemMaker;
import net.ascend.lib.menu.button.Button;
import net.ascend.lib.CC;
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

		builder.setDisplayName(XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.rank-grant-selection.buttons.grant.display-name").replace("%rank%", rank.getName()));
		builder.setData((short)ColorUtil.getWoolColorFromChatColor(rank.getDisplay_color()));


		for (String lore : XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("menus.rank-grant-selection.buttons.grant.lore")) {
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
			if (rank.getWeight() > XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId()).getActiveRank().getWeight() && !player.isOp()) {
				player.sendMessage(CC.translate(XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("messages.higher-priority")));
				return;
			}
			
			RankGrant rankGrant = new RankGrant(UUID.randomUUID(), rank.getName());
			new GrantScopesMenu(new RankGrantMenu(profile), profile, rankGrant).openMenu(player);
			Sound.AFFIRMATIVE.toPlayer(player);
		}
	}
}
