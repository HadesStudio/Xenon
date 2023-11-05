package dev.hely.core.bukkit.ranks.newgrants.menu.grant.tag.button;

import java.util.UUID;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.ranks.newgrants.menu.GrantScopesMenu;
import dev.hely.core.bukkit.ranks.newgrants.menu.grant.tag.TagGrantMenu;
import dev.hely.core.bukkit.ranks.newgrants.types.TagGrant;
import dev.hely.core.bukkit.tag.Tag;
import dev.hely.lib.maker.ItemMaker;
import dev.hely.lib.menu.button.Button;
import org.bukkit.Material;
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

		ItemMaker builder = ItemMaker.of(Material.NAME_TAG);

		builder.setDisplayName(OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.tag-grant-selection.buttons.grant.display-name").replace("%tag%", tag.getName()));
		for (String lore : OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("menus.tag-grant-selection.buttons.grant.lore")) {
			builder.addLore(lore
					.replace("%tag_name%", tag.getDisplay())
					.replace("%prefix%", tag.getPrefix())
					.replace("%permission%", tag.getPermission())
					.replace("%weight%", String.valueOf(tag.getWeight()))
			);
		}

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
