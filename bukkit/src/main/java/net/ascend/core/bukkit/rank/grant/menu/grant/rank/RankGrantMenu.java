package net.ascend.core.bukkit.rank.grant.menu.grant.rank;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

		RankManager rankManager = XenonBukkit.getInstance().getRankManager();
		List<Rank> sortedRanks = rankManager.getRanks().stream()
				.filter(rank -> !rank.isDefaultRank())
				.sorted(Comparator.comparingInt(Rank::getWeight))
				.collect(Collectors.toList());

		for (Rank rank : sortedRanks) {
			buttons.put(buttons.size(), new RankGrantSelectButton(rank, profile));
		}
		
		return buttons;
	}

}
