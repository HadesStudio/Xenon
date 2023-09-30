package dev.hely.core.bukkit.ranks.newgrants.menu.grant.tag.button;

import java.util.List;
import java.util.UUID;

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

import com.google.common.collect.Lists;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TagGrantSelectButton extends Button {
	private final Tag tag;
	private final Profile profile;
	
	@Override
	public ItemStack getItemStack(Player player) {

		ItemMaker builder = ItemMaker.of(Material.NAME_TAG);

		builder.setDisplayName("&6Tag - " + tag.getName());

		List<String> lore = Lists.newArrayList();

		lore.add("");
		lore.add("&6Left-Click &7to grant the tag &6" + tag.getName() + "&7 to &r" + profile.getColoredName() + "&7!");
		lore.add("");

		builder.setLore(lore);

		return builder.build();
	}
	
	@Override
	public void onClick(Player player, int slot, ClickType clickType) {
		if (clickType.isLeftClick()) {
			TagGrant tagGrant = new TagGrant(UUID.randomUUID(), tag.getName());
			
			new GrantScopesMenu(new TagGrantMenu(profile), profile, tagGrant, "tag").openMenu(player);
			Sound.AFFIRMATIVE.toPlayer(player);
		}
	}
}
