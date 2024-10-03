package net.ascend.core.bukkit.rank.grant.menu.grant.tag.button;

import java.util.UUID;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.menu.grant.GrantScopesMenu;
import net.ascend.core.bukkit.rank.grant.menu.grant.tag.TagGrantMenu;
import net.ascend.core.bukkit.rank.grant.type.TagGrant;
import net.ascend.core.bukkit.tag.Tag;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import net.ascend.core.bukkit.lib.menu.button.Button;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

public class TagGrantSelectButton extends Button {

	private final Tag tag;
	private final Profile profile;
	
	@Override
	public ItemStack getItemStack(Player player) {

		ItemMaker builder = ItemMaker.of("NAME_TAG");

		builder.setDisplayName(tag.getName());
		builder.setLore(
				"",
				"&7Click to grant &f" + tag.getName() + " &7to &f" + profile.getColoredName() + "&7!",
				"");

		return builder.build();
	}
	
	@Override
	public void onClick(Player player, int slot, ClickType clickType) {
		if (clickType.isLeftClick()) {
			TagGrant tagGrant = new TagGrant(UUID.randomUUID(), tag.getName());
			
			new GrantScopesMenu(new TagGrantMenu(profile), profile, tagGrant).openMenu(player);
			Sound.AFFIRMATIVE.toPlayer(player);
		}
	}
}
