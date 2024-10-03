package net.ascend.core.bukkit.rank.grant.menu.grant.rank.button;

import java.util.UUID;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.menu.grant.GrantScopesMenu;
import net.ascend.core.bukkit.rank.grant.menu.grant.rank.RankGrantMenu;
import net.ascend.core.bukkit.rank.grant.type.RankGrant;
import net.ascend.core.bukkit.rank.rank.Rank;
import net.ascend.core.bukkit.util.ColorUtil;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.CC;
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
		ItemMaker builder = ItemMaker.of("LEATHER_CHESTPLATE");
		builder.setDisplayName(rank.getDisplay_color() + rank.getName());
		builder.setColor(ColorUtil.getColorFromChatColor(rank.getDisplay_color()));

		builder.setLore(
				"",
				"&7Click to grant &f" + rank.getDisplay_color() + rank.getName() + " &7to &f" + profile.getColoredName() + "&7!",
				"");

		return builder.build();
	}
	
	@Override
	public void onClick(Player player, int slot, ClickType clickType) {
		if (clickType.isLeftClick()) {
			if (rank.getWeight() > XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId()).getActiveRank().getWeight() && !player.isOp()) {
				player.sendMessage(CC.translate("&cYou cannot grant ranks to players who are a higher rank thank you."));
				return;
			}
			
			RankGrant rankGrant = new RankGrant(UUID.randomUUID(), rank.getName());
			new GrantScopesMenu(new RankGrantMenu(profile), profile, rankGrant).openMenu(player);
			Sound.AFFIRMATIVE.toPlayer(player);
		}
	}
}
