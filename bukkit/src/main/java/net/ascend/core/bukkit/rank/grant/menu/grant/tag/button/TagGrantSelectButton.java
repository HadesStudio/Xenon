package net.ascend.core.bukkit.rank.grant.menu.grant.tag.button;

import java.util.UUID;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.menu.GrantScopesMenu;
import net.ascend.core.bukkit.rank.grant.menu.grant.tag.TagGrantMenu;
import net.ascend.core.bukkit.rank.grant.types.TagGrant;
import net.ascend.core.bukkit.tag.Tag;
import net.ascend.lib.maker.ItemMaker;
import net.ascend.lib.menu.button.Button;
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

		builder.setDisplayName(XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.tag-grant-selection.buttons.grant.display-name").replace("%tag%", tag.getName()));
		for (String lore : XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getStringList("menus.tag-grant-selection.buttons.grant.lore")) {
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
