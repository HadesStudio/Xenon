package dev.hely.core.bukkit.ranks.newgrants.menu.grant.tag;

import java.util.Map;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.ranks.newgrants.menu.grant.GrantMenu;
import dev.hely.core.bukkit.ranks.newgrants.menu.grant.tag.button.TagGrantSelectButton;
import dev.hely.core.bukkit.tag.Tag;
import dev.hely.core.bukkit.tag.TagManager;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.button.implement.BackButton;
import dev.hely.lib.menu.implement.PaginatedMenu;
import org.bukkit.entity.Player;

import com.google.common.collect.Maps;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TagGrantMenu extends PaginatedMenu {
	private final Profile profile;
	
	@Override
	public String getName() {
		return OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.tag-grant-selection.title");
	}

	@Override
	public String getTitle(Player player) {
		return OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.tag-grant-selection.title");
	}

	@Override
	public Map<Integer, Button> getPaginatedContent(Player player) {
		Map<Integer, Button> buttons = Maps.newHashMap();
		
		getMenuContent().put(4, new BackButton(new GrantMenu(profile)));
		
		TagManager tagManager = OxygenBukkit.getInstance().getTagManager();
		for (Tag tag : tagManager.getTags()) {
			buttons.put(buttons.size(), new TagGrantSelectButton(tag, profile));
		}
		
		return buttons;
	}
}
