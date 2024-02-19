package net.ascend.core.bukkit.rank.grant.menu.rank;

import com.google.common.collect.Lists;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.Grant;
import net.ascend.core.bukkit.rank.grant.menu.GrantViewSelectMenu;
import net.ascend.core.bukkit.rank.grant.menu.buttons.GrantInformationButton;
import net.ascend.core.bukkit.rank.grant.menu.rank.button.RankGrantActiveButton;
import net.ascend.core.bukkit.rank.grant.types.RankGrant;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.button.implement.BackButton;
import net.ascend.core.bukkit.lib.menu.button.implement.PageButton;
import net.ascend.core.bukkit.lib.menu.button.implement.PageFilterButton;
import net.ascend.core.bukkit.lib.menu.implement.FilterablePaginatedMenu;
import net.ascend.core.bukkit.lib.menu.implement.PageFilter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
public class RankGrantsMenu extends FilterablePaginatedMenu<RankGrant> {
    private final Profile profile;

    {
        setAutoUpdate(true);
    }

    @Override
    public String getName() {
        return XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.ranks-menu.title").replace("%player%", profile.getActiveRank().getDisplay_color() + profile.getName()).replace("%amount%", String.valueOf(getFilteredCount()));
    }

    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.ranks-menu.title").replace("%player%", profile.getActiveRank().getDisplay_color() + profile.getName()).replace("%amount%", String.valueOf(getFilteredCount()));
    }

    public Map<Integer, Button> getGlobalContent(Player player) {
        Map<Integer, Button> menuContent = new HashMap<>();

        menuContent.put(0, Button.getPlaceholder());
        menuContent.put(1, Button.getPlaceholder());
        menuContent.put(2, Button.getPlaceholder());

        menuContent.put(3, Button.getPlaceholder());
        if (profile.getActiveRankGrant() != null) {
            menuContent.put(4, new RankGrantActiveButton(profile));
        } else {
            menuContent.put(4, Button.getPlaceholder());
        }
        menuContent.put(5, Button.getPlaceholder());

        menuContent.put(6, Button.getPlaceholder());
        menuContent.put(7, Button.getPlaceholder());
        menuContent.put(8, Button.getPlaceholder());

        menuContent.put(9, Button.getPlaceholder());
        menuContent.put(17, Button.getPlaceholder());
        menuContent.put(18, Button.getPlaceholder());
        menuContent.put(26, Button.getPlaceholder());
        menuContent.put(27, Button.getPlaceholder());
        menuContent.put(35, Button.getPlaceholder());

        menuContent.put(36, new BackButton(new GrantViewSelectMenu(profile)));
        menuContent.put(37, new PageButton(-1, this));
        menuContent.put(38, Button.getPlaceholder());

        menuContent.put(39, Button.getPlaceholder());
        menuContent.put(40, new PageFilterButton<>(this));
        menuContent.put(41, Button.getPlaceholder());

        menuContent.put(42, Button.getPlaceholder());
        menuContent.put(43, new PageButton(1, this));
        menuContent.put(44, Button.getPlaceholder());

        return menuContent;
    }

    @Override
    public Map<Integer, Button> getFilteredButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        loop:
        for (RankGrant grant : profile.getRankGrants()) {
            for (PageFilter<RankGrant> filter : getFilters()) {
                if (filter.test(grant)) {
                    buttons.put(buttons.size(), new GrantInformationButton(profile, grant));
                    continue loop;
                }
            }
        }

        return buttons;
    }

    @Override
    public List<PageFilter<RankGrant>> generateFilters() {
        List<PageFilter<RankGrant>> filters = Lists.newArrayList();

        filters.add(new PageFilter<>("Filter: All Ranks", (Objects::nonNull), true));
        filters.add(new PageFilter<>("Filter: Temporary Ranks", (grant -> !grant.isPermanent()), false));
        filters.add(new PageFilter<>("Filter: Permanent Ranks", (Grant::isPermanent), false));
        filters.add(new PageFilter<>("Filter: Expired Ranks", (Grant::hasExpired), false));
        filters.add(new PageFilter<>("Filter: Removed Ranks", (grant -> grant.isRemoved() && !grant.hasExpired()), false));

        return filters;
    }

    public int getFilteredCount() {
        int count = 0;
        obj:
        for(RankGrant grant : profile.getRankGrants()) {
            for(PageFilter<RankGrant> filter : getFilters()) {
                if(filter.test(grant)) {
                    count++;
                    continue obj;
                }
            }
        }

        return count;
    }
}

