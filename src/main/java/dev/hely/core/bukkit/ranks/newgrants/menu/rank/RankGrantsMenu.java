package dev.hely.core.bukkit.ranks.newgrants.menu.rank;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.ranks.newgrants.Grant;
import dev.hely.core.bukkit.ranks.newgrants.menu.GrantViewSelectMenu;
import dev.hely.core.bukkit.ranks.newgrants.menu.buttons.GrantInformationButton;
import dev.hely.core.bukkit.ranks.newgrants.menu.rank.button.RankGrantActiveButton;
import dev.hely.core.bukkit.ranks.newgrants.types.RankGrant;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.button.implement.BackButton;
import dev.hely.lib.menu.implement.FilterablePaginatedMenu;
import dev.hely.lib.menu.implement.PageFilter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

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
        return OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.ranks-menu.title").replace("%player%", profile.getActiveRank().getDisplay_color() + profile.getName()).replace("%amount%", String.valueOf(profile.getGrants().size()));
    }


    @Override
    public Map<Integer, Button> getFilteredButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();

        getMenuContent().put(36, new BackButton(new GrantViewSelectMenu(profile)));

        if (profile.getActiveRankGrant() != null) {
            getMenuContent().put(4, new RankGrantActiveButton(profile));
        }

        loop:
        for (RankGrant grant : profile.getRankGrants()) {
            for (PageFilter<RankGrant> filter : getFilters()) {
                if (filter.test(grant)) {
                    OxygenBukkit.getInstance().debug_message("[Rank Grant List Menu] Adding " + grant.getRankName() + " to the slot: " + buttons.size());
                    buttons.put(buttons.size(), new GrantInformationButton(profile, grant));
                    OxygenBukkit.getInstance().debug_message("[Rank Grant List Menu] Success! New slot: " + buttons.size());
                    OxygenBukkit.getInstance().debug_message("--------------------------------------------------------");
                    continue loop;
                }
            }
        }

        return buttons;
    }

    @Override
    public List<PageFilter<RankGrant>> generateFilters() {
        List<PageFilter<RankGrant>> filters = Lists.newArrayList();

        filters.add(new PageFilter<>("Type: All", (rankGrant -> rankGrant != null), true));
        filters.add(new PageFilter<>("Type: Temporary", (grant -> !grant.isPermanent()), true));
        filters.add(new PageFilter<>("Type: Permanent", (Grant::isPermanent), true));
        filters.add(new PageFilter<>("Type: Expired", (Grant::hasExpired), true));
        filters.add(new PageFilter<>("Type: Removed", (grant -> grant.isRemoved() && !grant.hasExpired()), true));

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

