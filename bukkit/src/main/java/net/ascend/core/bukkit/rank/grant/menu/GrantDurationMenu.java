package net.ascend.core.bukkit.rank.grant.menu;

import com.google.common.collect.Maps;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.Grant;
import net.ascend.core.bukkit.rank.grant.menu.buttons.scopes.PermanentDurationButton;
import net.ascend.core.bukkit.rank.grant.menu.buttons.scopes.TemporaryDurationButton;
import net.ascend.core.bukkit.lib.menu.Menu;
import net.ascend.core.bukkit.lib.menu.button.Button;
import net.ascend.core.bukkit.lib.menu.button.implement.BackButton;
import net.ascend.core.bukkit.lib.menu.fill.FillerTemplate;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class GrantDurationMenu extends Menu {
    private final Menu backMenu;
    private final Profile profile;
    private final Grant grant;
    private final List<String> servers;

    @Override
    public FillerTemplate getFillerTemplate() {
        return FillerTemplate.FILL;
    }

    @Override
    public String getTitle(Player player) {
        return XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.select-duration.title");
    }

    @Override
    public String getName() {
        return XenonBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.select-duration.title");
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();

        buttons.put(4, new BackButton(new GrantScopesMenu(backMenu, profile, grant)));

        buttons.put(11, new PermanentDurationButton(profile, grant, servers));
        buttons.put(15, new TemporaryDurationButton(profile, grant, servers));

        return buttons;
    }

    @Override
    public int getSize() {
        return 27;
    }
}