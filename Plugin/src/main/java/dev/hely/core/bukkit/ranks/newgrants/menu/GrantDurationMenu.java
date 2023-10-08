package dev.hely.core.bukkit.ranks.newgrants.menu;

import com.google.common.collect.Maps;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.ranks.newgrants.Grant;
import dev.hely.core.bukkit.ranks.newgrants.menu.buttons.scopes.PermanentDurationButton;
import dev.hely.core.bukkit.ranks.newgrants.menu.buttons.scopes.TemporaryDurationButton;
import dev.hely.lib.menu.Menu;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.button.implement.BackButton;
import dev.hely.lib.menu.fill.FillerTemplate;
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
        return OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.select-duration.title");
    }

    @Override
    public String getName() {
        return OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.select-duration.title");
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