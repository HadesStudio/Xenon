package dev.hely.core.bukkit.ranks.newgrants.menu;

import com.google.common.collect.Maps;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.ranks.newgrants.Grant;
import dev.hely.core.bukkit.ranks.newgrants.menu.buttons.scopes.BukkitPermissionButton;
import dev.hely.core.bukkit.ranks.newgrants.menu.buttons.scopes.BungeePermissionButton;
import dev.hely.core.bukkit.ranks.newgrants.menu.buttons.scopes.GlobalRankButton;
import dev.hely.core.bukkit.ranks.newgrants.menu.buttons.scopes.SpecifyServersButton;
import dev.hely.core.bukkit.ranks.newgrants.types.PermissionGrant;
import dev.hely.lib.menu.Menu;
import dev.hely.lib.menu.button.Button;
import dev.hely.lib.menu.button.implement.BackButton;
import dev.hely.lib.menu.fill.FillerTemplate;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Map;

@RequiredArgsConstructor
public class GrantScopesMenu extends Menu {
    private final Menu backMenu;
    private final Profile profile;
    private final Grant grant;

    @Override
    public String getTitle(Player player) {
        return OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.select-server.title");
    }

    @Override
    public FillerTemplate getFillerTemplate() {
        return FillerTemplate.FILL;
    }

    @Override
    public String getName() {
        return OxygenBukkit.getInstance().getGrantMenuConfig().getConfiguration().getString("menus.select-server.title");
    }

    @Override
    public Map<Integer, Button> getMenuContent(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();

        buttons.put(4, new BackButton(backMenu));

        if (grant instanceof PermissionGrant) {
            PermissionGrant permissionGrant = (PermissionGrant) grant;

            buttons.put(11, new BungeePermissionButton(profile, grant, permissionGrant));
            buttons.put(13, new BukkitPermissionButton(profile, grant, permissionGrant));
        } else {
            buttons.put(11, new GlobalRankButton(profile, grant));
        }


        buttons.put(15, new SpecifyServersButton(profile, grant));

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 3;
    }
}

