package dev.hely.core.bukkit.ranks.newgrants.menu.grant.rank;

import java.util.Map;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.ranks.newgrants.menu.grant.GrantMenu;
import dev.hely.core.bukkit.ranks.newgrants.menu.grant.rank.button.RankGrantSelectButton;
import dev.hely.core.bukkit.ranks.rank.Rank;
import dev.hely.core.bukkit.ranks.rank.RankManager;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.button.implement.BackButton;
import dev.hely.lib.menu.implement.PaginatedMenu;
import org.bukkit.entity.Player;

import com.google.common.collect.Maps;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RankGrantMenu extends PaginatedMenu {
	private final Profile profile;
	
	@Override
	public String getName() {
		return "Select Rank";
	}

	@Override
	public String getTitle(Player player) {
		return "Select Rank";
	}

	@Override
	public Map<Integer, Button> getPaginatedContent(Player player) {
		Map<Integer, Button> buttons = Maps.newHashMap();
		
		getMenuContent().put(4, new BackButton(new GrantMenu(profile)));
		
		RankManager rankManager = OxygenBukkit.getInstance().getRankManager();
		OxygenBukkit.getInstance().debug_message("--------------------------------------------------------");
		for (Rank rank : rankManager.getRanks()) {
			OxygenBukkit.getInstance().debug_message("[Rank Grant List Menu] Adding " + rank + " to the slot: " + buttons.size());
			buttons.put(buttons.size(), new RankGrantSelectButton(rank, profile));
			OxygenBukkit.getInstance().debug_message("[Rank Grant List Menu] Success! New slot: " + buttons.size());
			OxygenBukkit.getInstance().debug_message("--------------------------------------------------------");
		}
		
		return buttons;
	}

}
