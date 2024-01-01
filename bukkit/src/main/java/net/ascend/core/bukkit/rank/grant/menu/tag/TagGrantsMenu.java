package net.ascend.core.bukkit.rank.grant.menu.tag;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.Grant;
import net.ascend.core.bukkit.rank.grant.menu.GrantViewSelectMenu;
import net.ascend.core.bukkit.rank.grant.menu.buttons.GrantInformationButton;
import net.ascend.core.bukkit.rank.grant.types.TagGrant;
import net.ascend.lib.menu.button.Button;
import net.ascend.lib.menu.button.implement.BackButton;
import net.ascend.lib.menu.implement.FilterablePaginatedMenu;
import net.ascend.lib.menu.implement.PageFilter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
public class TagGrantsMenu extends FilterablePaginatedMenu<TagGrant> {
    private final Profile profile;

    {
        setAutoUpdate(true);
    }

    @Override
    public String getName() {
        return XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.tags-menu.title").replace("%player%", profile.getActiveRank().getDisplay_color() + profile.getName()).replace("%amount%", String.valueOf(getFilteredCount()));
    }

    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.tags-menu.title").replace("%player%", profile.getActiveRank().getDisplay_color() + profile.getName()).replace("%amount%", String.valueOf(getFilteredCount()));
    }


    @Override
    public Map<Integer, Button> getFilteredButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();

        getMenuContent().put(36, new BackButton(new GrantViewSelectMenu(profile)));

        loop:
        for (TagGrant grant : profile.getTagGrants()) {
            for (PageFilter<TagGrant> filter : getFilters()) {
                if (filter.test(grant)) {
                    buttons.put(buttons.size(), new GrantInformationButton(profile, grant));
                    continue loop;

                }
            }
        }

        return buttons;
    }

    @Override
    public List<PageFilter<TagGrant>> generateFilters() {
        List<PageFilter<TagGrant>> filters = Lists.newArrayList();

        filters.add(new PageFilter<>("Filter: All Tags", (Objects::nonNull), true));
        filters.add(new PageFilter<>("Filter: Temporary Tags", (grant -> !grant.isPermanent()), false));
        filters.add(new PageFilter<>("Filter: Permanent Tags", (Grant::isPermanent), false));
        filters.add(new PageFilter<>("Filter: Expired Tags", (Grant::hasExpired), false));
        filters.add(new PageFilter<>("Filter: Removed Tags", (Grant::isRemoved), false));

        return filters;
    }

    public int getFilteredCount() {
        int count = 0;

        obj:
        for(TagGrant grant : profile.getTagGrants()) {
            for(PageFilter<TagGrant> filter : getFilters()) {
                if(filter.test(grant)) {
                    count++;
                    continue obj;
                }
            }
        }

        return count;
    }
}

