package net.ascend.core.bukkit.rank.grant.menu.grant.tag;

import java.util.Map;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.menu.grant.GrantMenu;
import net.ascend.core.bukkit.rank.grant.menu.grant.tag.button.TagGrantSelectButton;
import net.ascend.core.bukkit.tag.Tag;
import net.ascend.core.bukkit.tag.TagManager;
import net.ascend.lib.menu.button.Button;
import net.ascend.lib.menu.button.implement.BackButton;
import net.ascend.lib.menu.implement.PaginatedMenu;
import org.bukkit.entity.Player;

import com.google.common.collect.Maps;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TagGrantMenu extends PaginatedMenu {
	private final Profile profile;
	
	@Override
	public String getName() {
		return XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.tag-grant-selection.title");
	}

	@Override
	public String getTitle(Player player) {
		return XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.tag-grant-selection.title");
	}

	@Override
	public Map<Integer, Button> getPaginatedContent(Player player) {
		Map<Integer, Button> buttons = Maps.newHashMap();
		
		getMenuContent().put(4, new BackButton(new GrantMenu(profile)));
		
		TagManager tagManager = XenonBukkit.getInstance().getTagManager();
		for (Tag tag : tagManager.getTags()) {
			buttons.put(buttons.size(), new TagGrantSelectButton(tag, profile));
		}
		
		return buttons;
	}
}
