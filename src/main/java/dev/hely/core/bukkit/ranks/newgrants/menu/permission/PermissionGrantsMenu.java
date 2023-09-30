package dev.hely.core.bukkit.ranks.newgrants.menu.permission;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.ranks.newgrants.Grant;
import dev.hely.core.bukkit.ranks.newgrants.menu.GrantViewSelectMenu;
import dev.hely.core.bukkit.ranks.newgrants.menu.buttons.GrantInformationButton;
import dev.hely.core.bukkit.ranks.newgrants.types.PermissionGrant;
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
public class PermissionGrantsMenu extends FilterablePaginatedMenu<PermissionGrant> {
    private final Profile profile;

    {
        setAutoUpdate(true);
    }

    @Override
    public String getName() {
        return OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.permissions-menu.title").replace("%player%", profile.getActiveRank().getDisplay_color() + profile.getName()).replace("%amount%", String.valueOf(getFilteredCount()));
    }


    @Override
    public Map<Integer, Button> getFilteredButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();

        getMenuContent().put(36, new BackButton(new GrantViewSelectMenu(profile)));

        loop:
        for (PermissionGrant grant : profile.getPermissionGrants()) {
            for (PageFilter<PermissionGrant> filter : getFilters()) {
                if (filter.test(grant)) {
                    buttons.put(buttons.size(), new GrantInformationButton(profile, grant));
                    continue loop;
                }
            }
        }

        return buttons;
    }

    @Override
    public List<PageFilter<PermissionGrant>> generateFilters() {
        List<PageFilter<PermissionGrant>> filters = Lists.newArrayList();

        filters.add(new PageFilter<>("Type: All", (Objects::nonNull), true));
        filters.add(new PageFilter<>("Type: Temporary", (grant -> !grant.isPermanent()), false));
        filters.add(new PageFilter<>("Type: Permanent", (Grant::isPermanent), false));
        filters.add(new PageFilter<>("Type: Expired", (Grant::hasExpired), false));
        filters.add(new PageFilter<>("Type: Removed", (Grant::isRemoved), false));

        return filters;
    }

    public int getFilteredCount() {
        int count = 0;

        obj:
        for(PermissionGrant grant : profile.getPermissionGrants()) {
            for(PageFilter<PermissionGrant> filter : getFilters()) {
                if(filter.test(grant)) {
                    count++;
                    continue obj;
                }
            }
        }

        return count;
    }
}