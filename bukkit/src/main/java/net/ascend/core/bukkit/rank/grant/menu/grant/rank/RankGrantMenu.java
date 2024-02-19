package net.ascend.core.bukkit.rank.grant.menu.grant.rank;

import java.util.Map;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.menu.grant.GrantMenu;
import net.ascend.core.bukkit.rank.grant.menu.grant.rank.button.RankGrantSelectButton;
import net.ascend.core.bukkit.rank.rank.Rank;
import net.ascend.core.bukkit.rank.rank.RankManager;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.button.implement.BackButton;
import net.ascend.core.bukkit.lib.menu.implement.PaginatedMenu;
import org.bukkit.entity.Player;

import com.google.common.collect.Maps;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RankGrantMenu extends PaginatedMenu {
	private final Profile profile;
	
	@Override
	public String getName() {
		return XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.rank-grant-selection.title");
	}

	@Override
	public String getTitle(Player player) {
		return XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.rank-grant-selection.title");
	}

	@Override
	public Map<Integer, Button> getPaginatedContent(Player player) {
		Map<Integer, Button> buttons = Maps.newHashMap();
		
		getMenuContent().put(4, new BackButton(new GrantMenu(profile)));
		
		RankManager rankManager = XenonBukkit.getInstance().getRankManager();
		for (Rank rank : rankManager.getRanks()) {
			buttons.put(buttons.size(), new RankGrantSelectButton(rank, profile));
		}
		
		return buttons;
	}

}
